package experimental;

import java.util.*;

public class RadixHeap1 {

	List<Long>[] b;
	int[] u;

	public RadixHeap1(int n, int maxValue) {
		int B = 0;
		while (1 << B <= maxValue)
			++B;
		++B;
		b = new List[B];
		for (int i = 0; i < b.length; i++) {
			b[i] = new ArrayList<>();
		}
		u = new int[B];
		for (int i = 0; i + 1 < B; i++) {
			u[i + 1] = 1 << i;
		}
		u[B - 1] = Integer.MAX_VALUE;
	}

	public void add(int label, int value) {
		int i = b.length - 1;
		while (u[i] > value)
			--i;
		b[i].add(((long) label * 1000) + value);
	}

	public long remove() {
		int i = 0;
		while (b[i].isEmpty())
			++i;
		int bestj = 0;
		for (int j = 1; j < b[i].size(); j++)
			if (b[i].get(bestj) % 1000 > b[i].get(j) % 1000)
				bestj = j;
		long res = b[i].remove(bestj);
		i = 0;
		while (i < b.length && b[i].isEmpty())
			++i;
		if (i == b.length) {
			return res;
		}
		if (i > 0) {
			bestj = 0;
			for (int j = 1; j < b[i].size(); j++)
				if (b[i].get(bestj) % 1000 > b[i].get(j) % 1000)
					bestj = j;
			long minValue = b[i].get(bestj);
			u[0] = (int) (minValue % 1000);
			u[1] = u[0] + 1;
			for (int j = 2; j <= i; j++) {
				u[j] = Math.min(u[j - 1] + (1 << (j - 2)), u[i + 1]);
			}
			int j = 0;
			for (long v : b[i]) {
				while ((int) v % 1000 >= u[j + 1])
					++j;
				b[j].add(v);
			}
			b[i].clear();
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {

		RadixHeap1 h = new RadixHeap1(10, 100);
		h.add(1, 3);
		h.add(2, 2);
		h.add(4, 0);
		h.add(3, 1);

		long a = h.remove();
		System.out.println(a / 1000);

		a = h.remove();
		System.out.println(a / 1000);

		a = h.remove();
		System.out.println(a / 1000);

		a = h.remove();
		System.out.println(a / 1000);
	}

}
