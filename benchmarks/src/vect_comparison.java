
import java.util.ArrayList;

public class vect_comparison {
    public static void main(String[] args) {
        long T1 = System.nanoTime();
        for(int k= 0; k<10; ++k) {
            vector_copy v = new vector_copy();
            for(int i = 0; i<1000; ++i)
                v.add(i);
            for(int i = 0; i<1_000_000; ++i) {
                int idx = (int) Math.random()*v.get_size();
                v.get_at(idx);
            }
        }
        T1 = System.nanoTime()-T1;
        long T2 = System.nanoTime();
        for(int k= 0; k<10; ++k) {
            ArrayList<Integer> a = new ArrayList<>();
            for(int i = 0; i<1000; ++i)
                a.add(i);
            for(int i = 0; i<1_000_000; ++i) {
                int idx = (int) (Math.random()*a.size());
                a.get(idx);
            }
        }
        T2 = System.nanoTime()-T2;
        System.out.println("vect: " + T1/10);
        System.out.println("arraylist: " + T2/10);
    }
}
