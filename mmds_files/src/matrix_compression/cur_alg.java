
package matrix_compression;

public class cur_alg {
    
    private final buf_float prob_map;
    private final float[][] inp_mat;
    private final int[] C;
    private final int[] R;
    private final float[] U;
    private final float[] prob_dist; 
    
    /**
     * @param k the k-rank approximation
     * @param inp_mat the original matrix
     */
    public cur_alg(int k, float[][] inp_mat) {
        this.inp_mat = inp_mat;
        C = new int[k << 2];
        R = new int[k << 2];
        U = new float[k];
        prob_dist = new float[inp_mat[0].length];
        prob_map = new buf_float();
    }
    
    // traverse in cache friendly order
    public void popul_dist() {
        float cache;
        float total = 0;
        for(int i = 0; i<inp_mat.length; ++i) {
            for(int j = 0; j<inp_mat[0].length; ++j) {
                prob_dist[j] += (cache = inp_mat[i][j]*inp_mat[i][j]);
                total += cache;
            }
        }
        
        prob_map.push(0f);
        for(int i = 0; i<prob_dist.length; ++i) {
            prob_dist[i] /= total;
            prob_map.push(prob_map.peek()+prob_dist[i]);
        }
    }
    
    public void sample(boolean c) {
        final boolean[] set = new boolean[inp_mat[0].length];
        int val;
        final int size = c ? C.length : R.length;
        for(int i = 0; i<size;) {
            val = prob_map.get_range((float) Math.random());
            if(!set[val]) {
                if(c) {
                    C[i++] = val;
                } else {
                    R[i++] = val;
                }
                set[val] = true;
            }
        }
    }
    
    public void cur() {
        sample(true);
        sample(false);
        sv_decomp svd = new sv_decomp(U.length, inp_mat);
        // compute the pseudoinverse 
        svd.svd();
        float[][] mat = svd.get_U();
        for(int i = 0; i<mat.length; ++i) {
            U[i] = mat[i][i];
        }
    }
}
