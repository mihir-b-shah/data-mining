
// conclusion literally anything is fine except the wrapper class
// if i had to guess the float operations are good

public class infloat_class {

    private static final int MASK = 0xffff_ffff;
    private static final long BACK = 0xffff_ffff_0000_0000L;
    
    private static class intfloat {

        private final int idx;
        private float prob;

        public intfloat(int idx, float p) {
            this.idx = idx;
            prob = p;
        }

        public void incr_prob(float add) {
            prob += add;
        }
    }
    
    public static long alg1() {
        /*
        long T = System.nanoTime();
        intfloat[] arr_1 = new intfloat[1000];
        for (int i = 0; i < 1000; ++i) {
            arr_1[i] = new intfloat(i, (float) Math.random());
        }
        for(int i = 0; i < 1000; ++i) {
            arr_1[i].incr_prob((float) Math.random());
        }
        return System.nanoTime()-T; */
        long T = System.nanoTime();
        float[] arr_3 = new float[1000];
        for (int i = 0; i < 1000; ++i) {
            arr_3[i] = (float) (i+Math.random()*(0xffff_ffff));
        }
        long val;
        for(int i = 0; i < 1000; ++i) {
            arr_3[i] += Math.random()*(0xffff_ffff);
        }
        return System.nanoTime()-T;
    }
    
    public static long alg2() {    
        long T = System.nanoTime();
        double[] arr_3 = new double[1000];
        for (int i = 0; i < 1000; ++i) {
            arr_3[i] = i+Math.random()*(1L<<32);
        }
        long val;
        for(int i = 0; i < 1000; ++i) {
            arr_3[i] += Math.random()*(1L<<32);
        }
        return System.nanoTime()-T;
    }
    
    public static long alg3() {
        long T = System.nanoTime();
        long[] arr_2 = new long[1000];
        for (int i = 0; i < 1000; ++i) {
            arr_2[i] = Float.floatToIntBits((float) Math.random())
                    + (((long) i) << 32);
        }
        long val;
        for(int i = 0; i < 1000; ++i) {
            arr_2[i] = (arr_2[i]&BACK) + 
                    Float.floatToIntBits(((float) Math.random())*
                            Float.intBitsToFloat((int) (arr_2[i]&MASK)));
        }
        return System.nanoTime()-T;
    }

    public static void main(String[] args) {
        int c1 = 0; int c2 = 0; int c3 = 0;
        for(int i = 0; i<25; ++i) {
            long T1 = 0;
            long T2 = 0;
            long T3 = 0;
            for (int k = 0; k < 1000; ++k) {
                int s = (int) (Math.random()*6);
                switch(s) {
                    case 0: T1 += alg1(); T2 += alg2(); T3 += alg3(); break;
                    case 1: T1 += alg1(); T3 += alg3(); T2 += alg2(); break;
                    case 2: T2 += alg2(); T1 += alg1(); T3 += alg3(); break;
                    case 3: T2 += alg2(); T3 += alg3(); T1 += alg1(); break;
                    case 4: T3 += alg3(); T1 += alg1(); T2 += alg2(); break;
                    case 5: T3 += alg3(); T2 += alg2(); T1 += alg1(); break;
                    default: System.err.println("wtf are you doing!!!");
                }
            }
            if(T1 < T3 && T2 < T3) {
                ++c1;
            } else if(T2 < T3) {
                ++c2;
            } else {
                ++c3;
            }
            
        }
        
        System.out.printf("reg float: %d, bs float: %d, bitmask: %d%n", c1, c2, c3);
    }

}
