
import java.util.HashMap;


public class map_autobox {
    
    private static class float_wrap {
        private float val;
        
        public float_wrap(float val) {
            this.val = val;
        }
        
        public float get_val() {
            return val;
        }
        
        public float_wrap incr_val(float val) {
            this.val += val;
            return this;
        }
    }
    
    public static long test_1() {
        HashMap<Integer,float_wrap> map_2 = new HashMap<>();
        long T2 = System.nanoTime();

        for(int i = 0; i<10_000; ++i) {
            int val = i%10;
            if(map_2.containsKey(val)){
                map_2.put(val, map_2.get(val).incr_val((float) Math.random()));
            } else {
                map_2.put(val, new float_wrap((float) Math.random()));
            }
        }
        return System.nanoTime()-T2;
    }
    
    public static long test_2() {
        HashMap<Integer,Float> map_1 = new HashMap<>();      

        long T1 = System.nanoTime();
        for(int i = 0; i<10_000; ++i) {
            int val = i%10;
            if(map_1.containsKey(val)){
                map_1.put(val, map_1.get(val)+(float) Math.random());
            } else {
                map_1.put(val, (float) Math.random());
            }
        }
        
        return System.nanoTime()-T1;
    }
    
    public static void main(String[] args) {
        long t1_avg = 0;
        long t2_avg = 0;
        int ctr =0;
        for(int i = 0; i<10; ++i) {
            for(int k = 0; k<1000; ++k) {
                if(Math.random()<0.5) {
                    t1_avg += test_1();
                    t2_avg += test_2();
                } else {
                    t2_avg += test_2();
                    t1_avg += test_1();
                }
            }
            if(t1_avg < t2_avg) {
                ++ctr;
            }
        }
        System.out.printf("%f%n", ctr*10f);
    }
}
