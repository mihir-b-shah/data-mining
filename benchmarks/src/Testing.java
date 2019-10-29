
public class Testing {

    public static void main(String[] args) {
        //test1();
        //test2();
        test2();
    }
    
    public static long test1() {
        double aggregate = 0;
        String str = "mycatisbig";
        aggregate += (double) str.hashCode()*20_000;
        double sum = 0;
        for(int i = 0; i<10_000; ++i) {
            double n = Math.random()*Math.random();
            aggregate += n;
            sum += n;
        }
        double v = aggregate%20_000;
        System.out.println("Value: " + (int) v);
        System.out.println("Hashcode: " + Math.round(aggregate/20_000));
        return 0L;
    }
    
    public static void test2() {
        int mask_1 = Float.floatToIntBits(3.375f);
        int mask_2 = Float.floatToIntBits(6.625f);
        disp(mask_1);
        disp(mask_2);
        disp(Float.floatToIntBits(3.375f+6.625f));
    }
    
    public static void disp(int mask_1) {  
        System.out.printf("%32s%n", Integer.toBinaryString(mask_1));
        int exp = (mask_1 >> 23) & 0xff;
        int sgn = (mask_1 >> 31);
        int val = mask_1 & 0x7_ffff;
        System.out.printf("%01x %02x %06x%n", sgn, exp, val);
    }
}
    