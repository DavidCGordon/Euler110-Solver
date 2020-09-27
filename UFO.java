import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author David Gordon
 * @version 11.0.5
 */
public class UFO {
    private final int[] primes;
    private final int[] powers;
    private final double numSol;
    private double actualSol;
    private int numChecked = 0;
    private BigInteger curMin;
    private int numPrimes;
    public int primeIdx;
    

    /**
     * Constructor for UnitFractionOperator (UFO)
     * @param s a double, containing the desired number of solutions
     */
    public UFO(double s) {
        numSol = s;

        final double log3 = Math.log(2 * s - 1) / Math.log(3);
        numPrimes = (int) Math.ceil(log3);

        int first = (int) (numPrimes - (Math.pow(numPrimes, .5)));
        int last = numPrimes;
        primeIdx = (first + last-1) / 2;

        primes = PrimeGenerator.generate(numPrimes);
        powers = new int[numPrimes];

        for (int i = 0; i < primeIdx-1; i++) {
            powers[i] = 1;
        }
        curMin = BigInteger.ONE;
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
        BigInteger tot = BigInteger.ONE;
        for (int i = 0; i < primes.length; i++) {
            powerCopy = powers[i];
            while (powerCopy > 8) {
                prime = (long) Math.pow(primes[i], 8);
                tot = tot.multiply(BigInteger.valueOf(prime));
                powerCopy -= 8;
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
            sol *= (2 * powers[i] + 1);
        }
        return ((sol + 1) / 2);
    }

    /**
     * "Rolls over" the UFO, (sets everything before a certain index equal), increasing how
     * far it rolls over until the number held is less than curMin. This is what
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
     * This increments the UFO through good potential candidates only. it starts
     * with an almost full list of ones, then it will increment, rolling over until the next digit after prime idx fills
     */
    public void increment() {
        powers[0]++;
        rollOver();
        numChecked += 1;
    }

    /**
     * Solve the problem
     */
    public void solve () {
        if (primeIdx != primes.length){
            while (powers[primeIdx+1] == 0) {
                increment();
                if (getSol() >= numSol && curMin.compareTo(getNum()) > 0) {
                    curMin = getNum();
                    actualSol = getSol();
                    //System.out.println(Arrays.toString(powers));
                }
            }
        }
    }

    public int getUsed () {
        BigInteger prime;
        for (int i = 0; i < primes.length; i++) {
            prime = BigInteger.valueOf(primes[i]);
            if (!(curMin.remainder(prime).equals(BigInteger.ZERO))) {
                return i;
            }
        }
        return primes.length;
    }
    public static void main(final String[] args) {
        final long start = System.nanoTime();

        final UFO UF = new UFO((Double.parseDouble(args[0])));
        UF.solve();
        System.out.println(UF.curMin);

        final long end = System.nanoTime();
        final long elapsed = end - start;

        System.out.println("Elapsed time in Nanoseconds: " + elapsed);
        System.out.println("Elapsed time in Milliseconds: " + (double) elapsed/1000000);
        System.out.println("Number of Candidates checked: " + UF.numChecked);
        System.out.println("Actual number of solutions: " + UF.actualSol);
        System.out.println("Primorial limit: " + UF.numPrimes);
        System.out.println("Primes used: " + UF.getUsed());
        System.out.println("PrimeIdx: " + UF.primeIdx);
    }
}
