

/**
 *
 * @author mihir
 * still ridiculously inefficient
 *  get a bloom filter with associated float
 * 
 */
public class int_wrapper {
    private long val;
    private static final long MASK = 1L<<32;
    private static final long FIRST = 0x7fff_ffff;
    
    public int_wrapper() {
        val = 1;
    }
    
    public int_wrapper(int v) {
        val = (MASK+v);
    }
    
    public void incr(int i) {
        val += (MASK+i);
    }
    
    public int ct_reg() {
        return (int) (val&FIRST);
    }
    
    public void incr_reg() {
        ++val;
    }
    
    public float avg() {
        return ((float) (val&FIRST))/(val>>>32);
    }
}