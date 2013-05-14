import java.util.*;

public class TreeCenters {

	// returns 1 or 2 tree centers
	// http://en.wikipedia.org/wiki/Graph_center
	public static List<Integer> findCenters(List<Integer>[] tree) {
		int n = tree.length;
		int cnt = n;
		List<Integer> a = new ArrayList<>();
		int[] degree = new int[n];
		for (int i = 0; i < n; i++) {
			degree[i] = tree[i].size();
			if (degree[i] <= 1) {
				a.add(i);
				--cnt;
			}
		}
		while (cnt > 0) {
			List<Integer> na = new ArrayList<>();
			for (int u : a) {
				for (int v : tree[u]) {
					if (--degree[v] == 1) {
						na.add(v);
						--cnt;
					}
				}
			}
			a = na;
		}
		return a;
	}

	// returns vertex that has all its subtrees sizes <= n/2
	public static int findCenter(List<Integer>[] tree, int u, int p) {
		int n = tree.length;
		int cnt = 1;
		boolean goodCenter = true;
		for (int v : tree[u]) {
			if (v == p) continue;
			int res = findCenter(tree, v, u);
			if (res >= 0)
				return res;
			int size = -res;
			goodCenter &= size <= n / 2;
			cnt += size;
		}
		goodCenter &= n - cnt <= n / 2;
		return goodCenter ? u : -cnt;
	}

	// Usage example
	public static void main(String[] args) {
		int n = 4;
		List<Integer>[] tree = new List[n];
		for (int i = 0; i < n; i++) {
			tree[i] = new ArrayList<>();
		}
		tree[3].add(0);
		tree[0].add(3);
		tree[3].add(1);
		tree[1].add(3);
		tree[3].add(2);
		tree[2].add(3);
		System.out.println(3 == findCenter(tree, 0, -1));
	}
}
