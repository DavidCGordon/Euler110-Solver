import java.util.ArrayList;

public class PrimeGenerator {

    public static int[] generate(int lim) {
        ArrayList <Integer> primes = new ArrayList <Integer>();
        ArrayList <Integer> multiples =  new ArrayList <Integer>();
        
        boolean isMultiple;
        int n = 2;
        while (primes.size() < lim) {
            isMultiple = false;
            for (int i = 0; i < multiples.size(); i++) {
                if (n == (int) multiples.get(i)) {   
                    multiples.set(i, multiples.get(i) + primes.get(i));
                    isMultiple = true;
                }
            }

            if (!isMultiple) {
                primes.add(n);
                multiples.add((int) Math.pow(n, 2));
            } 
            n++;
        }
        int[] out = new int[primes.size()];
        for (int i = 0; i < primes.size(); i++) {
            out[i] = primes.get(i);
        }
        return out;
    }
} 