
package kmeans;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class tester extends Application {
    
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage st) throws Exception {
                
        quadtree qtree = new quadtree(1f, 1f);
                
        final NumberAxis x_axis = new NumberAxis();
        final NumberAxis y_axis = new NumberAxis();
        
        final ScatterChart<Float,Float> gph = 
                new ScatterChart(x_axis,y_axis);

        XYChart.Series<Float,Float> srs = new XYChart.Series();
        
        for(int i = 0; i<16; ++i) {
            float x = (float) Math.random();
            float y = (float) Math.random();
            srs.getData().add(new XYChart.Data(x,y));
            qtree.add(x, y);
        }

        System.out.println(qtree);
        Scene sc = new Scene(gph, 600,600);
        gph.getData().add(srs);
        st.setScene(sc);
        st.show();
    }
}
