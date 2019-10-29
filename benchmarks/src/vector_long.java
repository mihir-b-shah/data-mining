
public class vector_long<T> {

    private long[] array;
    private int pos;
    private static final long FIRST_MASK = 0x7fff_ffff;
    private static final long MASK_OUTER = 0x1_ffff;
    private static final long SHIFT_OUTER = 1 << 17;

    public vector_long() {
        array = new long[2];
    }

    public vector_long(int N) {
        array = new long[N];
    }

    public void add(long e) {
        if (pos == array.length) {
            long[] aux = new long[pos << 1];
            System.arraycopy(array, 0, aux, 0, pos);
            array = aux;
        }
        array[pos++] = e;
    }

    public long[] get_array() {
        return array;
    }

    public long get(int i) {
        return array[i];
    }

    public int get_size() {
        return pos;
    }

    public int find_hash(T o) {
        int hcode = o.hashCode();
        for (int i = 0; i < pos; ++i) {
            if ((int) (array[i] >> 32) == hcode) {
                return (int) (array[i] & FIRST_MASK);
            }
        }
        return -1;
    }

    public void set_hash(T o, int v) {
        int hcode = o.hashCode();
        for (int i = 0; i < pos; ++i) {
            if ((int) (array[i] >> 32) == hcode) {
                String s = Long.toHexString(array[i]);
                array[i] += (v & MASK_OUTER) + (SHIFT_OUTER);
                String t = Long.toHexString(array[i]);
                break;
            }
        }
    }
    
    public void set_hash_reg(T o, int v) {
        int hcode = o.hashCode();
        for (int i = 0; i < pos; ++i) {
            if ((int) (array[i] >> 32) == hcode) {
                array[i] += v & FIRST_MASK;
                break;
            }
        }
    }
}
