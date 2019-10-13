package frequent_itemsets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.TreeMap;

public class pcy_alg {

    private final int[] ct;
    private final int support;
    private final int[] h_table;

    public pcy_alg(int s) throws Exception {
        support = s;
        ct = new int[1_000];
        h_table = new int[1_000_000];
        read();
    }

    public final void read() throws Exception {
        BufferedReader br = new BufferedReader(
                new FileReader("freq_item.txt"));
        String line;
        int ptr, size;
        short val;
        vector v;
        while ((line = br.readLine()) != null) {
            val = 0;
            v = new vector();
            ptr = 0;
            size = line.length();
            while (ptr < size) {
                if (line.charAt(ptr) == ' ') {
                    ++ct[val];
                    v.add(val);
                    val = 0;
                } else {
                    val *= 10;
                    val += line.charAt(ptr) - '0';
                }
                ++ptr;
            }
            for (int i = 0; i < v.get_size(); ++i) {
                for (int j = 0; j < i; ++j) {
                    ++h_table[hash(v.get_at(i), v.get_at(j))];
                }
            }

            BitSet bits = new BitSet();
            for (int i = 0; i < h_table.length; ++i) {
                bits.set(i, h_table[i] >= support);
            }

            TreeMap tm = new TreeMap();
            for (int i = 0; i < ct.length; ++i) {
                tm.put(ct[i], i);
            }
            Iterator<Integer> iter = tm.descendingKeySet().iterator();
            vector vect = new vector();
            ArrayList<tuple> t2 = new ArrayList<>();

            while (iter.hasNext()) {
                if (val > support) {
                    vect.add(val);
                }
            }

            // optimizes most costly part of apriori
            for (int i = 0; i < vect.get_size(); ++i) {
                for (int j = 0; j < i; ++j) {
                    if (bits.get(hash(vect.get_at(i), vect.get_at(j)))) {
                        t2.add(new tuple(vect.get_at(i), vect.get_at(j)));
                    }
                }
            }
        }
        br.close();
    }

    public static int hash(int i, int j) {
        return Math.max(i, j & 0x3F) * Math.min(i, j);
    }

    public static void main(String[] args) {
        try {
            pcy_alg pcy = new pcy_alg(1_000);
        } catch (Exception e) {
        }
    }
}
