
import java.util.PriorityQueue;


public class pq {

    // the sparsevect is a long vector internally
    // so were using it for that
    private sparse_vect array;

    public pq() {
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
    
    public static long alg1(PriorityQueue<Float> pq) {
        long T = System.nanoTime();

        for(int i = 0; i<100_000; ++i) {
            pq.add(i+(float) Math.random()*0xffff_ffff);
        }

        return System.nanoTime() - T;
    }
    
    public static long alg2(pq pq1) {
        long T = System.nanoTime();

        for(int i = 0; i<100_000; ++i) {
            pq1.add((((long) i) << 0x20) + 
                    Float.floatToIntBits((float) Math.random()));
        }
        
        return System.nanoTime() - T;
    }
    
    public static void main(String[] args) {
        pq pq1 = new pq();
        PriorityQueue<Float> pq = new PriorityQueue<>();
        
        long t1 = 0; long t2 = 0;
        
        for(int k = 0; k<25; ++k) {
            if(Math.random()>0.5) {
                t1 += alg1(pq);
                t2 += alg2(pq1);
            } else {
                t2 += alg2(pq1);
                t1 += alg1(pq);
            }
        }
        
        System.out.printf("Java priorityqueue: %d, my pq: %d%n", t1, t2);
    }
}