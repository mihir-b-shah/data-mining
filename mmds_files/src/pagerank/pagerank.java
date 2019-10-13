
package pagerank;

import java.util.Arrays;
import java.util.Scanner;

public class pagerank {
    
    private static class matrix {
        private final double[][] mat;
        
        public matrix(int N) {mat = new double[N][N];}
        public void mod(int i, int j, double val) {mat[i][j] = val;}
        public void scl(boolean flg, int rc, double scl) {
            if(flg) 
                for(int i = 0; i<mat.length; ++i)
                    mat[rc][i] *= scl;
            else 
                for(int i = 0; i<mat[0].length; ++i)
                    mat[i][rc] *= scl;
        }
        
        public void fill(double val) {
            for(int i = 0; i<mat.length; ++i)
                for(int j = 0; j<mat.length; ++j)
                    mat[i][j] = val;
        }
        
        public double get(int i, int j) {return mat[i][j];}

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i<mat.length; ++i) {
                for(int j = 0; j<mat[0].length; ++j)
                    sb.append(String.format("%.3f\t", mat[i][j]));
                sb.append('\n');
            }
            return sb.toString();
        }
               
    }
    
    private static class vector implements Comparable<vector> {
        private final double[] vect;
        
        public vector(int N) {vect = new double[N];}
        
        public int get_n() {return vect.length;}
        public void mod(int i, double val) {vect[i] = val;}
        public double get(int i) {return vect[i];}
        public void scl(double val) {
            for(int i = 0; i<vect.length; ++i)
                vect[i] *= val;
        }
        
        public void fill(double val) {
            Arrays.fill(vect, val);
        }
        
        public vector add(double scl, vector v) {
            for(int i = 0; i<vect.length; ++i)
                vect[i] += scl*v.get(i);
            return this;
        }
        
        @Override
        public int compareTo(vector v) {
            double sum = 0;
            for(int i = 0; i<vect.length; ++i)
                sum += Math.pow(vect[i]-v.get(i),2);
            sum /= vect.length;
            return Math.abs(sum) < 1e-4 ? 0 : 1;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i<vect.length; ++i)
                sb.append(String.format("r%c: %.3f%n", 'A'+i, vect[i]));
            return sb.toString();
        }
    }
    
    public static vector mult(double sc, matrix m, vector v) {
        vector vect = new vector(v.get_n());
        for(int i = 0; i<v.get_n(); ++i) {
            for(int j = 0; j<v.get_n(); ++j) {
                vect.mod(i, vect.get(i)+m.get(i, j)*v.get(j));
            }
            vect.mod(i, sc*vect.get(i));
        }
        return vect;
    }
    
    public static void main(String[] args) {
        Scanner f = new Scanner(System.in);
        System.out.print("N: ");
        int N = f.nextInt();
        matrix M = new matrix(N);
        int[] NN = new int[N];
        vector v = new vector(N);
        
        for(int i = 0; i<N; ++i) {
            System.out.printf("%n%c: ", 'A'+i);
            String[] links = f.next().split(",");
            NN[i] = links.length;
            for (String link : links) 
                M.mod(link.charAt(0) - 'A', i, 1);
        }
        
        System.out.printf("%nβ: ");
        double B = f.nextDouble();
        
        for(int i = 0; i<N; ++i)
            M.scl(false, i, 1D/NN[i]);
        
        vector one = new vector(N);
        one.fill(1);
        
        vector prev;
        vector iter = new vector(N);
        iter.fill(1D/N);

        do {
            prev = iter;
            iter = mult(B, M, iter).add((1-B)/N, one);
        } while(iter.compareTo(prev) != 0);

        System.out.printf("%n%s%n", iter);
    }
}
