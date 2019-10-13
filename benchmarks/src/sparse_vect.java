
import java.util.Arrays;

public class sparse_vect {

    /* stores the probabilities along using IEEE native routines
           inbuilt into the VM. */
    private long[] data;
    private int size;
    public static final int PROB_MASK = 0xffff_ffff;
    public static final long EXC_MASK = 0xffff_ffff_0000_0000L;
    public static final int SHIFT = 0x20;

    public sparse_vect() {
        data = new long[2];
    }

    public sparse_vect(int N) {
        data = new long[N];
    }

    public void add(long s) {
        if (data.length == size) {
            long[] aux = new long[size << 1];
            System.arraycopy(data, 0, aux, 0, size);
            data = aux;
        }
        data[size++] = s;
    }
    
    public void sort() {
        Arrays.sort(data);
    }

    public long get(int i) {
        return data[i];
    }
    
    public void scl(float f) {
        for (int i = 0; i < size; ++i) {
            data[i] = (data[i] & EXC_MASK) + Float.floatToIntBits(
                    f * Float.intBitsToFloat((int) (data[i] & PROB_MASK)));
        }
    }

    @Override
    public sparse_vect clone() {
        sparse_vect sv = new sparse_vect(size);
        for (int i = 0; i < size; ++i) {
            sv.add(data[i]);
        }
        return sv;
    }

    /*
    public int loc(int idx, long val) {
        
    } */
    
    // the legend himself! native code
    public float get_F(int idx) {
        return Float.intBitsToFloat((int) (data[idx] & PROB_MASK));
    }

    public int get_idx(int idx) {
        return (int) (data[idx] >>> SHIFT);
    }

    public void set(int i, long v) {
        data[i] = v;
    }
    
    public int get_size() {
        return size;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(int i = 0; i<size; ++i) {
            sb.append(String.format("<%d, %f>, ", get_idx(i), get_F(i)));
        }
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        sb.append(']');
        return sb.toString();
    }
}
