package pagerank;


import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class markov {

    public static void main(String[] args) {
        int M = Integer.parseInt(
                JOptionPane.showInputDialog("Enter number of states."));
        int N = Integer.parseInt(
                JOptionPane.showInputDialog("Enter predicted time."));

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

        double[][] coeff = new double[M][M];

        for (int i = 0; i < M; ++i) {
            for (int j = 0; j < M; ++j) {
                coeff[i][j] = Double.parseDouble(
                        ((JTextField) fields[i][j]).getText());
            }
        }

        double[][] result = new double[M][M];
        for (int i = 0; i < M; ++i) {
            System.arraycopy(coeff[i], 0, result[i], 0, M);
        }

        // naive matrix exponentiation
        
        for(int k = 0; k<N-1; ++k) {
            for(int i = 0; i<M; ++i) {
                for(int j = 0; j<M; ++j) {
                    double aux = 0;
                    for(int m = 0; m<M; ++m) {
                        aux += result[m][i]*coeff[j][m];
                    }
                    result[i][j] = aux;
                }
            }
        }
        
        StringBuilder sb = new StringBuilder();
        
        for(double[] i: result) {
            for(double j: i) {
                sb.append(String.format("%6.3f\t", j));
            }
            sb.append('\n');
        }
        
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}
