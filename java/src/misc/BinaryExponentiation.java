package misc;

// https://en.wikipedia.org/wiki/Exponentiation_by_squaring
public class BinaryExponentiation {

    public static int pow(int x, int n, int mod) {
        int res = 1;
        for (long p = x; n > 0; n >>= 1, p = (p * p) % mod) {
            if ((n & 1) != 0) {
                res = (int) (res * p % mod);
            }
        }
        return res;
    }

    // usage example
    public static void main(String[] args) {
        System.out.println(8 == pow(2, 3, 100));
    }
}
