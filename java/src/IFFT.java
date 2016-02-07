import java.math.BigInteger;
import java.util.Random;

public class IFFT {

	static int pow(int x, int n, int mod) {
		int res = 1;
		for (long p = x; n > 0; n >>= 1, p = (p * p) % mod) {
			if ((n & 1) != 0) {
				res = (int) (res * p % mod);
			}
		}
		return res;
	}

	// a.length == b.length == 2^x
	public static void fft(int[] a, boolean invert, int mod, int root) {
		final int root_inv = pow(root, mod - 2, mod);
		final int root_pw = 1 << 20;

		int n = a.length;
		int shift = 32 - Integer.numberOfTrailingZeros(n);
		for (int i = 1; i < n; i++) {
			int j = Integer.reverse(i << shift);
			if (i < j) {
				int temp = a[i];
				a[i] = a[j];
				a[j] = temp;
			}
		}
		for (int len = 2; len <= n; len <<= 1) {
			int wlen = invert ? root_inv : root;
			for (int i = len; i < root_pw; i <<= 1)
				wlen = (int) ((long) wlen * wlen % mod);
			for (int i = 0; i < n; i += len) {
				int w = 1;
				for (int j = 0; j < len / 2; ++j) {
					int u = a[i + j];
					int v = (int) ((long) a[i + j + len / 2] * w % mod);
					a[i + j] = (u + v) % mod;
					a[i + j + len / 2] = (u - v + mod) % mod;
					w = (int) ((long) w * wlen % mod);
				}
			}
		}
		if (invert) {
			int nrev = pow(n, mod - 2, mod);
			for (int i = 0; i < n; ++i)
				a[i] = (int) ((long) a[i] * nrev % mod);
		}
	}

	public static int[] multiply(int[] a, int[] b) {
		int resultSize = Integer.highestOneBit(Math.max(a.length, b.length) - 1) << 2;
		resultSize = Math.max(resultSize, 2);
		int[] aReal = new int[resultSize];
		int[] bReal = new int[resultSize];
		for (int i = 0; i < a.length; i++)
			aReal[i] = a[i];
		for (int i = 0; i < b.length; i++)
			bReal[i] = b[i];

		final int mod = 0x1c000001;
		final int root = 899;
//		final int mod = 7340033;
//		final int root = 5;

		fft(aReal, false, mod, root);
		fft(bReal, false, mod, root);
		for (int i = 0; i < resultSize; i++) {
			aReal[i] = (int) (((long) aReal[i] * bReal[i]) % mod);
		}
		fft(aReal, true, mod, root);
		long carry = 0;
		for (int i = 0; i < resultSize; i++) {
			aReal[i] += carry;
			carry = aReal[i] / 10;
			aReal[i] %= 10;
		}
		return aReal;
	}

	// random test
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 1000; step++) {
			int n1 = rnd.nextInt(10) + 1;
			String s1 = "";
			int[] a = new int[n1];
			for (int i = 0; i < n1; i++) {
				int x = rnd.nextInt(10);
				s1 = x + s1;
				a[i] = x;
			}
			int n2 = rnd.nextInt(10) + 1;
			String s2 = "";
			int[] b = new int[n2];
			for (int i = 0; i < n2; i++) {
				int x = rnd.nextInt(10);
				s2 = x + s2;
				b[i] = x;
			}
			int[] res = multiply(a, b);
			String s = "";
			for (long v : res) {
				s = v + s;
			}
			BigInteger mul = new BigInteger(s1).multiply(new BigInteger(s2));
			if (!mul.equals(new BigInteger(s)))
				throw new RuntimeException();
		}
	}
}
