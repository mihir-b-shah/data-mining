package pagerank;


import java.util.Scanner;


public class nonlinear_newton {
    
    private double[] aux;
    
    public nonlinear_newton(int N) {
        aux = new double[N];
    }
    
    @FunctionalInterface
    public interface function_1 {
        double f(double x, double y);
    }
    
    @FunctionalInterface
    public interface deriv_f1_x {
        double dfx(double x, double y);
    }
    
    @FunctionalInterface
    public interface deriv_f1_y {
        double dfy(double x, double y);
    }
    
    @FunctionalInterface
    public interface function_2 {
        double g(double x, double y);
    }
    
    @FunctionalInterface
    public interface deriv_f2_x {
        double dgx(double x, double y);
    }
    
    @FunctionalInterface
    public interface deriv_f2_y {
        double dgy(double x, double y);
    }
    
    private static class jacobian {
        
        private Object[][] function_matrix;
            
        public jacobian(deriv_f1_x dfx, deriv_f2_x dgx, 
                        deriv_f1_y dfy, deriv_f2_y dgy) {
            function_matrix = new Object[2][2];
            function_matrix[0][0] = dfx;
            function_matrix[0][1] = dfy;
            function_matrix[1][0] = dgx;
            function_matrix[1][1] = dgy;
        }
        
        public void eval(double[][] matrix, double x0, double y0) {
            matrix[0][0] = ((deriv_f1_x) function_matrix[0][0]).dfx(x0, y0);
            matrix[0][1] = ((deriv_f1_y) function_matrix[0][1]).dfy(x0, y0);
            matrix[1][0] = ((deriv_f2_x) function_matrix[1][0]).dgx(x0, y0);
            matrix[1][1] = ((deriv_f2_y) function_matrix[1][1]).dgy(x0, y0);
        }
    }
    
    public void invert(double[][] mat) throws Exception {
        // calc determinant
        double det = mat[0][0]*mat[1][1]-mat[0][1]*mat[1][0];
        if(Double.compare(det,0) != 0) {
            // swaps
            double val = mat[1][1];
            mat[1][1] = mat[0][0];
            mat[0][0] = val;
            mat[1][0] *= -1;
            mat[0][1] *= -1;
        } else throw new Exception("Not possible to invert matrix.");
    }
    
    public void scalar(double[] mat, double sc) {
        for(int i = 0; i<mat.length; ++i)
            mat[i] *= sc;
    }
    
    public void matmult(double[][] mat, double[] sc) {
        for(int i = 0; i<mat.length; ++i) 
            for(int j = 0; j<sc.length; ++j)
                aux[i] += mat[i][j]*sc[j];
        System.arraycopy(aux,0,sc,0,sc.length);
    }
    
    public static void main(String[] args) throws Exception {
        
        nonlinear_newton nn = new nonlinear_newton(2);
        Scanner sc = new Scanner(System.in);
        double[][] matrix = new double[2][2];
        
        function_1 f = (x,y)->(Math.pow(x,3)+Math.pow(y,2));
        deriv_f1_x dfx = (x,y)->(3*Math.pow(x,2));
        deriv_f1_y dfy = (x,y)->(2*Math.pow(y,1));
        function_2 g = (x,y)->(Math.cos(x)+Math.sin(y));
        deriv_f2_x dgx = (x,y)->(-Math.sin(x));
        deriv_f2_y dgy = (x,y)->(Math.cos(y));
        
        jacobian jac = new jacobian(dfx,dgx,dfy,dgy);
        
        System.out.println("Enter x0: ");
        double x0 = sc.nextDouble();
        System.out.println("Enter y0: ");
        double y0 = sc.nextDouble();
        
        double xn = x0;
        double yn = y0;
        
        for(int j = 0; j<10; ++j) {
            jac.eval(matrix, xn, yn);
            nn.invert(matrix);
            double[] scl = new double[2];
            scl[0] = f.f(xn, yn);
            scl[1] = g.g(xn, yn);
            nn.scalar(scl, -1);
            nn.matmult(matrix, scl);
            xn = scl[0]; yn = scl[1];
                    
            System.out.printf("X: %f, Y: %f%n", xn, yn);
        }
    }
}
