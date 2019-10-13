
import java.util.ArrayList;
import java.util.Collections;

public class shuffler {
    
    public static long shuffle_me(int k, int N) {
        long T = System.nanoTime();
        int[] aux = new int[N];
        boolean[] v = new boolean[N];
        int ctr = 0;
        
        while(ctr < k) {
            int idx = (int) (N*Math.random());
            if(v[idx]) {
                continue;
            }
            aux[ctr++] = idx;
        }
        
        return System.nanoTime()-T;
    }
    
    public static long shuffle_java(int k, int N) {

        long T = System.nanoTime();
        ArrayList<Integer> list = new ArrayList<>();
        
        for(int i = 0; i<N; ++i) {
            list.add(i);
        }
        
        Collections.shuffle(list);
        return System.nanoTime()-T;
    }
    
    public static void main(String[] args) {
        long T_me = 0;
        long T_java = 0;
        
        for(int i = 0; i<10; ++i) {
            if(Math.random()<0.5) {
                T_me += shuffle_me(10, 1_000_000);
                T_java += shuffle_java(10, 1_000_000);
            } else {
                T_java = shuffle_java(10, 1_000_000);
                T_me = shuffle_me(10, 1_000_000);
            }
        }
        
        System.out.printf("1m total mine: %d, 1m total java: %d%n", 
                T_me, T_java);
    }
}
