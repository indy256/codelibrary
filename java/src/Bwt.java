import java.io.*;
import java.util.*;

// https://en.wikipedia.org/wiki/Burrows–Wheeler_transform
public class Bwt {

	public static TransformedData bwt(byte[] s) {
		int n = s.length;
		int[] sa = suffixArray(s);
		byte[] last = new byte[n];
		int position = 0;
		for (int i = 0; i < n; i++) {
			last[i] = s[(sa[i] + n - 1) % n];
			if (sa[i] == 0)
				position = i;
		}
		return new TransformedData(last, position);
	}

	public static class TransformedData {
		final byte[] last;
		final int position;

		TransformedData(byte[] last, int position) {
			this.last = last;
			this.position = position;
		}
	}

	// sort rotations of S in O(n*log(n))
	static int[] suffixArray(byte[] S) {
		int n = S.length;
		Integer[] order = new Integer[n];
		for (int i = 0; i < n; i++)
			order[i] = i;
		Arrays.sort(order, (a, b) -> Integer.compare(Byte.toUnsignedInt(S[a]), Byte.toUnsignedInt(S[b])));
		int[] sa = new int[n];
		int[] classes = new int[n];
		for (int i = 0; i < n; i++) {
			sa[i] = order[i];
			classes[i] = S[i];
		}
		for (int len = 1; len < n; len *= 2) {
			int[] c = classes.clone();
			for (int i = 0; i < n; i++)
				classes[sa[i]] = i > 0 && c[sa[i - 1]] == c[sa[i]] && c[(sa[i - 1] + len / 2) % n] == c[(sa[i] + len / 2) % n] ? classes[sa[i - 1]] : i;
			int[] cnt = new int[n];
			for (int i = 0; i < n; i++)
				cnt[i] = i;
			int[] s = sa.clone();
			for (int i = 0; i < n; i++) {
				int s1 = (s[i] - len + n) % n;
				sa[cnt[classes[s1]]++] = s1;
			}
		}
		return sa;
	}

	public static byte[] inverseBwt(byte[] last, int position) {
		int[] cnt = new int[256];
		for (byte b : last)
			++cnt[Byte.toUnsignedInt(b)];
		for (int i = 1; i < cnt.length; i++)
			cnt[i] += cnt[i - 1];
		int n = last.length;
		int[] t = new int[n];
		for (int i = n - 1; i >= 0; i--)
			t[--cnt[Byte.toUnsignedInt(last[i])]] = i;
		byte[] res = new byte[n];
		int j = t[position];
		for (int i = 0; i < n; i++) {
			res[i] = last[j];
			j = t[j];
		}
		return res;
	}

	static byte[] mtfEncode(byte[] s) {
		byte[] table = new byte[256];
		for (int i = 0; i < table.length; i++)
			table[i] = (byte) i;
		byte[] res = new byte[s.length];
		for (int i = 0; i < s.length; i++) {
			int pos;
			for (pos = 0; table[pos] != s[i]; pos++) ;
			res[i] = (byte) pos;
			byte t = table[pos];
			System.arraycopy(table, 0, table, 1, pos);
			table[0] = t;
		}
		return res;
	}

	static byte[] mtfDecode(byte[] s) {
		byte[] table = new byte[256];
		for (int i = 0; i < table.length; i++)
			table[i] = (byte) i;
		byte[] res = new byte[s.length];
		for (int i = 0; i < s.length; i++) {
			int pos = Byte.toUnsignedInt(s[i]);
			res[i] = table[pos];
			byte t = table[pos];
			System.arraycopy(table, 0, table, 1, pos);
			table[0] = t;
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) throws Exception {
		Random rnd = new Random(1);
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(20) + 1;
			byte[] s = new byte[n];
			for (int i = 0; i < n; i++)
				s[i] = (byte) (rnd.nextInt(256));
			TransformedData transformedData = bwt(s);
			byte[] s2 = inverseBwt(transformedData.last, transformedData.position);
			if (!Arrays.equals(s, s2))
				throw new RuntimeException();
		}

		FileInputStream fs = new FileInputStream("src/Bwt.java");
		byte[] data = new byte[1_000_000];
		int len = fs.read(data, 0, data.length);
		data = Arrays.copyOf(data, len);
		TransformedData bwt = bwt(data);
		byte[] encoded = mtfEncode(bwt.last);
		byte[] last2 = mtfDecode(encoded);
		int[] encodedInts = new int[encoded.length];
		for (int i = 0; i < encoded.length; i++)
			encodedInts[i] = Byte.toUnsignedInt(encoded[i]);
		byte[] data2 = inverseBwt(bwt.last, bwt.position);
		System.out.println(Arrays.equals(data, data2));
		System.out.println(Arrays.equals(bwt.last, last2));
		Locale.setDefault(Locale.US);
		System.out.printf("%d -> %.0f\n", len, optimalCompressedLength(encodedInts));
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
