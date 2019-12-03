
import java.util.HashMap;
import java.util.Random;

public class fastintmap_test {
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        HashMap<String, Integer> hmap = new HashMap<>(4500000);
        FastIntMap<String> fmap = new FastIntMap<>(9000011);
        
        String[] keys = new String[4500000];
        int[] vals = new int[4500000];
        Random r = new Random();
        
        for(int i = 0; i<4500000; ++i) {
            char[] string = new char[10];
            for(int j = 0; j<10; ++j) {
                string[j] = (char) (1+r.nextInt(127));
            }
            keys[i] = new String(string);
            vals[i] = r.nextInt();
            if(vals[i] == -1) {
                vals[i] = r.nextInt();
            }
        }

        long mem = runtime.freeMemory();
        long T = System.nanoTime();
        for(int i = 0; i<4500000; ++i) {
            hmap.put(keys[i], vals[i]);
        }
        System.out.println("4.5m hashmap memory: " + (mem-runtime.freeMemory()));
        System.out.println("4.5m hashmap insert: " + (System.nanoTime()-T));

        long mem2 = runtime.freeMemory();
        T = System.nanoTime();
        for(int i = 0; i<4500000; ++i) {
            fmap.insert(keys[i], vals[i]);
        }
        System.out.println("4.5m fastmap memory: " + (mem2-runtime.freeMemory()));
        System.out.println("4.5m fastmap insert: " + (System.nanoTime()-T));
        
        T = System.nanoTime();
        for(int i = 4499999; i>=0; --i) {
            hmap.get(keys[i]);
        }
        System.out.println("4.5m hashmap get: " + (System.nanoTime()-T));
        
        T = System.nanoTime();
        for(int i = 4499999; i>=0; --i) {
            fmap.get(keys[i]);
        }
        System.out.println("4.5m fastmap get: " + (System.nanoTime()-T));
    }

    private static class FastIntMap<T> {
        private long[] data;
        private static final long HASH_MASK = 0x7fff_ffff;
        private static final int NOT_FOUND = -1;
        private int size;

        /**
         * Hashcodes of objects inserted should NEVER be zero and map to 0 value
         * They should also not map to the key -1
         */
        public FastIntMap() {
            data = new long[1021];
        }

        /**
         * Hashcodes should never be zero AND map to 0 value
         *
         * @param N should be prime
         */
        public FastIntMap(int N) {
            data = new long[N];
        }

        public void insert(T obj, long v) {
            int iter;
            long hcode;
            if (size > data.length >>> 1) {
                long[] aux = data;
                data = new long[data.length << 1];
                for (int i = 0; i < aux.length; ++i) {
                    iter = (int) adjustIndex(hcode = aux[i] & HASH_MASK);
                    while (data[iter %= data.length] != 0L) {
                        ++iter;
                    }
                    data[iter] = hcode + (v << 32);
                }
            } else {
                iter = (int) adjustIndex(hcode = obj.hashCode() & HASH_MASK);
                while (data[iter %= data.length] != 0L) {
                    ++iter;
                }
                data[iter] = hcode + (v << 32);
            }
        }

        /**
         * @param obj the object
         * @return -1 if not found, else the mapping value
         */
        public int get(T obj) {
            long hcode; 
            int iter;
            iter = (int) adjustIndex(hcode = obj.hashCode() & HASH_MASK);
            while (data[iter %= data.length] != 0L) {
                if(hcode == (data[iter] & HASH_MASK)) 
                    return (int) (data[iter] >>> 32);
                ++iter;
            }
            return NOT_FOUND;
        }

        private final long adjustIndex(long val) {
            long hash = val % data.length;
            return hash > 0 ? hash : hash + data.length;
        }

        public int size() {
            return size;
        }
    }
}
