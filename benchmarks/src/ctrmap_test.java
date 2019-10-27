
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
        HashMap<String, Integer> hmap = new HashMap<>(50_000);

        for(String s: strings) {
            int num = r.nextInt()&0x7fff_ffff;
            cm.put(s, num);
            hmap.put(s, num);
            
            if(cm.get(s) != hmap.get(s)) {
                int c = cm.get(s);
                int h = hmap.get(s);
            }
        }
        Runtime rt = Runtime.getRuntime();
        
        int[] index = new int[10_000_000];
        for(int i = 0; i<index.length; ++i) {
            index[i] = r.nextInt(strings.length);
        }
        
        System.gc();
        long T = System.nanoTime();
        long M = rt.freeMemory();
        
        for(int i = 0; i<index.length; ++i) {
            cm.incr_str_reg(strings[index[i]]);
        }
        System.out.println("ctrmap 10m incr: " + (System.nanoTime()-T));
        System.out.println("ctrmap mem usage: " + (M-rt.freeMemory()));
        System.gc();
        
        M = rt.freeMemory();
        T = System.nanoTime();
        for(int i = 0; i<index.length; ++i) {
            hmap.put(strings[index[i]], hmap.get(strings[index[i]])+1);
        }
        
        System.out.println("hashmap 10m incr: " + (System.nanoTime()-T));
        System.out.println("hashmap mem usage: " + (M-rt.freeMemory()));
        
        int error = 0;
        
        for(int i = 0; i<strings.length; ++i) {
            if(hmap.get(strings[i]) != cm.get(strings[i])) {
                //System.out.println(hmap.get(strings[i]));
                //System.out.println(cm.get(strings[i])+"\n");
                ++error;
            }
        }
        
        System.out.printf("Error: %f%n", (float) error/strings.length);
    }
}
