import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Partitions {

	public static boolean nextPartition(List<Integer> p) {
		int n = p.size();
		if (n <= 1)
			return false;
		int s = p.remove(n - 1) - 1;
		int i = n - 2;
		while (i > 0 && p.get(i) == p.get(i - 1)) {
			s += p.remove(i);
			--i;
		}
		p.set(i, p.get(i) + 1);
		while (s-- > 0) {
			p.add(1);
		}
		return true;
	}

	public static long countPartitions(int n) {
		long[] p = new long[n + 1];
		p[0] = 1;
		for (int i = 1; i <= n; i++) {
			for (int j = 0; j <= n - i; j++) {
				p[j + i] += p[j];
			}
		}
		return p[n];
	}

	public static long[][] partitionFunction(int n) {
		long[][] p = new long[n + 1][n + 1];
		p[0][0] = 1;
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= i; j++) {
				for (int k = 0; k <= j; k++) {
					p[i][j] += p[i - j][k];
				}
			}
		}
		long res = 0;
		for (int j = 0; j <= n; j++) {
			res += p[n][j];
		}
		return p;
	}

	// Usage example
	public static void main(String[] args) {
		System.out.println(7 == countPartitions(5));
		System.out.println(627 == countPartitions(20));
		System.out.println(5604 == countPartitions(30));
		System.out.println(204226 == countPartitions(50));
		System.out.println(190569292 == countPartitions(100));

		List<Integer> p = new ArrayList<Integer>();
		Collections.addAll(p, 1, 1, 1, 1, 1);
		do {
			System.out.println(p);
		} while (nextPartition(p));

		long[][] part = partitionFunction(250);
		System.out.println(countPartitions(250));
	}
}
