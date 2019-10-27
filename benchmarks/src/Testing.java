
public class Testing {

    public static void main(String[] args) {
        String test = "jfdfefksaa";
        long mask = 0;
        mask = (long) test.hashCode() << 32;
        System.out.println(Integer.toHexString(test.hashCode()));
        System.out.println(Long.toHexString(mask+(test.hashCode()&0x7fff_ffff)));
    }
}
    