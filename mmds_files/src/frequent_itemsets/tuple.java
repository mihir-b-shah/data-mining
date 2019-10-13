package frequent_itemsets;

import java.util.Arrays;

public class tuple {

    private final int[] data;
    private final double hash;

    public tuple(int val) {
        data = new int[1];
        data[0] = val;
        hash = val;
    }

    // for the pcy implementation
    public tuple(int a, int b) {
        data = new int[2];
        data[0] = a;
        data[1] = b;
        hash = pcy_alg.hash(a, b);
    }
    
    public int get_size() {
        return data.length;
    }

    public int[] get_data() {
        return data;
    }

    public int get_at(int i) {
        return data[i];
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }

    public tuple(tuple it_1, tuple it_2) {
        data = new int[it_1.get_size() + it_2.get_size()];
        System.arraycopy(it_1.get_data(), 0, data, 0, it_1.get_size());
        System.arraycopy(it_2.get_data(), 0, data,
                it_1.get_size(), it_2.get_size());
        hash = it_1.actual_hash() * it_2.actual_hash();
    }

    public double actual_hash() {
        return hash;
    }

    public static int conv_fi(double hash, int N) {
        return (int) hash % N;
    }
}
