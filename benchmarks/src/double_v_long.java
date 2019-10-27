
public class double_v_long {
        
    private static final long HCODE_SHIFT = 10_000_000_000L;
    private static final long CT = 100_000;
    
    public static void main(String[] args) {
        
        /*
        String s = "aJCkDl";
        float aggr = (float) Math.random()*100_000;
        int ct = (int) (Math.random()*16_000);
        
        double val = 0;
        val += s.hashCode()*HCODE_SHIFT;
        val += ct*CT;
        val += aggr;
        
        long lval = 0;
        lval += Float.floatToRawIntBits(aggr);
        lval += (long) s.hashCode() << 32; */
        float f = 5.4f;
        System.out.println(Integer.toBinaryString(Float.floatToRawIntBits(f)));
    }
}
