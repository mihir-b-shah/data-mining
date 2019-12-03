
// float mod vs double mod vs float div/manual

public class moddiv {
    public static void main(String[] args) {
        long time_1 = System.nanoTime();
        for(int i = 0; i<1_000_000; ++i) {
            float val = (float) (1_000_000*Math.random());
            float rem = 0;
            val %= 13.77f;
            val %= 7.49f;
            val %= 3.21f;
            val %= 1.24f;
            val %= 13.77f;
            val %= 7.49f;
            val %= 3.21f;
            val %= 1.24f;
            val %= 13.77f;
            val %= 7.49f;
            val %= 3.21f;
            val %= 1.24f;
            val %= 13.77f;
            val %= 7.49f;
            val %= 3.21f;
            val %= 1.24f;
        }
        System.out.println(System.nanoTime()-time_1);
        long time_2 = System.nanoTime();
        for(int i = 0; i<1_000_000; ++i) {
            float val = (float) (1_000_000*Math.random());
            val /= 13.77f;
            val /= 7.49f;
            val /= 3.21f;
            val /= 1.24f;
            val /= 13.77f;
            val /= 7.49f;
            val /= 3.21f;
            val /= 1.24f;
            val /= 13.77f;
            val /= 7.49f;
            val /= 3.21f;
            val /= 1.24f;
            val /= 13.77f;
            val /= 7.49f;
            val /= 3.21f;
            val /= 1.24f;
        }
        System.out.println(System.nanoTime()-time_2);
    }
}
