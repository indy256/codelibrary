import java.util.Arrays;

public class ZFunction {

	// z[i] = lcp(s[0..], s[i..])
	public static int[] zFunction(String s) {
		int[] z = new int[s.length()];
		for (int i = 1, l = 0, r = 0; i < z.length; ++i) {
			if (i <= r)
				z[i] = Math.min(r - i + 1, z[i - l]);
			while (i + z[i] < z.length && s.charAt(z[i]) == s.charAt(i + z[i]))
				++z[i];
			if (i + z[i] - 1 > r) {
				l = i;
				r = i + z[i] - 1;
			}
		}
		return z;
	}

	// Usage example
	public static void main(String[] args) {
		System.out.println(Arrays.toString(zFunction("0010010")));
	}
}
