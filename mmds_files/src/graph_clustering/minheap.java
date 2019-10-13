package graph_clustering;

// cache optimized priority queue
public class minheap {

    // the sparsevect is a long vector internally
    // so were using it for that
    private final sparse_vect array;

    public minheap() {
        array = new sparse_vect();
    }

    public void add(long s) {
        array.add(s);
        rebalance();
    }

    public void rebalance() {
        int pos = array.get_size()-1;
        long temp;
        int sh = pos >> 1;
        while (pos > 0 && array.get(sh) > array.get(pos)) {
            temp = array.get(sh);
            array.set(sh, array.get(pos));
            array.set(pos, temp);
            pos = sh;
            sh >>= 1;
        }
    }
}
