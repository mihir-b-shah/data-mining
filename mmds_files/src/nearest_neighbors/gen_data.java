
package nearest_neighbors;

import java.util.function.Function;
import static java.lang.Math.*;
import java.util.HashMap;

public class gen_data {
    
    private static HashMap<Double, Double> data;
    
    /* gaussian noise generation
       just uses simple taylor series approx  */
    public gen_data(Function<Double,Double> f) {
        data = new HashMap<>();
        for(int i = 0; i<100; ++i) {
            double x = 2*PI*random();
            data.put(100*x, 100*f.apply(x)+gen_noise());
        }
    }
    
    public double gen_noise() {
        return cdf_norm(random(),0, 25);
    }
    
    public double cdf_norm(double x, double u, double v) {
        double tt1, tt2, lnx, sgn;
        sgn = random()>0.5 ? -1.0 : 1.0;

        x = 1-x*x;
        lnx = log(x);

        tt1 = 2/(PI*0.147) + 0.5f * lnx;
        tt2 = 1/(0.147) * lnx;

        return u+v*sgn*sqrt(-tt1 + sqrt(tt1*tt1 - tt2));
    }

    public HashMap<Double,Double> get_data() {return data;}
    
}
