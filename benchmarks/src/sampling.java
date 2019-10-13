
import java.util.BitSet;
import java.util.HashSet;

/* moral of the story, all hail boolean arrays
   also look at the overheads for bitset and hashset! */

public class sampling {
    
    private static long T_hash,M_hash;
    private static long T_bits,M_bits;
    private static long T_bool,M_bool;
    private static final Runtime RUNTIME = Runtime.getRuntime();
    
    public static void main(String[] args) {
        for(int k = 0; k<20; ++k) {
            int[] array = new int[10_000];
            for(int i = 0; i<array.length; ++i) {
                array[i] = (int) (1_000_000*Math.random());
            }
            int[] sampl = new int[10_000];
            for(int i = 0; i<100; ++i) {
                int desc = (int) (6*Math.random());
                switch(desc) {
                    case 0: 
                        hashset_alg(sampl, array);
                        bitset_alg(sampl, array);
                        boolarray_alg(sampl, array);
                        break;
                    case 1: 
                        hashset_alg(sampl, array);
                        boolarray_alg(sampl, array);
                        bitset_alg(sampl, array);
                        break;
                    case 2:
                        bitset_alg(sampl, array);                    
                        hashset_alg(sampl, array);
                        boolarray_alg(sampl, array);
                        break;
                    case 3:
                        bitset_alg(sampl, array);    
                        boolarray_alg(sampl, array);                    
                        hashset_alg(sampl, array);
                        break;
                    case 4:
                        boolarray_alg(sampl, array);                    
                        hashset_alg(sampl, array);
                        bitset_alg(sampl, array);
                        break;
                    case 5: 
                        boolarray_alg(sampl, array);
                        bitset_alg(sampl, array);                    
                        hashset_alg(sampl, array);
                        break;
                }   
            }
            
            System.out.printf("Hashset: %d, Bitset: %d, Bool array: %d%n",
                T_hash, T_bits, T_bool);
            System.out.printf("Hashset: %d, Bitset: %d, Bool array: %d%n%n",
                M_hash, M_bits, M_bool);
            T_hash = 0;
            T_bits = 0;
            T_bool = 0;
            M_hash = 0;
            M_bits = 0;
            M_bool = 0;
        }
    }
    
    public static void hashset_alg(int[] sampl, int[] array) {
        RUNTIME.gc();
        long T = System.nanoTime();
        final HashSet<Integer> set = new HashSet<>(array.length);
        int val;
        final int size = sampl.length;
        for(int i = 0; i<size;) {
            val = (int) (Math.random()*array.length);
            if(!set.contains(val)) {
                sampl[i++] = array[val];
                set.add(val);
            }
        }
        T_hash += System.nanoTime()-T;
        M_hash += RUNTIME.totalMemory()-RUNTIME.freeMemory();
    }
    
    public static void bitset_alg(int[] sampl, int[] array) {
        RUNTIME.gc();
        long T = System.nanoTime();
        final BitSet set = new BitSet(array.length);
        int val;
        final int size = sampl.length;
        for(int i = 0; i<size;) {
            val = (int) (Math.random()*array.length);
            if(!set.get(val)) {
                sampl[i++] = array[val];
                set.set(val);
            }
        }
        T_bits += System.nanoTime()-T;
        M_bits += RUNTIME.totalMemory()-RUNTIME.freeMemory();
    }
    
    public static void boolarray_alg(int[] sampl, int[] array) {
        RUNTIME.gc();
        long T = System.nanoTime();
        final boolean[] set = new boolean[array.length];
        int val;
        final int size = sampl.length;
        for(int i = 0; i<size;) {
            val = (int) (Math.random()*array.length);
            if(!set[val]) {
                sampl[i++] = array[val];
                set[val] = true;
            }
        }
        T_bool += System.nanoTime()-T;
        M_bool += RUNTIME.totalMemory()-RUNTIME.freeMemory();
    }
}
