
package matrix_compression;

// this is for matrices where Q and Pt fit in main memory
// we're not using sparse matrices
public class stoch_gradient {
    
    private final float lambda;
    private final float[][] R;
    private final float[][] Q;
    private final float[][] Pt;
    private static final float INCR = 1e-2f;
    
    public stoch_gradient(float lambda, float[][] R, float[][] Q, float[][] Pt) {
        this.lambda = lambda;
        this.R = R;
        this.Q = Q;
        this.Pt = Pt;
    }
    
    public static float[][] matmult(float[][] m1, float[][] m2) {
        float[][] ret = new float[m1.length][m2[0].length];
        for(int i = 0; i<ret.length; ++i) {
            for(int j = 0; j<ret[0].length; ++j) {
                float sum = 0;
                for(int k = 0; k<m1[0].length; ++k) {
                    sum += m1[i][k]*m2[k][j];
                }
                ret[i][j] = sum;
            }
        }
        return ret;
    }
    
    public float[][] get_Q() {
        return Q;
    }
    
    public float[][] get_Pt() {
        return Pt;
    }
    
    /**
     * @param prev mxn matrix
     * @param curr same dimensions, mxn matrix
     * @return whether converged
     */
    public boolean convg(float[][] prev, float[][] curr) {
        for(int i = 0; i<prev.length; ++i) {
            for(int j = 0; j<prev[0].length; ++j) {
                if(Math.abs(prev[i][j]-curr[i][j]) > 1e-3f) {
                    return false;
                }
            }
        }
        
        return true;
    }

    private final float dot(float[] one, float[] two) {
        float sum = 0;
        for(int i = 0; i<one.length; ++i) {
            sum += one[i]*two[i];
        }
        return sum;
    }
    
    public void gradient_desc() {
        float[][] grad_Q = new float[Q.length][Q[0].length];
        float[][] grad_P = new float[Pt[0].length][Pt.length];
        float[][] prev_Q,prev_P;
        
        do {
            prev_Q = grad_Q;
            grad_Q = new float[Q.length][Q[0].length];
            for(int i = 0; i<Q.length; ++i) {
                for(int j = 0; j<Q[0].length; ++j) {
                    for(int k = 0; k<R.length; ++k) {
                        grad_Q[i][j] += -2*Pt[j][k]*(R[k][i]-dot(Q[i], Pt[j]))
                                + 2*lambda*Q[i][j];
                    }
                    Q[i][j] += grad_Q[i][j]*INCR;
                }
            }
        } while(!convg(prev_Q, grad_Q));
        
        do {
            prev_P = grad_P;
            grad_P = new float[Pt.length][Pt[0].length];
            for(int i = 0; i<Pt.length; ++i) {
                for(int j = 0; j<Pt[0].length; ++j) {
                    for(int k = 0; k<R.length; ++k) {
                        grad_P[i][j] += -2*Pt[j][k]*(R[k][i]-dot(Q[i], Pt[j]))
                                + 2*lambda*Pt[j][i];
                    }
                    Pt[i][j] += grad_P[i][j]*INCR;
                }
            }
        } while(!convg(prev_P, grad_P));
    }
    
    public static void main(String[] args) {
        float[][] util_mat = new float[1_000][1_000];
        for(int i = 0; i<util_mat.length; ++i) {
            for(int j = 0; j<util_mat[0].length; ++j) {
                util_mat[i][j] = 10_000 *(float) Math.random();
            }
        }
        
        sv_decomp svd = new sv_decomp(4, util_mat);
        svd.svd();
        // initial vals using svd
        float[][] Q = svd.get_U();
        float[][] Pt = matmult(svd.get_S(), svd.get_V());
        stoch_gradient sg = new stoch_gradient(0.4f, util_mat, Q, Pt);
               
        // now optimize using stochastic gradient descent
        sg.gradient_desc();
    }
}
