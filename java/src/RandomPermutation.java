import java.util.*;

public class RandomPermutation {

	public static int[] getRandomPermutation(int n, Random rnd) {
		int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			res[i] = i;
		}
		for (int i = res.length - 1; i > 0; i--) {
			int j = rnd.nextInt(i + 1);
			int t = res[i];
			res[i] = res[j];
			res[j] = t;
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		Random rnd = new Random();
		int[] p = getRandomPermutation(10, rnd);
		System.out.println(Arrays.toString(p));
	}
}
