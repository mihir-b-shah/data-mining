
package machine_learning;

import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * 
 * This is a generalized implementation of gradient descent
 * so I dont have to re-implement it for different data input types. 
 * Will implement SGD later
 * 
 * @author mihir
 */

public class gen_gd {
    
    private static final float INCR = 0.0001f;
    private static final float TOL = 0.001f;
    
    public class opt_args {
        private final float[] scls;
        private final float[][] vects;
        private final float[][][] matxs;
        
        public opt_args(int s) {
            scls = new float[s];
            vects = null;
            matxs = null;
        }
        
        public opt_args(int s, int v, int dv) {
            scls = new float[s];
            vects = new float[v][dv];
            matxs = null;
        }
        
        public opt_args(int s, int v, int dv, int m, int dm1, int dm2) {
            scls = new float[s];
            vects = new float[v][dv];
            matxs = new float[m][dm1][dm2];
        }
        
        public opt_args init() {
            opt_args ret = new opt_args(scls.length, vects.length, 
                    vects[0].length, matxs.length, 
                    matxs[0].length, matxs[0][0].length);
            
            float scl_fill = (float) Math.sqrt(1f/scls.length);
            Arrays.fill(scls, scl_fill);
            
            float vect_fill = (float) Math.sqrt(1f/vects.length*vects[0].length);
            for(float[] fvect: vects) {
                Arrays.fill(fvect, vect_fill);
            }
            
            float mat_fill = (float) Math.sqrt(1f/matxs.length*matxs[0].length
                                                *matxs[0][0].length);
            for(float[][] fmat: matxs) {
                for(float[] fvect: fmat) {
                    Arrays.fill(fvect, mat_fill);
                }
            }
            
            return ret;
        }
        
        public void copy(opt_args copy) {
            final float[] copy_scls = copy.get_scls();
            final int size_scls = scls.length;
            System.arraycopy(scls, 0, copy_scls, 0, size_scls);
            
            final float[][] copy_vects = copy.get_vects();
            final int size_vect1 = vects.length;
            final int size_vect2 = vects[0].length;
            
            for(int i = 0; i<size_vect1; ++i) {
                System.arraycopy(vects[i], 0, copy_vects[i], 0, size_vect2);
            }
            
            final float[][][] copy_matxs = copy.get_matxs();
            final int size_matx1 = matxs.length;
            final int size_matx2 = matxs[0].length;
            final int size_matx3 = matxs[0][0].length;
            
            for(int i = 0; i<size_matx1; ++i) {
                for(int j = 0; j<size_matx2; ++j) {
                    System.arraycopy(matxs[i][j], 0, copy_matxs[i][j], 0,
                            size_matx3);
                }
            }
        }
        
        public float[] get_scls() {
            return scls;
        }
        
        public float[][] get_vects() {
            return vects;
        }
        
        public float[][][] get_matxs() {
            return matxs;
        }
        
        public int get_numscls() {
            return scls.length;
        }
        
        public int get_numvects() {
            return vects.length;
        }
        
        public int get_nummats() {
            return matxs.length;
        }
        
        public boolean subt(opt_args subtr) {
            boolean ret = true;
            float[] aux1 = subtr.get_scls();
            final int size_1 = scls.length;
            for(int i = 0; i<size_1; ++i) {
                scls[i] -= aux1[i];
                ret &= Math.abs(aux1[i]) <= TOL;
            }
            float[][] aux2 = subtr.get_vects();
            final int size_2 = vects.length;
            final int size_inner2 = vects[0].length;
            for(int i = 0; i<size_2; ++i) {
                for(int j = 0; j<size_inner2; ++j) {
                    vects[i][j] -= aux2[i][j];
                    ret &= Math.abs(aux2[i][j]) <= TOL;
                }
            }
            float[][][] aux3 = subtr.get_matxs();
            final int size_3 = matxs.length;
            final int size_inner3 = matxs[0].length;
            final int size_innerinner3 = matxs[0][0].length;
            for(int i = 0; i<size_3; ++i) {
                for(int j = 0; j<size_inner3; ++j) {
                    for(int k = 0; k<size_innerinner3; ++k) {
                        matxs[i][j][k] -= aux3[i][j][k];
                        ret &= Math.abs(aux3[i][j][k]) <= TOL;
                    }
                }
            }
            return ret;
        }
    }
    
    public void gd(int s, BiFunction<opt_args,opt_args,Void> gradient) {
        opt_args args = new opt_args(s);
        gd(args, gradient);
    }
    
    public void gd(int s, int v1, int v2, 
            BiFunction<opt_args,opt_args,Void> gradient) {
        opt_args args = new opt_args(s,v1,v2);
        gd(args, gradient);
    }
    
    public void gd(int s, int v1, int v2, int m1, int m2, int m3, 
            BiFunction<opt_args,opt_args,Void> gradient) {
        opt_args args = new opt_args(s,v1,v2,m1,m2,m3);
        gd(args, gradient);
    }
    
    private void gd(opt_args args, 
            BiFunction<opt_args,opt_args,Void> gradient) {
        opt_args aggr = args.init();
        do {
            gradient.apply(aggr,args);
        } while(!aggr.subt(args));
        args.copy(aggr);
    }
}
