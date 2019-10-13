
package kmeans;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import matrix_compression.buf_float;

public class vector_xy {
    private final buf_float xs;
    private final buf_float ys;
    private int size;
    
    public vector_xy() {
        xs = new buf_float();
        ys = new buf_float();
    }
    
    public void add(float x, float y) {
        xs.push(x); ys.push(y);
        ++size;
    } 
    
    public int get_size() {
        return size;
    }
    
    public float get_x(int i) {
        return xs.get(i);
    }
    
    public float get_y(int i) {
        return ys.get(i);
    }
}
