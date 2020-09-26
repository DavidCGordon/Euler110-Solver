import java.math.BigInteger;

/**
 * @author David Gordon
 * @version 11.0.5
 */
public class UFO2 {
    private final int[] primes;
    private final int[] powers;
    private final double numSol;
    private BigInteger curMin = BigInteger.ONE;
    private final int first;
    private int last;
    private int numChecked = 0;

    /**
     * Constructor for UnitFractionOperator (UFO)
     * @param s a double, containing the desired number of solutions
     */
    public UnitFractionOperator2(double s) {
        numSol = s;

        final double log3 = Math.log(2 * s + 1) / Math.log(3);
        final int num = (int) Math.ceil(log3);

        primes = PrimeGenerator.generate(num);
        powers = new int[num];
        first = (int) (num - (Math.pow(num, .5))) - 1;
        last = num - 2;

        for (int i = 0; i < primes.length - 1; i++) {
            powers[i] = 1;
        }
        for (final int p : primes) {
            curMin = curMin.multiply(BigInteger.valueOf(p));
        }
    }

    public BigInteger getCurMin() {
        return curMin;
    }
    /**
     * Calculates the actual number currently held by the UFO
     * @return BigInt tot (number held currently held).
     */
    public BigInteger getNum() {
        int powerCopy;
        Long prime;
        BigInteger tot = new BigInteger("1");
        for (int i = 0; i < primes.length; i++) {
            powerCopy = powers[i];
            while (powerCopy > 32) {
                prime = (long) Math.pow(primes[i], 32);
                tot = tot.multiply(BigInteger.valueOf(prime));
                powerCopy -= 32;
            }
            prime = (long) Math.pow(primes[i], powerCopy);
            tot = tot.multiply(BigInteger.valueOf(prime));
        }
        return tot;
    }

    /**
     * Calculates the number of solutions, using the fact that sol = the product of
     * (2a+1) for a in powers.
     * @return sol, the number of solutions of the number currently held.
     */
    public double getSol() {
        double sol = 1;
        for (int i = 0; i < primes.length; i++) {
            sol *= (int) (2 * powers[i] + 1);
        }
        return (sol / 2);
    }

    /**
     * "Rolls over" the UFO, (sets everything equal up to a point) , increasing how
     * far it rolls over until the number held is less than curMin. this is what
     * makes the UFO increment through good candidates only.
     */
    private void rollOver() {
        int idx = 0;
        while (curMin.compareTo(getNum()) < 0 && idx < powers.length - 1) {
            for (int i = 0; i <= idx + 1; i++) {
                powers[i] = powers[idx + 1] + 1;
            }
            idx++;
        }
    }

    /**
     * if the index after last has filled (due to a roll-over that went past all
     * current digits), then this method resets everything to ones (because you are
     * entering an index you have already checked, so it needs to be reset). finally
     * reduces last by 1.
     */
    private void reset() {
        if (powers[last + 1] > 0) {
            for (int i = 0; i < powers.length; i++) {
                powers[i] = 0;
            }
            for (int i = 0; i < last; i++) {
                powers[i] = 1;
            }
            last -= 1;
        }
    }

    /**
     * This increments the UFO through good potential candidates only. it starts
     * with an almost full list of ones, then it will increment, rolling over until
     * powers[last + 1] fills, then jumps back 2 (to last-1), due to reset.
     */
    public void increment() {
        powers[0]++;
        rollOver();
        reset();
        numChecked += 1;
    }

    public int getUsed () {
        BigInteger prime;
        for (int i = 0; i < primes.length; i++) {
            prime = BigInteger.valueOf(primes[i]);
            if (!(curMin.remainder(prime).equals(BigInteger.ZERO))) {
                return i;
            }
        }
        return -1;
    }

    public void solve() {
        while (last >= first) {
            increment();
            if (getSol() > numSol && curMin.compareTo(getNum()) > 0) {
                curMin = getNum();
            }
        }
    }
    public static void main(final String[] args) {
        final long start = System.nanoTime();

        final UFO2 UF = new UnitFractionOperator2((Double.parseDouble(args[0])));
        UF.solve();
        System.out.println(UF.curMin);

        final long end = System.nanoTime();
        final long elapsed = end - start;
        final int used = UF.getUsed();
        final int front = used - UF.first;
        final int back = UF.primes.length - used;

        System.out.println("Elapsed time in Nanoseconds: " + elapsed);
        System.out.println("Elapsed time in Milliseconds: " + (double) elapsed/1000000);
        System.out.println("number of solutions checked: " + UF.numChecked);
        System.out.println("Primorial limit: " + UF.primes.length);
        System.out.println("Primes used: " + used);
        System.out.println("Space on front: " + front);
        System.out.println("Space on back: " + back);
    }
}
