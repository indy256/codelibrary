import java.util.*;

public class DistinctQuerySqrt {

	static int solveSlow(int[] cnt, int[] a, int haveLeft, int haveRight, int needLeft, int needRight) {
		int res = 0;
		for (int i = haveRight + 1; i <= needRight; ++i) {
			res += cnt[a[i]] == 0 ? 1 : 0;
			++cnt[a[i]];
		}
		for (int i = haveLeft; i < needLeft; ++i) {
			res += cnt[a[i]] == 1 ? -1 : 0;
			--cnt[a[i]];
		}
		for (int i = haveRight + 1; i <= needRight; ++i) {
			--cnt[a[i]];
		}
		for (int i = haveLeft; i < needLeft; ++i) {
			++cnt[a[i]];
		}
		return res;
	}

	static int[] solve(int[] a, int[][] queries) {
		int n = a.length;
		Map<Integer, Integer> m = new HashMap<>();
		int id = 0;
		for (int i = 0; i < n; i++) {
			if (!m.containsKey(a[i]))
				m.put(a[i], id++);
			a[i] = m.get(a[i]);
		}
		int blockSize = (int) Math.sqrt(n);
		int blocks = (n - 1) / blockSize + 1;
		int[][] head = new int[blocks][blocks];
		for (int i = 0; i < blocks; i++)
			Arrays.fill(head[i], -1);
		int t = queries.length;
		int[] left = new int[t];
		int[] right = new int[t];
		int[] next = new int[t];
		int[] res = new int[t];
		for (int i = 0; i < t; i++) {
			left[i] = queries[i][0];
			right[i] = queries[i][1];
			int lb = left[i] / blockSize;
			int rb = right[i] / blockSize;
			next[i] = head[lb][rb];
			head[lb][rb] = i;
		}
		for (int leftBlock = 0; leftBlock < blocks; leftBlock++) {
			int[] cnt = new int[id];
			int curAnswer = 0;
			for (int rightBlock = leftBlock; rightBlock < blocks; rightBlock++) {
				for (int i = head[leftBlock][rightBlock]; i >= 0; i = next[i]) {
					res[i] = curAnswer
							+ solveSlow(cnt, a, leftBlock * blockSize, rightBlock * blockSize - 1, left[i], right[i]);
				}
				if (rightBlock + 1 < blocks) {
					for (int i = 0; i < blockSize; i++) {
						int cur = a[rightBlock * blockSize + i];
						curAnswer += cnt[cur] == 0 ? 1 : 0;
						++cnt[cur];
					}
				}
			}
		}
		return res;
	}

	public static void main(String[] args) {
		int[] a = { 1, 2, 3, 4 };
		int[][] queries = { { 3, 3 } };
		int[] res = solve(a, queries);
		System.out.println(Arrays.toString(res));
	}
}
