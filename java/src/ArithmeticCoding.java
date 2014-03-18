import java.io.*;
import java.util.*;

public class ArithmeticCoding {

	final int BITS = 30;
	final int HIGHEST_BIT = 1 << (BITS - 1);
	final int MASK = (1 << BITS) - 1;
	final int END = 256;

	long low, high;
	int additionalBits;
	long value;
	int[] cumFreq;
	int[] bits;
	int bitsPos;
	List<Boolean> encodedBits;
	List<Integer> decodedBytes;

	public int[] encode(int[] inputBytes) {
		cumFreq = createFenwickTree(END + 1);
		encodedBits = new ArrayList<>();

		low = 0;
		high = (1 << BITS) - 1;

		additionalBits = 0;

		for (int c : inputBytes)
			encodeSymbol(c);

		encodeSymbol(END);
		outputBit(true);

		int[] bits = new int[encodedBits.size()];
		for (int i = 0; i < bits.length; i++)
			bits[i] = encodedBits.get(i) ? 1 : 0;
		return bits;
	}

	void encodeSymbol(int c) {
		long range = high - low + 1;
		high = low + range * sum(cumFreq, c) / sum(cumFreq, END) - 1;
		low = low + range * sum(cumFreq, c - 1) / sum(cumFreq, END);

		while (true) {
			if ((low & HIGHEST_BIT) == (high & HIGHEST_BIT)) {
				outputBit((high & HIGHEST_BIT) != 0);
				low = (low << 1) & MASK;
				high = ((high << 1) + 1) & MASK;
			} else if (high - low < sum(cumFreq, END)) {
				low = (low - (1 << (BITS - 2))) << 1;
				high = ((high - (1 << (BITS - 2))) << 1) + 1;
				++additionalBits;
			} else {
				break;
			}
		}
		increment(cumFreq, c);
	}

	void outputBit(boolean bit) {
		encodedBits.add(bit);
		for (; additionalBits > 0; additionalBits--)
			encodedBits.add(!bit);
	}

	public int[] decode(int[] bits) {
		this.bits = bits;
		cumFreq = createFenwickTree(END + 1);
		decodedBytes = new ArrayList<>();

		value = 0;
		for (bitsPos = 0; bitsPos < BITS; bitsPos++)
			value = (value << 1) + (bitsPos < bits.length ? bits[bitsPos] : 0);

		low = 0;
		high = (1 << BITS) - 1;

		while (true) {
			int c = decodeSymbol();

			if (c == END)
				break;

			decodedBytes.add(c);
			increment(cumFreq, c);
		}

		int[] bytes = new int[decodedBytes.size()];
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = decodedBytes.get(i);
		return bytes;
	}

	int decodeSymbol() {
		int cum = (int) (((value - low + 1) * sum(cumFreq, END) - 1) / (high - low + 1));
		int c = upper_bound(cumFreq, cum);

		long range = high - low + 1;
		high = low + range * sum(cumFreq, c) / sum(cumFreq, END) - 1;
		low = low + range * sum(cumFreq, c - 1) / sum(cumFreq, END);

		while (true) {
			if ((low & HIGHEST_BIT) == (high & HIGHEST_BIT)) {
				low = (low << 1) & MASK;
				high = ((high << 1) + 1) & MASK;
				int b = bitsPos < bits.length ? bits[bitsPos++] : 0;
				value = ((value << 1) + b) & MASK;
			} else if (high - low < sum(cumFreq, END)) {
				low = (low - (1 << (BITS - 2))) << 1;
				high = ((high - (1 << (BITS - 2))) << 1) + 1;
				int b = bitsPos < bits.length ? bits[bitsPos++] : 0;
				value = ((value - (1 << (BITS - 2))) << 1) + b;
			} else {
				break;
			}
		}

		return c;
	}

	// T[i] += 1
	static void increment(int[] t, int i) {
		for (; i < t.length; i |= i + 1)
			++t[i];
	}

	// sum[0..i]
	static int sum(int[] t, int i) {
		int res = 0;
		for (; i >= 0; i = (i & (i + 1)) - 1)
			res += t[i];
		return res;
	}

	// Returns min(p|sum[0,p]>sum)
	static int upper_bound(int[] t, int sum) {
		int pos = -1;
		for (int blockSize = Integer.highestOneBit(t.length); blockSize != 0; blockSize >>= 1) {
			int nextPos = pos + blockSize;
			if (nextPos < t.length && sum >= t[nextPos]) {
				sum -= t[nextPos];
				pos = nextPos;
			}
		}
		return pos + 1;
	}

	static int[] createFenwickTree(int n) {
		int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			++res[i];
			int j = i | (i + 1);
			if (j < n)
				res[j] += res[i];
		}
		return res;
	}

	// random tests
	public static void main(String[] args) throws IOException {
		ArithmeticCoding codec = new ArithmeticCoding();
		int[] a = new int[1000_000];
		int[] encodedBits = codec.encode(a);
		System.out.println(a.length + " -> " + encodedBits.length / 8);
		System.out.println(Arrays.equals(a, codec.decode(encodedBits)));

		Random rnd = new Random();
		for (int step = 0; step < 10_000; step++) {
			int n = rnd.nextInt(100) + 1;
			int[] inputBytes = new int[n];
			for (int i = 0; i < n; i++)
				inputBytes[i] = rnd.nextInt(255);

			encodedBits = codec.encode(inputBytes);
			int[] decodedBytes = codec.decode(encodedBits);

			if (!Arrays.equals(inputBytes, decodedBytes))
				throw new RuntimeException();
		}

		FileInputStream fs = new FileInputStream("src/ArithmeticCoding.java");
		byte[] buffer = new byte[10_000_000];
		int len = fs.read(buffer, 0, buffer.length);
		a = new int[len];
		for (int i = 0; i < len; i++)
			a[i] = Byte.toUnsignedInt(buffer[i]);

		encodedBits = codec.encode(a);
		Locale.setDefault(Locale.US);
		System.out.printf("%d -> %d (%.0f)\n", a.length, encodedBits.length / 8, optimalCompressedLength(a));
		System.out.println(Arrays.equals(a, codec.decode(encodedBits)));
	}

	static double optimalCompressedLength(int[] a) {
		int max = 0;
		for (int x : a)
			max = Math.max(max, x);
		int[] freq = new int[max + 1];
		for (int x : a)
			++freq[x];
		double optimalLength = 0;
		for (int f : freq)
			if (f > 0)
				optimalLength += f * Math.log((double) a.length / f) / Math.log(2) / 8;
		return optimalLength;
	}
}
