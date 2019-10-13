
package kmeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class kmeans {
    
    public int[] sample(int k, int N) {
        int[] aux = new int[k];
        if(N > 1_000_000) {
            HashSet<Integer> v = new HashSet<>();
            int ctr = 0;

            while(ctr < k) {
                int idx = (int) (N*Math.random());
                if(v.contains(idx)) {
                    continue;
                }
                aux[ctr++] = idx;
            }
            return aux;
        } else {
            boolean[] v = new boolean[N];
            int ctr = 0;

            while(ctr < k) {
                int idx = (int) (N*Math.random());
                if(v[idx]) {
                    continue;
                }
                aux[ctr++] = idx;
            }
            return aux;
        }
    }
    
    public static void main(String[] args) {
        kmeans km = new kmeans();
        vector_xy vect = new vector_xy();
        
        int size = vect.get_size();
        // 4 is some arbitrary constant
        final int k = 4;
        int[] sampl_idx = km.sample(k, size);
        ArrayList<quadtree>[] clusters = new ArrayList[k];
        HashMap<Integer,Integer> index_map = new HashMap<>();
        
        for(int i = 0; i<clusters.length; ++i) {
            clusters[i] = new ArrayList<>();
        }
                
        quadtree qtree = new quadtree(1f,1f);
        for(int i: sampl_idx) {
            index_map.put(Float.floatToIntBits(vect.get_x(i))^
                    Float.floatToIntBits(vect.get_y(i)), i);
            qtree.add(vect.get_x(i), vect.get_y(i));
        }
        
        for(int i = 0; i<size; ++i) {
            quadtree qt = qtree.nn(vect.get_x(i), vect.get_y(i));
            clusters[index_map.get(Float.floatToIntBits(vect.get_x(i))^
                    Float.floatToIntBits(vect.get_y(i)))].add(qt);
        }
        
        // arbitrarily determined const
        // it should have some tolerances
        // but thats kind of expensive
        final int ITER = 10;
        
        for(int i = 0; i<ITER; ++i) {
            for(int j = 0; j<k; ++j) {
                final ArrayList<quadtree> iter = clusters[j];
                float xsum = 0, ysum = 0;
                for(quadtree qt: iter) {
                    xsum += qt.get_x();
                    ysum += qt.get_y();
                }
                xsum /= iter.size();
                ysum /= iter.size();
                
                index_map.put(Float.floatToIntBits(xsum)^
                        Float.floatToIntBits(ysum), j);
            }
            
            for(int j = 0; j<size; ++j) {
                quadtree qt = qtree.nn(vect.get_x(j), vect.get_y(j));
                clusters[index_map.get(Float.floatToIntBits(vect.get_x(j))^
                        Float.floatToIntBits(vect.get_y(j)))].add(qt);
            }
        }
    }           
}
