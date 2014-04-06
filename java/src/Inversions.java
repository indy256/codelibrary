import java.util.*;

public class Inversions {

	public static int inversions(int[] a, int low, int high) {
		if (low >= high - 1)
			return 0;
		int mid = (low + high) >>> 1;
		int res1 = inversions(a, low, mid);
		int res2 = inversions(a, mid, high);
		int res = res1 + res2;

		int size = high - low;
		int[] b = new int[size];
		int i = low;
		int j = mid;
		for (int k = 0; k < size; k++) {
			if (j >= high || i < mid && a[i] <= a[j]) {
				b[k] = a[i++];
			} else {
				b[k] = a[j++];
				res += mid - i;
			}
		}
		System.arraycopy(b, 0, a, low, size);
		return res;
	}

	// random test
	public static void main(String[] args) {
		Random rnd = new Random();
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(100);
			int[] p = new int[n];
			for (int i = 0; i < n; i++)
				p[i] = rnd.nextInt(n);
			int res1 = inversions(p.clone(), 0, p.length);
			int res2 = slowInversions(p);
			if (res1 != res2)
				throw new RuntimeException(res1 + " " + res2);
		}
	}

	static int slowInversions(int[] p) {
		int res = 0;
		for (int i = 0; i < p.length; i++)
			for (int j = 0; j < i; j++)
				if (p[j] > p[i])
					++res;
		return res;
	}
}
