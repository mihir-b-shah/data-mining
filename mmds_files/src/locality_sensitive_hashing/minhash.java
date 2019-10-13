
package locality_sensitive_hashing;

import java.util.BitSet;
import java.util.function.Function;
import java.util.Arrays;

public class minhash {
    private static final int LIM = 37*37*37*37;
    private static final int TABLE_SIZE = 100000;
    private static final Function<Integer,Integer>[] hash_fs;

    static {
        hash_fs = new Function[100];
        for(int i = 0; i<hash_fs.length; ++i) {
            double r1,r2;
            r1 = 100*Math.random(); r2 = 100*Math.random();
            hash_fs[i] = x->((int) (r1*x+r2)%100000);
        }
    }
    
    public static double jaccard_siml(BitSet b1, BitSet b2) {
        int[][] tbl = new int[hash_fs.length][2];
        for(int i = 0; i<hash_fs.length; ++i)
            Arrays.fill(tbl[i],1_000_000_000);
        for(int i = 0; i<100000; ++i) 
            for(int j = 0; j<hash_fs.length; ++j) {
                if(b1.get(i)) 
                    tbl[j][0] = Math.min(tbl[j][0], hash_fs[j].apply(i));
                if(b2.get(i))
                    tbl[j][1] = Math.min(tbl[j][1], hash_fs[j].apply(i));
                //System.out.println(hash_fs[j].apply(i));
            }
        
        double aggr = 0;
        for(int i = 0; i<hash_fs.length; ++i)
            aggr += tbl[i][0] == tbl[i][1] ? 1 : 0;
        return aggr/hash_fs.length;
    }
    
    public static BitSet gen_bitset(String file) {
        BitSet f_bool = new BitSet(LIM);
        
        int hash = 0;
        for(int i = 0; i<10; ++i)
            hash += file.charAt(i)*(1 << 9-i);
        
        f_bool.set(hash);
        for(int i = 1; i<file.length()-10; ++i) {
            hash = (file.charAt(i+9) << 9) - file.charAt(i-1);
            f_bool.set(hash%TABLE_SIZE);
        }
        return f_bool;
    }    
                
}
