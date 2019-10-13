
package nearest_neighbors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;

// Optimized with locality-sensitive hashing
// currently doesnt work for large k that well (very bad loss of accuracy)
public class knn {
    
    private final HashMap<Double, Double> pts;
    private final Function<Double,Integer>[] hash_fs; 
    private final loc_set[] maps;
    
    // screw java cant you give me access to the buckets ?!?!? 
    private static class loc_set {
        private final ArrayList<Double>[] set;
        
        public loc_set(int N) {
            set = new ArrayList[N];
            for(int i = 0; i<N; ++i) 
                set[i] = new ArrayList<>();
        }
        
        public void add(int hash, double x) {set[hash].add(x);}
        public ArrayList<Double> get_bucket(int hash) {return set[hash];}
        
        @Override
        public String toString() {
            return Arrays.toString(set);
        }
    }
    
    public knn() {
        gen_data data = new gen_data((x)->x*Math.sin(x));
        pts = data.get_data();
        hash_fs = new Function[5];
        for(int i = 0; i<hash_fs.length; ++i) {
            double r1,r2;
            r1 = Math.random(); r2 = 3*Math.random()+1;
            hash_fs[i] = x->(((int) Math.pow(r1*x, Math.floor(r2)))%1000);
        }
        maps = new loc_set[hash_fs.length];
        for(int i = 0; i<maps.length; ++i)
            maps[i] = new loc_set(1000);
        Set<Double> pt_set = pts.keySet();
        for(double pt: pt_set) 
            for(int j = 0; j<hash_fs.length; ++j)
                maps[j].add(hash_fs[j].apply(pt),pt);
    }
    
    /* Implements a k-nn to predict value 
       Arbitrary number of hash boxes */
    public double calc(int k, double x) {
        PriorityQueue<Double> pq = 
                new PriorityQueue<>((a,b)->((int) (1000*(b-a))));
        for(int i = 0; i<hash_fs.length; ++i) 
            for(int j = -2; j<=2; ++j) {
                int hash = hash_fs[i].apply(x);
                pq.addAll(maps[i].get_bucket(
                        Math.min(Math.max(hash+j,0),1000)));
            }
        double sum = 0;
        int size = pq.size();
        if(size < k) System.err.printf("k = %d used.%n", size);
        for(int i = 0; i<Math.min(k,size); ++i) 
            sum += pts.get(pq.poll());
        return size == 0 ? 0 : sum/Math.min(size, k);
    }
    
    public static void main(String[] args) {
        knn k_nn = new knn();
        for(loc_set set: k_nn.maps)
            System.out.println(set);
        System.out.println(k_nn.calc(1, 300));
    }
}
