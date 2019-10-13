package recommenders;

import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class movie_suggester {

    private static final ArrayList<spvect_avl> matrix;
    
    static {
        matrix = new ArrayList<>();
    }
    
    private static class index_prob implements Comparable<index_prob> {
        private final int idx;
        private final float prob;
        
        public index_prob(int i, float p) {
            idx = i;
            prob = p;
        }
        
        public int get_idx() {
            return idx;
        }
        
        public float get_prob() {
            return prob;
        }
        
        @Override
        public int compareTo(index_prob o) {
            return Float.compare(o.get_prob(), prob);
        }
    }
    
    public static float gen_recs(int idx) {
        PriorityQueue<index_prob> probs = new PriorityQueue<>(matrix.size());
        for(int i = 0; i<matrix.size(); ++i) {
            probs.add(new index_prob(i, 
                    spvect_avl.dot(matrix.get(idx), matrix.get(i))));
        }
        
        final int size = Math.min(probs.size(), 3);
        float avg = 0;
        
        for(int i = 0; i<size; ++i) {
            avg += probs.poll().get_idx();
        }
        
        return avg/size;
    }
    
    public static void main(String[] args) {
        Scanner f = new Scanner(System.in);
        
        HashMap<String, Integer> index_map = new HashMap<>();
        HashMap<String, Integer> mov_map = new HashMap<>();
        int ctr = 0;
        int mov_ctr = 0;
        
        outer:
        do {
            System.out.println("Enter code: 1, add movie/rating; 2, "
                    + "add user; "
                    + "3, view recommendations and exit.\n");
            int code = f.nextInt();
            f.nextLine();

            int val;
            String name;
            String mov_name;

            switch (code) {
                case 1:
                    System.out.println("Enter user name: ");
                    name = f.nextLine();
                    System.out.println("Enter movie name: ");
                    mov_name = f.nextLine();
                    System.out.println("Enter rating (1-5): ");
                    val = f.nextInt();
                    mov_map.put(mov_name, mov_ctr++);
                    matrix.get(index_map.get(name)).add(mov_ctr-1, val);
                    f.nextLine();
                    break;
                case 2:
                    System.out.println("Enter user name: ");
                    name = f.nextLine();
                    index_map.put(name, ctr++);
                    matrix.add(new spvect_avl());
                    break;
                case 3:
                    System.out.println("Enter user name: ");
                    name = f.nextLine();
                    System.out.printf("Recs: %f%n", 
                            gen_recs(index_map.get(name)));
                    break outer;
                default:
                    System.err.println("AM HUGE AND HUNGRY!");
            }

        } while (true);
    }
}
