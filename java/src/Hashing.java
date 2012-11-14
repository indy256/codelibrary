import java.math.BigInteger;
import java.util.*;

public class Hashing {

	final int multiplier = 43;
	static final Random rnd = new Random();
	final int mod1 = BigInteger.valueOf((int) (1e9 + rnd.nextInt((int) 1e9))).nextProbablePrime().intValue();
	final int mod2 = BigInteger.valueOf((int) (1e9 + rnd.nextInt((int) 1e9))).nextProbablePrime().intValue();
	long[] p1, p2;
	long[] hash1, hash2;
	int n;

	public void buildHash(String s) {
		n = s.length();
		p1 = new long[n + 1];
		p2 = new long[n + 1];
		hash1 = new long[n + 1];
		hash2 = new long[n + 1];

		p1[0] = 1;
		p2[0] = 1;
		for (int i = 0; i < n; i++) {
			hash1[i + 1] = (hash1[i] + s.charAt(i) * p1[i]) % mod1;
			p1[i + 1] = p1[i] * multiplier % mod1;
			hash2[i + 1] = (hash2[i] + s.charAt(i) * p2[i]) % mod2;
			p2[i + 1] = p2[i] * multiplier % mod2;
		}
	}

	public long getHash(int i, int len) {
		return (((hash1[i + len] - hash1[i] + mod1) * p1[n - i - len] % mod1) << 32)
				+ (hash2[i + len] - hash2[i] + mod2) * p2[n - i - len] % mod2;
	}

	public boolean equals(int i, int j, int len) {
		return getHash(i, len) == getHash(j, len);
	}

	// Usage example
	public static void main(String[] args) {
		String s = "123123";
		Hashing h = new Hashing();
		h.buildHash(s);

		System.out.println(true == h.equals(0, 3, 3));
		System.out.println(false == h.equals(1, 3, 2));
	}
}
