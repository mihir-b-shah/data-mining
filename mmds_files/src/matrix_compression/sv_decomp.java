package matrix_compression;

import java.util.HashSet;

public class sv_decomp {

    private final float[][] U;
    private final float[][] S;
    private float[][] V;
    private final float[][] A;
    private final float[][] Apr;
    private float[] eigenvals;
    private final int r;
    private static final float IFTS = 1e-2f;

    public sv_decomp(int r, float[][] Apr) {
        this.Apr = Apr;
        A = new float[Apr[0].length][Apr[0].length];
        final int size = Apr[0].length;
        final int inner_size = Apr.length;
        float aux;
        for(int i = 0; i<size; ++i) {
            for(int j = 0; j<size; ++j) {
                aux = 0f;
                for(int k = 0; k<inner_size; ++k) {
                    aux += Apr[i][k]*Apr[k][j];
                }
                A[i][j] = aux;
            }
        }
        this.r = r;
        U = new float[A.length][r];
        S = new float[r][r];
    }
   
    public float[][] get_U() {
        return U;
    }

    public float[][] get_S() {
        return S;
    }

    public float[][] get_V() {
        return V;
    }

    private final float secant(float lambda) {
        return (get_det(lambda+IFTS)-get_det(lambda))/IFTS;
    }
    
    private void solve(float[][] aux) {
        outer: for(int i = 0; i<aux.length; ++i) {
            for(int j = 0; j<i; ++j) {
                int pos = i-1;
                if(aux[i][j] != 0f) {
                    while(aux[pos][j] == 0) {
                        --pos;
                    }
                    if(pos == -1) {
                        System.err.println("Matrix not invertible.");
                        return;
                    }
                    float mult = aux[i][j]/aux[pos][j];
                    for(int k = 0; k<aux[0].length; ++k) {
                        aux[i][k] -= mult*aux[pos][k];
                    }
                }
            }
            
            if(aux[i][i] == 0) {
                System.err.println("Matrix not invertible.");
                return;
            }
            
            if(aux[i][i] != 1f) {
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
    }
    
    public void svd() {
        eigenvals = get_eigenvals(-1_000_000f, 1_000_000f);
        float[][] aux = new float[A.length][A[0].length+1];
        float[][] eigenvects = new float[eigenvals.length][A[0].length];
        final int size = eigenvals.length;
        for(int i = 0; i<size; ++i) {
            System.arraycopy(A[i], 0, aux[i], 0, A.length);
            aux[i][i] -= eigenvals[i];
            solve(aux);
            // shoot i cant use arraycopy nooooooo
            for(int j = 0; j<aux.length; ++j) {
                eigenvects[i][j] = aux[j][A.length];
            }
        }
        for(int i = 0; i<eigenvals.length; ++i) {
            S[i][i] = eigenvals[i];
        }
        qsort(eigenvects);
        V = eigenvects;
        for(int i = 0; i<eigenvals.length; ++i) {
            S[i][i] = (float) Math.sqrt(eigenvals[i]);
        }
        popul_U();
    }
    
    // multiply Apr by V by inverse of S
    // Assumes A is SQUARE to get guranteed orthogonal eigenvectors
    public void popul_U() {
        float[][] aux = new float[A.length][V.length];
        for(int i = 0; i<A.length; ++i) {
            for(int j = 0; j<V.length; ++j) {
                for(int k = 0; k<A[0].length; ++k) {
                    aux[i][j] += A[i][k]*V[k][j];
                }
            }
        }
        for(int i = 0; i<U.length; ++i) {
            for(int j = 0; j<S.length; ++j) {
                for(int k = 0; k<S[0].length; ++k) {
                    U[i][j] += aux[i][k]/S[k][j];
                }
            }
        }
    }
    
    public void qsort(float[][] array) {
        qsort(array, 0, array.length-1);
    }

    private void qsort(float[][] array, int start, int end) {
        if(start >= end)
            return;
        int index = partition(array, start, end, (start+end) >> 1);
        qsort(array, start, index-1);
        qsort(array, index, end);
    }
    
    private int partition(float[][] array, int start, int end, int pivot) {
        while(start < end) {
            while(eigenvals[start] > eigenvals[pivot])
                start++;
            
            while(eigenvals[end] < eigenvals[pivot])
                end--;
            
            if(start <= end) {
                swap(start, end, array);
                start++;
                end--;
            }
        }
        
        return start;
    }
    
    private static final void swap(int i, int j, float[][] array) {
        float[] temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    // screw efficiency right?
    // O(n^3) for determinant with fat constant factors
    public float[] get_eigenvals(float LL, float UL) {
        float[] ret = new float[r];
        float lambda,lambda_prior;
        float tol = (UL-LL)/100f;
        buf_float stack = new buf_float();
        HashSet<Float> set = new HashSet<>();
        for(int a = 0; a<r; ++a) {
            lambda = LL + (UL-LL)*((float) Math.random());
            do {
                if(set.contains(lambda)) {
                    break;
                }
                stack.push(lambda/tol*tol);
                lambda_prior = lambda;
                lambda -= get_det(lambda)/secant(lambda);
            } while(Math.abs(lambda-lambda_prior) < tol);
            while(!stack.empty()) {
                set.add(stack.pop());
            }
            ret[a] = lambda;
        }
        return ret;
    }
    
    public float get_det(float lambda) {       
        float[][] aux = new float[A.length][A.length];
        for(int i = 0; i<aux.length; ++i) {
            System.arraycopy(A[i], 0, aux[i], 0, aux[0].length);
            aux[i][i] -= lambda;
        }
     
        
        // first find triangular matrix, 1s on diagonal, 0 on Left
        
        outer: for(int i = 0; i<aux.length; ++i) {
            for(int j = 0; j<i; ++j) {
                int pos = i-1;
                if(aux[i][j] != 0f) {
                    while(aux[pos][j] == 0) {
                        --pos;
                    }
                    float mult = aux[i][j]/aux[pos][j];
                    for(int k = 0; k<aux[0].length; ++k) {
                        aux[i][k] -= mult*aux[pos][k];
                    }
                }
            }
            
            if(aux[i][i] == 0) {
                System.err.println("Matrix not invertible.");
                return 0f;
            }
        }
        
        float val = 1f;
        for(int i = 0; i<aux.length; ++i) {
            val *= aux[i][i];
        }
        
        return val;
    }
    
    
    public static void main(String[] args) {
        float[][] A = new float[100][100];
        for(int i = 0; i<A.length; ++i) {
            for(int j = 0; j<A.length; ++j) {
                A[i][j] = (float) (1000*Math.random());
            }
        }
        sv_decomp sv = new sv_decomp(10, A);
        sv.svd();
    }

}
