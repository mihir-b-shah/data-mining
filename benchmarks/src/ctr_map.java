
public class ctr_map<T> {
    
    private vector_long<T>[] map;
    private int size;
    private static final int incr = 1 << 17;
    
    public ctr_map(int N) {
        map = new vector_long[Math.min(N<<1,100_000)];
    }

    public void incr_str(T k, int v) {
        vector_long vl = map[adjust_index(k.hashCode())];
        if(vl != null) {
            vl.set_hash(k, v);
        }
    }
    
    public void incr_str_reg(T k) {
        vector_long vl = map[adjust_index(k.hashCode())];
        if(vl != null) {
            vl.set_hash_reg(k, 1);
        }
    }
    
    public void put_format(T k, int v) {
        put(k, incr+(v&0x1_ffff));
    }
    
    public void put_ctr(T k) {
        put(k, 1);
    }
    
    public void put(T k, int v) {
        int hcode = k.hashCode();
        int ahcode = adjust_index(hcode);
        vector_long<T> ref = map[ahcode];
        if(ref == null) {
            map[ahcode] = ref = new vector_long();
        }
        ref.add(((long) hcode << 32)+v);
        ++size;
        if(8 + (size >> 1) > map.length) {
            vector_long<T>[] aux = new vector_long[map.length<<1];
            long[] iter;
            long access;
            for(int i = 0; i<map.length; ++i) {
                iter = map[i].get_array();
                for(int j = 0; j<iter.length; ++j) {
                    access = iter[j];
                    aux[(int) ((access >>> 32) % aux.length)].add(access);
                }
            }
            map = aux;
        }
    }
    
    public int get(T o) {
        vector_long<T> vl = map[adjust_index(o.hashCode())];
        if(vl != null) {
            return vl.find_hash(o);
        } else {
            return -1;
        }
    }
    
    public float flt_avg(T o) {
        int res = get(o);
        return (float) (res&0x1_ffff)/(res>>17);
    }
    
    public int adjust_index(int e) {
        int val = e % map.length;
        if(val < 0) {
            return map.length + (val);
        } else {
            return val;
        }
    }
    
    public int get_size() {
        return size;
    }
}
