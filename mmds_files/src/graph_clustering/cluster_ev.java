package graph_clustering;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class cluster_ev {

    public static void main(String[] args) {
        int M = Integer.parseInt(
                JOptionPane.showInputDialog("Enter size of graph. "
                        + "The inputted graph must be k-regular and even size"));

        int D = Integer.parseInt(
                JOptionPane.showInputDialog("Enter degree of graph."));
        
        JPanel[] panels = new JPanel[M];

        for (int i = 0; i < M; ++i) {
            panels[i] = new JPanel();
        }

        Object[][] fields = new Object[M][M];

        for (int i = 0; i < M; ++i) {
            JPanel p = panels[i];
            for (int j = 0; j < M; ++j) {
                fields[i][j] = new JTextField("", 4);
                p.add((JTextField) fields[i][j]);
            }
        }

        JOptionPane.showMessageDialog(null, panels);

        float[][] coeff = new float[M][M];

        for(int i = 0; i<M; ++i)
            coeff[i][i] -= D;
        
        for (int i = 0; i < M; ++i) {
            for (int j = 0; j < M; ++j) {
                coeff[i][j] = Integer.parseInt(
                        ((JTextField) fields[i][j]).getText());
            }
        }

        /* implement the EVD of the laplacian graph.
           uses raleigh's algorithm */
        float mu = 0.1f;
        float mu_pr;
        float[] v = new float[M];
        float fill_v = (float) Math.sqrt(1f/M);
        
        for(int i = 0; i<v.length; ++i) {
            v[i] = (i&1)==0?fill_v:-fill_v;
        }
        
        float[][] aux;
        
        do {
            aux = invert(gen_smi(coeff, mu));
            v = matvect_mult(aux, v);
            scl(v, mgn_vect(v));
            mu_pr = mu;
            mu = inner_prod(vectmat_mult(v, coeff), v);
        } while(Math.abs(mu-mu_pr)<0.0001);

        StringBuilder cl_A = new StringBuilder();
        StringBuilder cl_B = new StringBuilder();
        
        for(int i = 0; i<v.length; ++i) {
            if(v[i] > 0) {
                cl_B.append(i);
                cl_B.append('\t');
            } else {
                cl_A.append(i);
                cl_A.append('\t');
            }
        }
        
        JOptionPane.showMessageDialog(null, 
                String.format("%s%n%s%n", cl_A, cl_B));
    }
    
    public static float[][] invert(float[][] mat) {
        float[][] aux = new float[mat.length][mat.length<<1];
        for(int i = 0; i<mat.length; ++i) {
            System.arraycopy(mat[i], 0, aux[i], 0, mat.length);
            aux[i][mat.length+i] = 1f;
        }
        
        // first find triangular matrix, 1s on diagonal, 0 on Left
        
        outer: for(int i = 0; i<aux.length; ++i) {
            for(int j = 0; j<i; ++j) {
                int pos = i-1;
                if(aux[i][j] != 0d) {
                    while(aux[pos][j] == 0) {
                        --pos;
                    }
                    if(pos == -1) {
                        System.err.println("Matrix not invertible.");
                        return null;
                    }
                    float mult = aux[i][j]/aux[pos][j];
                    for(int k = 0; k<aux[0].length; ++k) {
                        aux[i][k] -= mult*aux[pos][k];
                    }
                }
            }
            
            if(aux[i][i] == 0) {
                System.err.println("Matrix not invertible.");
                return null;
            }
            
            if(aux[i][i] != 1d) {
                float mult = 1/aux[i][i];
                for(int c = 0; c<aux[i].length; ++c) {
                    aux[i][c] *= mult;
                }
            }
        }
        
        for(int i = aux.length-2; i>=0; --i) {
            // map aux[i] <- aux[i] - k*aux[i+1]
            for(int j = i+1; j<aux.length; ++j) {
                float mult = aux[i][j]/aux[j][j];
                for(int k = 0; k<aux[i].length; ++k) {
                    aux[i][k] -= mult*aux[j][k];
                }
            }
        }
        
        float[][] inv = new float[mat.length][mat.length];
        
        for(int i = 0; i<mat.length; ++i) {
            System.arraycopy(aux[i], mat.length, inv[i], 0, mat.length);
        }
        
        return inv;
    }
    
    public static float[] matvect_mult(float[][] mat, float[] v) {
        float[] res = new float[v.length];
        float aux;
        for(int i = 0; i<v.length; ++i) {
            aux = 0;
            for(int j = 0; j<v.length; ++j) {
                aux += mat[i][j]*v[j];
            }
            res[i] = aux;
        }
        return res;
    }
    
    public static float[] vectmat_mult(float[] v, float[][] mat) {
        float[] res = new float[v.length];
        float aux;
        for(int i = 0; i<v.length; ++i) {
            aux = 0;
            for(int j = 0; j<v.length; ++j) {
                aux += mat[j][i]*v[i];
            }
            res[i] = aux;
        }
        return res;
    }
    
    public static float[][] gen_smi(float[][] A, float u) {
        float[][] aux = new float[A.length][A[0].length];
        for(int i = 0; i<aux.length; ++i) {
            System.arraycopy(A[i], 0, aux[i], 0, A.length);
            aux[i][i] -= u;
        }
        return aux;
    }
    
    public static final float mgn_vect(float[] v) {
        float sum = 0;
        for(int i = 0; i<v.length; ++i) {
            sum += v[i]*v[i];
        }
        return (float) Math.sqrt(sum);
    }
    
    public static final float inner_prod(float[] v1, float[] v2) {
        float sum = 0;
        for(int i = 0; i<v1.length; ++i) {
            sum += v1[i]*v2[i];
        }
        return sum;
    }
    
    public static final void scl(float[] v, float scl) {
        for(int i = 0; i<v.length; ++i) {
            v[i] /= scl;
        }
    }
}
