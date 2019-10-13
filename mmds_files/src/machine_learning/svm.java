
package machine_learning;

import java.util.function.BiFunction;
import machine_learning.gen_gd.opt_args;

public class svm {
    public static void main(String[] args) {
        gen_gd gd = new gen_gd();
        BiFunction<opt_args,opt_args,Void> gradient = (input, fill) -> {
            float[] vect = input.get_vects()[0];
            
            
            return null;
        };
        gd.gd(1, 1, 4, gradient);
    }
}
