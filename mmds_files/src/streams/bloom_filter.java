
package streams;

import java.util.function.Function;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Scanner;

public class bloom_filter {
    
    private static final Function<String,Integer>[] hash_fs;
    
    static {
        hash_fs = new Function[4];
    }
    
    public static void main(String[] args) {
        Scanner f = new Scanner(System.in);
        System.out.println("Randomized strings? 1/0:");
        int yn = f.nextInt();
        f.nextLine();
        
        BitSet bf = new BitSet(1_000_000);
        HashSet<String> strs = new HashSet<>();
        
        hash_fs[0] = (x)->x.hashCode()%972_373;
        hash_fs[1] = (x)->x.hashCode()%857_903;
        hash_fs[2] = (x)->x.hashCode()%752_177;
        hash_fs[3] = (x)->x.hashCode()%657_109;

        if(yn == 0) {
            String v;
            while((v = f.nextLine()).equals("DONE")) {
                strs.add(v);
                for(Function fi: hash_fs) {
                    bf.set((Integer) fi.apply(v));
                }
            }
        } else {
            // generate random strings
            char[] aux = new char[5];
            for(int i = 0; i<100_000; ++i) {
                for(int j = 0; j<(int) (Math.random()*aux.length); ++j) {
                    aux[j] = (char) (65+(Math.random()*26));
                }
                String s = new String(aux);
                strs.add(s);
                for(Function<String,Integer> fi: hash_fs) {
                    bf.set(fi.apply(s));
                }
            }
        }

        // generate random strings
        char[] aux = new char[5];
        int ctr = 0;
        for(int i = 0; i<100_000; ++i) {
            for(int j = 0; j<(int) (Math.random()*aux.length); ++j) {
                aux[j] = (char) (65+(Math.random()*26));
            }
            String s = new String(aux);
            boolean yes = true;
            for(Function<String,Integer> fi: hash_fs) {
                yes &= bf.get(fi.apply(s));
            }
            if(yes && !strs.contains(s)) {
                ++ctr;
            }
        }
        
        System.out.println("PROB OF ERROR: " + ctr/100_000d);
    }
}
