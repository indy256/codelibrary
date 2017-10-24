import java.io.FileInputStream;
import java.util.*;

public class Lzw {
	static final char END = (char) 0xFFFF;

	public static int[] lzwEncoding(String s) {
		List<Integer> result = new ArrayList<>();
		Map<String, Integer> dict = new HashMap<>();
		int cnt = 0;
		for (int i = 0; i < 256; i++)
			dict.put(String.valueOf((char) i), cnt++);
		dict.put(String.valueOf(END), cnt++);
		String cur = "";
		for (char ch : s.toCharArray()) {
			cur += ch;
			if (!dict.containsKey(cur)) {
				Integer e = dict.get(cur.substring(0, cur.length() - 1));
				result.add(e);
				dict.put(cur, cnt++);
				cur = String.valueOf(ch);
			}
		}
		result.add(dict.get(cur));
		result.add(dict.get(String.valueOf(END)));
		int[] res = new int[result.size()];
		for (int i = 0; i < res.length; i++)
			res[i] = result.get(i);
		System.out.printf("Dict items: %,d\n", dict.size());
//		System.out.printf("Dict size: %,d\n", SizingAgent.deepSizeOf(dict));
		return res;
	}

	public static String lzwDecoding(int[] s) {
		StringBuilder sb = new StringBuilder();
		Map<Integer, String> dict = new HashMap<>();
		int cnt = 0;
		for (int i = 0; i < 256; i++)
			dict.put(cnt++, String.valueOf((char) i));
		dict.put(cnt++, String.valueOf(END));

		String cur = dict.get(s[0]);
		sb.append(cur);
		char a = cur.charAt(0);

		for (int i = 1; i < s.length; i++) {
			int code = s[i];
			if (code < cnt) {
				String str = dict.get(code);
				a = str.charAt(0);
				if (a == END)
					break;
				dict.put(cnt++, cur + a);
				sb.append(str);
				cur = str;
			} else {
				cur += a;
				dict.put(cnt++, cur);
				sb.append(cur);
			}
		}

		return sb.toString();
	}

	// Usage example
	public static void main(String[] args) throws Exception {
		FileInputStream fs = new FileInputStream("src/Lzw.java");
		byte[] data = new byte[1_000_000];
		int len = fs.read(data, 0, data.length);
		data = Arrays.copyOf(data, len);
		char[] buffer = new char[len];
		for (int i = 0; i < len; i++)
			buffer[i] = (char) (Byte.toUnsignedInt(data[i]));
		String s = new String(buffer, 0, len);
		int[] encoded = lzwEncoding(s);
		String s2 = lzwDecoding(encoded);
		byte[] data2 = new byte[len];
		for (int i = 0; i < len; i++)
			data2[i] = (byte) s2.charAt(i);
		System.out.println(Arrays.equals(data, data2));
		Locale.setDefault(Locale.US);
		System.out.printf("%d -> %.0f\n", s.length(), optimalCompressedLength(encoded));
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
