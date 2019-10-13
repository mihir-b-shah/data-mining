
package matrix_compression;

// a stack
// but i didnt call it that....

public class buf_float {
    private float[] vect;
    private int pos;
    
    public buf_float() {
        vect = new float[2];
    }

    public void push(float f) {
        if(vect.length == pos) {
            float[] aux = new float[vect.length << 1];
            System.arraycopy(vect, 0, aux, 0, vect.length);
            vect = aux;
        }
        vect[pos++] = f;
    }
    
    public float get(int i) {
        return vect[i];
    }
    
    public float peek() {
        return vect[pos];
    }
        
    public int get_range(float f) {
        int left = 0;
        int right = pos;
        int mid = 0;
        while(left < right) {
            mid = (left+right)>>1;
            if(f > vect[mid]) {
                left = mid+1;
            } else {
                right = mid;
            }
        }
                
        return mid;
    }
    
    public float pop() {
        return vect[pos--];
    }
    
    public boolean empty() {
        return pos == 0;
    }
    
    
}
