package strings;

import java.math.BigInteger;
import java.util.Random;

public class Hashing {

    static final int multiplier = 43;
    static final Random rnd = new Random();
    static final int mod1 = BigInteger.valueOf((int) (1e9 + rnd.nextInt((int) 1e9))).nextProbablePrime().intValue();
    static final int mod2 = BigInteger.valueOf((int) (1e9 + rnd.nextInt((int) 1e9))).nextProbablePrime().intValue();
    static final int invMultiplier1 = BigInteger.valueOf(multiplier).modInverse(BigInteger.valueOf(mod1)).intValue();
    static final int invMultiplier2 = BigInteger.valueOf(multiplier).modInverse(BigInteger.valueOf(mod2)).intValue();

    long[] hash1, hash2;
    long[] inv1, inv2;
    int n;

    public Hashing(String s) {
        n = s.length();
        hash1 = new long[n + 1];
        hash2 = new long[n + 1];
        inv1 = new long[n + 1];
        inv2 = new long[n + 1];
        inv1[0] = 1;
        inv2[0] = 1;

        long p1 = 1;
        long p2 = 1;
        for (int i = 0; i < n; i++) {
            hash1[i + 1] = (hash1[i] + s.charAt(i) * p1) % mod1;
            p1 = p1 * multiplier % mod1;
            inv1[i + 1] = inv1[i] * invMultiplier1 % mod1;
            hash2[i + 1] = (hash2[i] + s.charAt(i) * p2) % mod2;
            p2 = p2 * multiplier % mod2;
            inv2[i + 1] = inv2[i] * invMultiplier2 % mod2;
        }
    }

    public long getHash(int i, int len) {
        return (((hash1[i + len] - hash1[i] + mod1) * inv1[i] % mod1) << 32)
                + (hash2[i + len] - hash2[i] + mod2) * inv2[i] % mod2;
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n1 = rnd.nextInt(50);
            String s1 = getRandomString(n1, rnd);
            int n2 = rnd.nextInt(50);
            String s2 = getRandomString(n2, rnd);
            Hashing h1 = new Hashing(s1);
            Hashing h2 = new Hashing(s2);
            for (int k = 0; k < 1000; k++) {
                int i1 = rnd.nextInt(n1 + 1);
                int j1 = rnd.nextInt(n1 - i1 + 1) + i1;
                int i2 = rnd.nextInt(n2 + 1);
                int j2 = rnd.nextInt(n2 - i2 + 1) + i2;
                if (s1.substring(i1, j1).equals(s2.substring(i2, j2)) != (h1.getHash(i1, j1 - i1) == h2.getHash(i2, j2 - i2)))
                    throw new RuntimeException();
            }
        }
    }

    static String getRandomString(int n, Random rnd) {
        char[] s = new char[n];
        for (int i = 0; i < n; i++) {
            s[i] = (char) ('a' + rnd.nextInt(3));
        }
        return new String(s);
    }
}
