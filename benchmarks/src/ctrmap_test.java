
import java.util.HashMap;

public class ctrmap_test {
    
    public static void main(String[] args) {
        
        String[] strings = new String[50_000];
        
        for(int i = 0; i<strings.length; ++i) {
            char[] buf = new char[5];
            for(int j = 0; j<buf.length; ++j) {
                buf[j] = (char) (65 + 26*Math.random());
            }
            strings[i] = new String(buf);
        }
        
        ctr_map<String> cm = new ctr_map(50_000);
        HashMap<String, Integer> hmap = new HashMap<>(50_000);

        long T1 = System.nanoTime();
        
        for(int i = 0; i<50_000; ++i) {
            hmap.put(strings[i], i);
        }
        
        System.out.println("HASHMAP 50k inserts: " + (System.nanoTime()-T1));
                
        T1 = System.nanoTime();
        for(int i = 0; i<50_000; ++i) {
            cm.put(strings[i], i);
        }
        System.out.println("CTR_MAP 50k inserts: " + (System.nanoTime()-T1));
    }
}
