import java.util.*;

public class Permutations {

	public static boolean nextPermutation(int[] p) {
		for (int a = p.length - 2; a >= 0; --a)
			if (p[a] < p[a + 1])
				for (int b = p.length - 1; ; --b)
					if (p[b] > p[a]) {
						int t = p[a];
						p[a] = p[b];
						p[b] = t;
						for (++a, b = p.length - 1; a < b; ++a, --b) {
							t = p[a];
							p[a] = p[b];
							p[b] = t;
						}
						return true;
					}
		return false;
	}

	public static int[] permutationByNumber(int len, long number) {
		long[] fact = new long[len];
		fact[0] = 1;
		for (int i = 1; i < len; i++) {
			fact[i] = i * fact[i - 1];
		}
		int[] p = new int[len];
		for (int i = 0; i < len; i++) {
			p[i] = (int) (number / fact[len - 1 - i]);
			number %= fact[len - 1 - i];
			for (int j = 0; j < i; j++) {
				if (p[j] <= p[i]) {
					++p[i];
				}
			}
		}
		return p;
	}

	public static long numberByPermutation(int[] p) {
		int n = p.length;
		long[] fact = new long[n];
		fact[0] = 1;
		for (int i = 1; i < n; i++) {
			fact[i] = i * fact[i - 1];
		}
		long res = 0;
		for (int i = 0; i < n; i++) {
			int a = p[i];
			for (int j = 0; j < i; j++) {
				if (p[j] < p[i]) {
					--a;
				}
			}
			res += a * fact[n - 1 - i];
		}
		return res;
	}

	public static void generatePermutations(int[] p, int depth) {
		int n = p.length;
		if (depth == n) {
			System.out.println(Arrays.toString(p));
			return;
		}
		for (int i = 0; i < n; i++) {
			if (p[i] == 0) {
				p[i] = depth;
				generatePermutations(p, depth + 1);
				p[i] = 0;
			}
		}
	}

	public static long nextPermutation(long x) {
		long s = x & -x;
		long r = x + s;
		long ones = x ^ r;
		ones = (ones >> 2) / s;
		return r | ones;
	}

	public static List<List<Integer>> decomposeIntoCycles(int[] p) {
		int n = p.length;
		boolean[] vis = new boolean[n];
		List<List<Integer>> res = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			if (vis[i])
				continue;
			int j = i;
			List<Integer> cur = new ArrayList<>();
			do {
				cur.add(j);
				vis[j] = true;
				j = p[j];
			} while (j != i);
			res.add(cur);
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		// print all permutations method 1
		generatePermutations(new int[2], 1);

		// print all permutations method 2
		int[] p = {1, 2, 3};
		do {
			System.out.println(Arrays.toString(p));
		} while (nextPermutation(p));

		p = new int[]{2, 0, 1};
		System.out.println(4 == numberByPermutation(p));
		System.out.println(Arrays.equals(p, permutationByNumber(p.length, numberByPermutation(p))));
		int x = 7;
		System.out.println(x == numberByPermutation(permutationByNumber(3, x)));
		System.out.println(0b1101 == nextPermutation(0b1011));
		System.out.println(decomposeIntoCycles(new int[]{0, 2, 1, 3}));
	}
}
