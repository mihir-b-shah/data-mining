/**
 * This is naive version of apriori algorithm.
 */
package frequent_itemsets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

public class apriori_alg {

    private final HashSet<vector> baskets;
    private final BitSet[] freq_set;
    private final int[] freq_ct;

    public apriori_alg() throws Exception {
        baskets = new HashSet<>();
        freq_set = new BitSet[1000];
        freq_ct = new int[1000];

        for (int i = 0; i < 1000; ++i) {
            freq_set[i] = new BitSet(100);
        }

        BufferedReader br = new BufferedReader(
                new FileReader("freq_item.txt"));

        String line;
        int ptr, size;
        short val;
        vector vect;
        while ((line = br.readLine()) != null) {
            val = 0;
            vect = new vector();
            ptr = 0;
            size = line.length();
            while (ptr < size) {
                if (line.charAt(ptr) == ' ') {
                    vect.add(val);
                    if (!freq_set[val].get(baskets.size())) {
                        ++freq_ct[val];
                    }
                    freq_set[val].set(baskets.size());
                    val = 0;
                } else {
                    val *= 10;
                    val += line.charAt(ptr) - '0';
                }
                ++ptr;
            }
            baskets.add(vect);
        }
        br.close();
        if (baskets.size() > 10_000_000) {
            throw new Exception("Data set too big!");
        }
    }

    public ArrayList<hashset> get_tuples(int k, double s) {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < 1000; ++i) {
            map.put(freq_ct[i], i);
        }

        Iterator<Integer> iter = map.descendingKeySet().iterator();

        double val;
        ArrayList<hashset> items = new ArrayList<>();
        items.add(new hashset(10000));

        // its hashing to the same value i think
        while (iter.hasNext()
                && Double.compare(
                        (val = iter.next()) / baskets.size(), s) > 0) {
            items.get(0).add(new tuple(map.get((int) val)));
        }

        tuple_helper(k, items);
        return items;
    }

    public boolean check(ArrayList<hashset> items, int k, tuple e) {
        double hash = e.actual_hash();

        
        if (items.get(k).contains(e)) {
            return false;
        }
        
        boolean aggr = true;
        for (int i = 0; i < e.get_size(); ++i) {
            aggr &= items.get(k - 1).contains(hash / e.get_at(i));
            if(!aggr) {
                return false;
            }
        }
        // now actually check
        BitSet b = freq_set[e.get_at(0)];
        b = (BitSet) b.clone();
        for (int i = 1; i < e.get_size(); ++i) {
            b.and(freq_set[e.get_at(i)]);
        }
 
        return !b.isEmpty();
    }

    // bottom up
    public void tuple_helper(int k_out, ArrayList<hashset> items) {
        k_out -= 2;
        int k = 1;
        do {
            items.add(new hashset(10000));
            tuple e;
            ArrayList<tuple> tp_outer = items.get(0).iterate();
            ArrayList<tuple> tp_inner = items.get(k - 1).iterate();
            for (tuple i : tp_outer) {
                for (tuple j : tp_inner) {
                    e = new tuple(i, j);
                    if (check(items, k, e)) {
                        items.get(k).add(e);
                    }
                }
            }
        } while (k < k_out && items.get(k - 1).size() < items.get(k++).size());
    }

    public HashSet<vector> get_baskets() {
        return baskets;
    }

    public static void main(String[] args) {
        // uses the freq_item.txt dataset
        // i have no idea what the dataset describes
        try {
            apriori_alg alg = new apriori_alg();
            long T = System.nanoTime();
            ArrayList<hashset> items
                    = alg.get_tuples(5, 0.04); // determined empirically
            System.out.println(System.nanoTime()-T);
            int ctr = 2;
            for (hashset t : items) {
                System.out.printf("k=%d, size=%d%n", ctr++, t.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
