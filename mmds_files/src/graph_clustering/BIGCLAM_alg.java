
package graph_clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import frequent_itemsets.vector;

public class BIGCLAM_alg {
    
    private final HashMap<Integer,Integer> f_map;
    private final HashMap<Integer,Integer> r_map;
    private final ArrayList<vector> adj_list;
    private final HashMap<Integer, sparse_matrix.float_wrap> aggr_map;
    
    public static final float DF = 0.001f;
    
    public BIGCLAM_alg() throws Exception {
        BufferedReader br = new BufferedReader(
                new FileReader("wiki-vote.txt"));
        f_map = new HashMap<>();
        r_map = new HashMap<>();
        adj_list = new ArrayList<>();
                
        String line;
        int ptr = 0;
        int from = 0;
        int to = 0;
        int ct = 0;
        while((line = br.readLine()) != null) {
            while(line.charAt(ptr) != ' ') {
                from *= 10;
                from += line.charAt(ptr++)-'0';
            }
            while(ptr < line.length()) {
                to *= 10;
                to += line.charAt(ptr++)-'0';
            }
            if(f_map.get(from) != null) {
                f_map.put(from, ct);
                r_map.put(ct++, from);
                adj_list.add(new vector());
            }
            if(f_map.get(to) != null) {
                f_map.put(to, ct);
                r_map.put(ct++, to);
                adj_list.add(new vector());
            }
            
            adj_list.get(from-1).add(to-1);
            adj_list.get(to-1).add(from-1);
        }

        aggr_map = new HashMap<>();
    }
    
    public void grad_ascent(sparse_matrix F) {
        fill_smF(F);
        F.sort_init();
        sparse_vect ref;
        Integer index;
        sparse_vect vect = null;
        for(int i = 0; i<F.get_size(); ++i) {
            ref = F.get_row(i);
            index = ref.get_idx(i);
            //vect = F.calc_gradient(i);
            // increment ref by DF*gradient
            if (aggr_map.containsKey(index)) {
                aggr_map.get(index).incr_val(DF * vect.get_F(i));
            } else {
                aggr_map.put(index, 
                        new sparse_matrix.float_wrap(DF * vect.get_F(i)));
            }
            
            Set<Map.Entry<Integer, sparse_matrix.float_wrap>> e_set = 
                    aggr_map.entrySet();
            Iterator<Map.Entry<Integer, sparse_matrix.float_wrap>> iter = 
                    e_set.iterator();
            Map.Entry<Integer, sparse_matrix.float_wrap> e;

            while (iter.hasNext()) {
                e = iter.next();
                //gradient[e.getKey()] = e.getValue().get_val();
                iter.remove();
            }
        }
    }
    
    public float[] fill_smF(sparse_matrix F) {
        int ctr = 0;
        final int SHIFT = 0x10;
        for(vector v: adj_list) {
            for(int i = 0; i<v.get_size(); ++i) {
                F.add(ctr, v.get_at(i) << SHIFT, 0);
            }
            ++ctr;
        }
        return null;
    }
    
    public int get_size() {
        return f_map.size();
    }
    
    public static void main(String[] args) {
        try {
            BIGCLAM_alg ba = new BIGCLAM_alg();
            sparse_matrix F = new sparse_matrix(ba.get_size());
            ba.grad_ascent(F);
        } catch (Exception e) {
        }
    }
}
