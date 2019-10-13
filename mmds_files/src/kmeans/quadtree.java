package kmeans;

import java.util.ArrayDeque;

public class quadtree {

    private final quadtree[] lower;
    private final float x;
    private final float y;

    public quadtree(float x, float y) {
        lower = new quadtree[4];
        this.x = x;
        this.y = y;
    }

    public float get_x() {
        return x;
    }
    
    public float get_y() {
        return y;
    }
    
    @Override
    public int hashCode() {
        return Float.floatToIntBits(x)^Float.floatToIntBits(y);
    }
    
    @Override
    public boolean equals(Object qt) {
        if(qt instanceof quadtree) {
            return ((quadtree) qt).get_x() == x &&
                    ((quadtree) qt).get_y() == y;
        } return false;
    }
    
    public quadtree[] get_lower() {
        return lower;
    }
    
    public static float dist(quadtree t1, quadtree t2) {
        return (float) Math.sqrt(Math.pow(t1.get_x()-t2.get_x(),2)+
                Math.pow(t1.get_y()-t2.get_y(),2));
    }
       
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ArrayDeque<quadtree> stack = new ArrayDeque<>();
        
        stack.push(this);
        while(!stack.isEmpty()) {
            quadtree qtree = stack.pop();
            sb.append(String.format("(%f, %f)%n", qtree.get_x(), qtree.get_y()));
            quadtree[] lower = qtree.get_lower();
            for(int i = 0; i<4; ++i) {
                if(lower[i] != null) {
                    stack.push(lower[i]);
                }
            }
        }

        return sb.toString();
    }
    
    /*
    jump between bounds- so if i have a y lower limit then upper limit
    in the search go between them yeee!
    maybe... lets try this later
    */
    public quadtree nn(float x, float y) {
        if (this.x == x && this.y == y) {
            return this;
        } else if (this.x <= x && this.y <= y) {
            if (lower[0] != null) {
                return lower[0].nn(x, y);
            } else {
                return this;
            }
        } else if (this.x > x && this.y <= y) {
            if (lower[1] != null) {
                return lower[1].nn(x, y);
            } else {
                return this;
            }
        } else if (this.x <= x && this.y > y) {
            if (lower[2] != null) {
                return lower[2].nn(x, y);
            } else {
                return this;
            }
        } else {
            if (lower[3] != null) {
                lower[3].nn(x, y);
            } else {
                return this;
            }
        }
        
        return null;
    }
    
    public void add(float x, float y) {
        if (this.x == x && this.y == y) {
        } else if (this.x <= x && this.y <= y) {
            if (lower[0] != null) {
                lower[0].add(x, y);
            } else {
                lower[0] = new quadtree(x, y);
            }
        } else if (this.x > x && this.y <= y) {
            if (lower[1] != null) {
                lower[1].add(x, y);
            } else {
                lower[1] = new quadtree(x, y);
            }
        } else if (this.x <= x && this.y > y) {
            if (lower[2] != null) {
                lower[2].add(x, y);
            } else {
                lower[2] = new quadtree(x, y);
            }
        } else {
            if (lower[3] != null) {
                lower[3].add(x, y);
            } else {
                lower[3] = new quadtree(x, y);
            }
        }
    }
    
    public static void main(String[] args) {
        quadtree qtree = new quadtree(1f,1f);
        qtree.add(0.7f, 0.8f);
        qtree.add(0.6f, 0.4f);
        qtree.add(0.15f, 0.6f);
        qtree.nn(0.4f, 0.5f);
    }

    public void adjust(float xo, float yo, float x, float y) {
        remove(xo, yo);
        add(x,y);
    }
    
    private void remove(float f, float f0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
