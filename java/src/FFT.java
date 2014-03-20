import java.math.BigInteger;
import java.util.Random;

public class FFT {

	public static void fft(double[] a, double[] b, boolean invert) {
		int n = a.length;
		int shift = 32 - Integer.numberOfTrailingZeros(n);
		for (int i = 1; i < n; i++) {
			int j = Integer.reverse(i << shift);
			if (i < j) {
				double temp = a[i];
				a[i] = a[j];
				a[j] = temp;
				temp = b[i];
				b[i] = b[j];
				b[j] = temp;
			}
		}
		for (int len = 2; len <= n; len <<= 1) {
			int halfLen = len >> 1;
			double angle = 2 * Math.PI / len * (invert ? -1 : 1);
			double wLenA = Math.cos(angle);
			double wLenB = Math.sin(angle);
			for (int i = 0; i < n; i += len) {
				double wA = 1;
				double wB = 0;
				for (int j = 0; j < halfLen; j++) {
					double uA = a[i + j];
					double uB = b[i + j];
					double vA = a[i + j + halfLen] * wA - b[i + j + halfLen] * wB;
					double vB = a[i + j + halfLen] * wB + b[i + j + halfLen] * wA;
					a[i + j] = uA + vA;
					b[i + j] = uB + vB;
					a[i + j + halfLen] = uA - vA;
					b[i + j + halfLen] = uB - vB;
					double nextWA = wA * wLenA - wB * wLenB;
					wB = wA * wLenB + wB * wLenA;
					wA = nextWA;
				}
			}
		}
		if (invert) {
			for (int i = 0; i < n; i++) {
				a[i] /= n;
				b[i] /= n;
			}
		}
	}

	public static long[] multiply(long[] a, long[] b) {
		int resultSize = Integer.highestOneBit(Math.max(a.length, b.length) - 1) << 2;
		resultSize = Math.max(resultSize, 2);
		double[] aReal = new double[resultSize];
		double[] aImaginary = new double[resultSize];
		double[] bReal = new double[resultSize];
		double[] bImaginary = new double[resultSize];
		for (int i = 0; i < a.length; i++)
			aReal[i] = a[i];
		for (int i = 0; i < b.length; i++)
			bReal[i] = b[i];
		fft(aReal, aImaginary, false);
		fft(bReal, bImaginary, false);
		for (int i = 0; i < resultSize; i++) {
			double real = aReal[i] * bReal[i] - aImaginary[i] * bImaginary[i];
			aImaginary[i] = aImaginary[i] * bReal[i] + bImaginary[i] * aReal[i];
			aReal[i] = real;
		}
		fft(aReal, aImaginary, true);
		long[] result = new long[resultSize];
		for (int i = 0; i < resultSize; i++)
			result[i] = Math.round(aReal[i]);
		return result;
	}

	// random test
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 1000; step++) {
			int n1 = rnd.nextInt(10) + 1;
			String s1 = "";
			long[] a = new long[n1];
			for (int i = 0; i < n1; i++) {
				int x = rnd.nextInt(10);
				s1 = x + s1;
				a[i] = x;
			}
			int n2 = rnd.nextInt(10) + 1;
			String s2 = "";
			long[] b = new long[n2];
			for (int i = 0; i < n2; i++) {
				int x = rnd.nextInt(10);
				s2 = x + s2;
				b[i] = x;
			}
			long[] res = multiply(a, b);
			long carry = 0;
			String s = "";
			for (int i = 0; i < res.length; ++i) {
				res[i] = res[i] + carry;
				carry = res[i] / 10;
				res[i] %= 10;
				s = res[i] + s;
			}
			BigInteger mul = new BigInteger(s1).multiply(new BigInteger(s2));
			if (!mul.equals(new BigInteger(s)))
				throw new RuntimeException();
		}
	}
}
