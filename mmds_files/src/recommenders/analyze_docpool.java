package recommenders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class analyze_docpool {

    // im assuming im not going to need bloom filters
    // it seems too extra
    private static class int_wrapper {

        private int data;

        public int_wrapper() {
            data = 1;
        }

        public int get_data() {
            return data;
        }

        public void incr_data() {
            ++data;
        }
    }

    private static class flt_wrapper {

        private float data;

        public flt_wrapper() {
            data = 1;
        }

        public float get_data() {
            return data;
        }

        public void incr_data() {
            ++data;
        }

        public void set_data(float f) {
            data = f;
        }
    }

    private static final class word implements Comparable<word> {

        private final String wrd;
        private final float val;

        public word(String w, float v) {
            wrd = w;
            val = v;
        }

        public String get_word() {
            return wrd;
        }

        public float get_val() {
            return val;
        }

        @Override
        public int compareTo(word o) {
            return Float.compare(o.get_val(), val);
        }
    }

    // implements tf-idf 
    public static void main(String[] args) throws Exception {
        Scanner f = new Scanner(System.in);
        System.out.println("Enter directory: ");
        String dir = f.nextLine();
        
        System.out.println("Enter the number of words per signature: ");
        int M = f.nextInt();
        f.nextLine();

        File[] files = new File(dir).listFiles();
        System.out.println(files.length);
        // for the whole database
        HashMap<String, int_wrapper> map_dir = new HashMap<>();
        HashMap<String, Float>[] tf = new HashMap[files.length];
        HashMap<String, flt_wrapper> idf = new HashMap<>();
        String[][] signatures = new String[files.length][M];

        for (int i = 0; i < files.length; ++i) {
            tf[i] = new HashMap<>();
        }

        int ctr = 0;

        for (File file : files) {
            HashMap<String, int_wrapper> map_file = new HashMap<>();
            HashSet<String> bitvect = new HashSet<>();
            PriorityQueue<word> pq = new PriorityQueue<>();

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringTokenizer st;
            while ((line = br.readLine()) != null) {
                st = new StringTokenizer(line);
                String word;
                while (st.hasMoreTokens()) {
                    word = st.nextToken();

                    if (!bitvect.contains(word)) {
                        if (idf.containsKey(word)) {
                            idf.get(word).incr_data();
                        } else {
                            idf.put(word, new flt_wrapper());
                        }
                        bitvect.add(word);
                    }

                    int_wrapper iw;
                    if ((iw = map_dir.get(word)) != null) {
                        iw.incr_data();
                    } else {
                        map_dir.put(word, new int_wrapper());
                    }
                    if ((iw = map_file.get(word)) != null) {
                        iw.incr_data();
                    } else {
                        map_file.put(word, new int_wrapper());
                    }
                }

                Set<Map.Entry<String, int_wrapper>> entryset
                        = map_file.entrySet();

                for (Map.Entry<String, int_wrapper> si : entryset) {
                    tf[ctr].put(si.getKey(), (float) si.getValue().get_data()
                            / map_dir.get(si.getKey()).get_data());
                }

                for (String s : bitvect) {
                    pq.offer(new word(s, tf[ctr].get(s)
                            * idf.get(s).get_data()));
                }

                int size = pq.size();
                for (int i = 0; i < Math.min(signatures[0].length, size); ++i) {
                    signatures[ctr][i] = pq.poll().get_word();
                }
            }

            ++ctr;
        }

        Set<Map.Entry<String, flt_wrapper>> entryset
                = idf.entrySet();

        float log = (float) Math.log(files.length);

        for (Map.Entry<String, flt_wrapper> idfv : entryset) {
            idfv.getValue().set_data((float) (log - Math.log(
                    idfv.getValue().get_data())));
        }

        for(String[] sgn: signatures) {
            System.out.println(Arrays.toString(sgn));
        }
        
        f.close();

    }
}
