
package locality_sensitive_hashing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.BitSet;

public class text_compare {
    public static void main(String[] args) {
        try {
            String file_1 = read_file("test1.txt");
            String file_2 = read_file("test2.txt");
            
            BitSet f1_bool = minhash.gen_bitset(file_1);
            BitSet f2_bool = minhash.gen_bitset(file_2);
            
            System.out.println(minhash.jaccard_siml(f1_bool, f2_bool));
        } catch (Exception e) {}
        
    }
    
    public static String read_file(String path) throws Exception {
        StringBuilder file = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(path));
        int ch;
        while((ch = br.read()) != -1)
            file.append((char) ch);
        br.close();
        return file.toString();
    }
}
