package experimental;

import java.util.*;

public class RadixHeap {

	List<Long>[] b;
	int[] u;

	public RadixHeap(int n, int maxValue) {
		int B = 0;
		while ((1 << B) <= maxValue)
			++B;
		B += 2;
		b = new List[B];
		for (int i = 0; i < B; i++) {
			b[i] = new ArrayList<>();
		}
		u = new int[B];
		for (int i = 1; i < B - 1; i++) {
			u[i] = 1 << (i - 1);
		}
		u[B - 1] = Integer.MAX_VALUE;
	}

	public void add(int label, int value) {
		int i = b.length - 1;
		while (u[i] > value)
			--i;
		b[i].add(((long) label << 32) + value);
	}

	public long remove() {
		int i = 0;
		while (b[i].isEmpty())
			++i;
		int bestj = 0;
		for (int j = 1; j < b[i].size(); j++)
			if (b[i].get(bestj).intValue() > b[i].get(j).intValue())
				bestj = j;
		long res = b[i].remove(bestj);
		while (i < b.length && b[i].isEmpty())
			++i;
		if (i > 0 && i < b.length) {
			bestj = 0;
			for (int j = 1; j < b[i].size(); j++)
				if (b[i].get(bestj).intValue() > b[i].get(j).intValue())
					bestj = j;
			long minValue = b[i].get(bestj);
			u[0] = (int) minValue;
			u[1] = u[0] + 1;
			for (int j = 2; j <= i; j++) {
				u[j] = Math.min(u[j - 1] + (1 << (j - 2)), u[i + 1]);
			}
			for (long v : b[i]) {
				int j = i;
				while (u[j] > (int) v)
					--j;
				b[j].add(v);
			}
			b[i].clear();
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		int n = 120;
		int[] a = new int[n];
		Random rnd = new Random(1);
		for (int i = 0; i < n; i++) {
			a[i] = rnd.nextInt(100);
		}

		RadixHeap h = new RadixHeap(n, 1000);
		for (int i = 0; i < n; i++) {
			h.add(i, a[i]);
		}

		int[] b = new int[n];
		for (int i = 0; i < n; i++) {
			b[i] = (int) h.remove();
		}

		Arrays.sort(a);
		System.out.println(Arrays.equals(a, b));
	}
}
