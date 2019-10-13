package graph_clustering;

/**
 * The gradient algorithm:
 * 
 * 1. Iterate the matrix to get aggr_vect.
 * 2. 
 */

import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/* try to compact large amounts of zeros
   backed by nested sparse_vects */
public class sparse_matrix {

    private final ArrayList<sparse_vect> mat;
    private final sparse_vect aggr_vect;
    private final sparse_vect curr_vect;
    private final HashMap<Integer, float_wrap> aggr_map;
    private final TreeMap<Integer, float_wrap> aggr_cmap;

    public static class float_wrap {

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

    public sparse_matrix(int N) {
        mat = new ArrayList<>();
        aggr_vect = new sparse_vect();
        curr_vect = new sparse_vect();
        aggr_map = new HashMap<>();
        aggr_cmap = new TreeMap<>();
        for (int i = 0; i < N; ++i) {
            mat.add(new sparse_vect());
        }
    }

    // store a probability and an index
    public void add(int i, long j, float F_val) {
        mat.get(i).add((j << sparse_vect.SHIFT) + Float.floatToIntBits(F_val));
    }

    public float get_F(int i, int idx) {
        return mat.get(i).get_F(idx);
    }
    
    public sparse_vect get_row(int r) {
        return mat.get(r);
    }
    
    public float[] calc_gradient(int r) {
        float[] gradient = new float[1000];//curr_vect.length];
        //Arrays.fill(curr_vect, 0f);
        //Arrays.fill(aggr_vect, 0f);
        
        for (int i = 1; i < mat.get(r).get_size(); ++i) {
            grad_inner(r, get_idx(r, i));
        }
        
        Set<Entry<Integer, float_wrap>> e_set = aggr_map.entrySet();
        Iterator<Map.Entry<Integer, float_wrap>> iter = e_set.iterator();
        Map.Entry<Integer, float_wrap> e;

        while (iter.hasNext()) {
            e = iter.next();
            gradient[e.getKey()] = e.getValue().get_val();
            iter.remove();
        }
        
        do_aggr();
        // find aggr - curr - current
        
        //for(int i = 0; i<curr_vect.length; ++i) {
        //    gradient[i] -= aggr_vect[i] - curr_vect[i];
        //}
        
        sparse_vect sv = mat.get(r);
        for(int i = 0; i<sv.get_size(); ++i) {
            gradient[sv.get_idx(i)] -= sv.get_F(i);
        }
        
        return gradient;
    }
    
    public void sort_init() {
        for (sparse_vect sv : mat) {
            sv.sort();
        }
    }

    public void grad_inner(int r, int t) {
        float v = (float) exp(-dot_prod(r, t));
        v /= 1 - v;
        sparse_vect vect = mat.get(r);
        Integer index;
        for (int i = 0; i < vect.get_size(); ++i) {
            index = vect.get_idx(i);
            if (aggr_cmap.containsKey(index)) {
                aggr_cmap.get(index).incr_val(vect.get_F(i));
            } else {
                aggr_cmap.put(index, new float_wrap(vect.get_F(i)));
            }
            if (aggr_map.containsKey(index)) {
                aggr_map.get(index).incr_val(v * vect.get_F(i));
            } else {
                aggr_map.put(index, new float_wrap(v * vect.get_F(i)));
            }
        }
    }

    public float dot_prod(int r1, int r2) {
        float val = 0;
        sparse_vect v1 = mat.get(r1);
        sparse_vect v2 = mat.get(r2);
        int p1 = 0;
        int p2 = 0;
        int i_1, i_2;
        while (p1 < v1.get_size() && p2 < v2.get_size()) {
            i_1 = v1.get_idx(p1);
            i_2 = v2.get_idx(p2);
            if (i_1 == i_2) {
                val += v1.get_F(p1) * v2.get_F(p1);
                ++p1;
                ++p2;
            } else if (i_1 > i_2) {
                ++p2;
            } else {
                ++p1;
            }
        }
        return val;
    }
    
    public void do_aggr() {
        sparse_vect total = new sparse_vect();
        Set<Entry<Integer, float_wrap>> e_set = aggr_cmap.entrySet();
        Iterator<Map.Entry<Integer, float_wrap>> iter = e_set.iterator();
        Map.Entry<Integer, float_wrap> e;

        while (iter.hasNext()) {
            e = iter.next();
            total.add((e.getKey() << sparse_vect.SHIFT) + 
                    Float.floatToIntBits(e.getValue().get_val()));
            iter.remove();
        }
        for(sparse_vect sv: mat) {
            for(int i = 0; i<sv.get_size(); ++i) {
                //aggr_vect[sv.get_idx(i)] += sv.get_F(i);
            }
        }
    }

    public int get_idx(int i, int idx) {
        return mat.get(i).get_idx(idx);
    }

    public int get_size(int i) {
        return mat.get(i).get_size();
    }
    
    public int get_size() {
        return mat.size();
    }
}
