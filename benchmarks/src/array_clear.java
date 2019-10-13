
import java.util.Arrays;

public class array_clear {
    
    private int[] array;
    
    public array_clear() {
        array = new int[100];
    }
    
    public long alg_1() {
        long T = System.nanoTime();
        Arrays.fill(array, 0);
        return System.nanoTime()-T;
    }
    
    public long alg_2() {
        long T = System.nanoTime();
        array = new int[100];
        return System.nanoTime()-T;
    }
    
    public static void main(String[] args) {
        array_clear ac = new array_clear();
        long T1 = 0; long T2 = 0;
        for(int i = 0; i<100_000; ++i) {
            if(Math.random()<0.5) {
                T1 += ac.alg_1();
                T2 += ac.alg_2();
            } else {
                T2 += ac.alg_2();
                T1 += ac.alg_1();
            }
        }
        System.out.printf("allocate 10k array 1k times: %d%n", T2);
        System.out.printf("clear 10k arrays 1k times: %d%n", T1);
    }
}
