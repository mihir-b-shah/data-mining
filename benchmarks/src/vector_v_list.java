
public class vector_v_list {

    public static void main(String[] args) {
        vector_copy v = new vector_copy();
        link_list LL = new link_list();
        int to_add;

        long vect_T = 0;
        long list_T = 0;
        
        // list performance
        long T = System.nanoTime();

        for(int i = 0; i<1_000; ++i) {
            to_add = (int) (1_000_000*Math.random());
            LL.add(to_add);
        }

        // vector performance
        list_T += System.nanoTime()-T;
        T = System.nanoTime();
        for(int i = 0; i<1_000; ++i) {
            to_add = (int) (1_000_000*Math.random());
            v.add(to_add);
        }

        vect_T += System.nanoTime()-T;

        System.out.printf("vect 1k appends: %d%n", vect_T);
        System.out.printf("list 1k appends: %d%n", list_T);
        
        vect_T = 0;
        list_T = 0;

        // list performance
        T = System.nanoTime();

        for(int i = 0; i<1000; ++i) {
            to_add = (int) (1_000_000*Math.random());
            LL.add_at(to_add);
            LL.move_ptr();
        }

        list_T += System.nanoTime()-T;
        T = System.nanoTime();
        
        for(int i = 0; i<1000; ++i) {
            to_add = (int) (1_000_000*Math.random());
            v.add(i, to_add);
        }

        vect_T += System.nanoTime()-T;
        
        System.out.printf("vect 1k inserts: %d%n", vect_T);
        System.out.printf("list 1k inserts: %d%n", list_T);
    }
}
