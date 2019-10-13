package frequent_itemsets;

import java.util.ArrayList;

public class hashset {

    private ArrayList<tuple>[] table;
    private ArrayList<tuple> iterable;
    private int size;

    public hashset(int N) {
        table = new ArrayList[N];
        iterable = new ArrayList<>();
        size = 0;
        for (int i = 0; i < N; ++i) {
            table[i] = new ArrayList();
        }
    }

    public boolean contains(double hash) {
        ArrayList<tuple> v = table[tuple.conv_fi(hash, table.length)];
        for (tuple i : v) {
            if (Double.compare(i.actual_hash(), hash) == 0) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }
    
    public ArrayList<tuple> iterate() {
        return iterable;
    }
    
    public boolean contains(tuple e) {
        return contains(e.actual_hash());
    }

    public void add(tuple e) {
        iterable.add(e);
        if (size > table.length >> 1) {
            ArrayList<tuple>[] aux = new ArrayList[size << 1];
            for (int i = 0; i < aux.length; ++i) {
                aux[i] = new ArrayList<>();
            }
            for (ArrayList<tuple> ae : table) {
                for (tuple e1 : ae) {
                    aux[tuple.conv_fi(e.actual_hash(), aux.length)].add(e1);
                }
            }
            table = aux;
        }
        table[tuple.conv_fi(e.actual_hash(), table.length)].add(e);
        ++size;
    }
}
