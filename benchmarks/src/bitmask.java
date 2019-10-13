
public class bitmask {
    
    public static void main(String[] args) {
        long T = System.nanoTime();
        long[] Ts = new long[3];
        Ts[0] = T;
        // bitmask 1
        double[] a1 = new double[1_000_000];
        int[] a2 = new int[1_000_000];
        int mask = 0xFFFF;
        
        for(int i = 0; i<1000000; ++i) {
            double v = Math.random();
            a1[i] = v+i;
            a2[i] = (int) (mask*v)+i<<16;
        }
        
        double p;
        int ix;
        
        System.out.printf("setup: %d%n", System.nanoTime()-T);
        T = System.nanoTime();
        
        // bitmask 1
        
        for(int i = 0; i<1000000; ++i) {
            p = a1[i] % 1;
            ix = (int) a1[i] / 1;
        }
        
        System.out.printf("fp: %d%n", System.nanoTime()-T);
        T = System.nanoTime();
        
        // bitmask 2

        for(int i = 0; i<1000000; ++i) {
            p = a2[i] & 0xFFFF;
            ix = a2[i] >>> 16;
        }
        
        System.out.printf("int: %d%n", System.nanoTime()-T);
        
    }
    
}
