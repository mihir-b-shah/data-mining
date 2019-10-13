
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;

/*
problem: if i have N sorted sets with items E[0,1000)
find true/false intersection across all

*/
public class set_intersect {

    public static long intersect_2(BitSet[] vect) {
        long T = System.nanoTime();
        BitSet aggr = (BitSet) vect[0].clone();
       
        for(int i = 1; i<vect.length; ++i) {
            aggr.and(vect[i]);
        }
        
        boolean ret = aggr.isEmpty();
        return System.nanoTime()-T;
    }
    
    public static long intersect_1(ArrayList<Integer>[] vect) {
        long T = System.nanoTime();
        HashMap<Integer, Integer> vals = new HashMap<>();
        outer: for(ArrayList<Integer> vi: vect)
            for(int i: vi)
                if((vals.get(i)==null?0:vals.get(i)) == vect.length-1) {
                    break outer;
                } else {
                    vals.put(i, (vals.get(i)==null?0:vals.get(i))+1);
                }
        return System.nanoTime()-T;
    }
    
    public static void main(String[] args) {
        long aggr_1 = 0;
        long aggr_2 = 0;
        for(int k =0; k<100; ++k) {
            ArrayList<Integer>[] vect = new ArrayList[100_000];
            BitSet[] vect_2 = new BitSet[100_000];
            int min_max = 1_000_000_000; int max_min = 0;
            for(int i = 0; i<vect.length; ++i) {
                vect[i] = new ArrayList<>();
                vect_2[i] = new BitSet();
                int min = 1_000_000_000; int max = 0;
                int lim = 2;//(int) (20*Math.random());
                for(int j = 0; j<lim; ++j) {
                    int val = (int) (1000*Math.random());
                    min = Math.min(min, val);
                    max = Math.max(max, val);
                    vect[i].add(val);
                    vect_2[i].set(val);
                }
                Collections.sort(vect[i]);
                if(max-min > max_min-min_max) {
                    max_min = min;
                    min_max = max;
                }
            }
            aggr_1 += intersect_1(vect);
            aggr_2 += intersect_2(vect_2);
        }
        System.out.println("Hashmap: " + aggr_1);
        System.out.println("Bitset: " + aggr_2);
    }
}
