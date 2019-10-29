
import java.util.HashMap;
import java.util.Random;

public class ctrmap_test {
    
    public static void main(String[] args) {
        
        String[] strings = new String[50_000];
        Random r = new Random();
        
        for(int i = 0; i<strings.length; ++i) {
            char[] buf = new char[20];
            for(int j = 0; j<buf.length; ++j) {
                buf[j] = (char) (65 + 26*Math.random());
            }
            strings[i] = new String(buf);
        }
        
        ctr_map<String> cm = new ctr_map(50_000);
        HashMap<String, int_wrapper> hmap = new HashMap<>(50_000);

        for(String s: strings) {
            int num = r.nextInt(5);
            cm.put_format(s, num);
            hmap.put(s, new int_wrapper(num));
        }
        Runtime rt = Runtime.getRuntime();
        
        int[] index = new int[1_000];
        for(int i = 0; i<index.length; ++i) {
            index[i] = r.nextInt(strings.length);
        }

        for(int i = 0; i<index.length; ++i) {
            int num = r.nextInt(5);
            cm.incr_str(strings[index[i]], num);
            hmap.get(strings[index[i]]).incr(num);
        }
        
        int error = 0;
        
        for(int i = 0; i<strings.length; ++i) {
            int res = cm.get(strings[i]);
            if(hmap.get(strings[i]).avg() != (float) (res&0x1_ffff)/(res>>17)) {
                System.out.printf("hashmap: %f%n", hmap.get(strings[i]).avg());
                System.out.printf("ctrmap: %f%n", (float) 
                        (res&0x1_ffff)/(res>>17));
                ++error;
            }
        }
        
        System.out.printf("Error: %f%n", (float) error/strings.length);
    }
}
