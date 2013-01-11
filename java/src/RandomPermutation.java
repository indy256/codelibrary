import java.util.*;

public class RandomPermutation {

	static void swap(int[] a, int i, int j) {
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	public static int[] getRandomPermutation(int n, Random rnd) {
		int[] res = new int[n];
		for (int i = 0; i < n; i++)
			res[i] = i;
		for (int i = 0; i < n; i++)
			swap(res, i, i + rnd.nextInt(n - i));
		return res;
	}

	public static int[] getRandomCombination(int n, int m, Random rnd) {
		int[] res = new int[n];
		for (int i = 0; i < n; i++)
			res[i] = i;
		for (int i = 0; i < m; i++)
			swap(res, i, i + rnd.nextInt(n - i));
		return Arrays.copyOf(res, m);
	}

	// Usage example
	public static void main(String[] args) {
		Random rnd = new Random();
		int[] p = getRandomPermutation(5, rnd);
		System.out.println(Arrays.toString(p));
	}
}
