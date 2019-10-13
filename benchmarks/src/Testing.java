
import java.util.BitSet;

public class Testing {

    public static void main(String[] args) {
        BitSet b1 = new BitSet();
        b1.set(1);
        b1.set(2);
        b1.set(3);
        b1.set(6);
        b1.set(7);
        BitSet b2 = new BitSet();
        b2.set(2);
        b2.set(3);
        b2.set(4);
        b2.set(5);
        b2.set(8);
        b1.and(b2);
        System.out.println(b1.cardinality());
    }
}
    