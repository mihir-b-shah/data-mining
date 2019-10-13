
package frequent_itemsets;

import java.io.File;
import java.util.Scanner;

public class prime_gen {
    private static final short[] primes;
    private static final short[] back;
    
    static {
        primes = new short[1000];
        back = new short[8000];
        try {
            Scanner f = new Scanner(new File("primes1000.txt"));
            for (short i = 0; i < 1000; ++i) {
                primes[i] = f.nextShort();
                back[primes[i]] = i;
            }
            f.close();
        } catch (Exception e) {
        }
    }
    
    public static short get_nth_prime(int idx) {
        return primes[idx];
    }
    
    public static int get_index(int prime) {
        return back[prime];
    }
}
