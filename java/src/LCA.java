import java.util.*;

public class LCA {

	int[] depth;
	int[] dfs_order;
	int cnt;
	int[] first;
	int[] minPos;
	int n;

	void dfs(List<Integer>[] tree, int u, int d) {
		depth[u] = d;
		dfs_order[cnt++] = u;
		for (int v : tree[u])
			if (depth[v] == -1) {
				dfs(tree, v, d + 1);
				dfs_order[cnt++] = u;
			}
	}

	void buildTree(int node, int left, int right) {
		if (left == right) {
			minPos[node] = dfs_order[left];
			return;
		}
		int mid = (left + right) >> 1;
		int n1 = node * 2 + 1;
		int n2 = node * 2 + 2;
		buildTree(n1, left, mid);
		buildTree(n2, mid + 1, right);
		minPos[node] = depth[minPos[n1]] < depth[minPos[n2]] ? minPos[n1] : minPos[n2];
	}

	public LCA(List<Integer>[] tree, int root) {
		int nodes = tree.length;
		depth = new int[nodes];
		Arrays.fill(depth, -1);

		n = 2 * nodes - 1;
		dfs_order = new int[n];
		cnt = 0;
		dfs(tree, root, 0);

		minPos = new int[4 * n];
		buildTree(0, 0, n - 1);

		first = new int[nodes];
		Arrays.fill(first, -1);
		for (int i = 0; i < dfs_order.length; i++) {
			int v = dfs_order[i];
			if (first[v] == -1)
				first[v] = i;
		}
	}

	public int lca(int a, int b) {
		return minPos(0, 0, n - 1, Math.min(first[a], first[b]), Math.max(first[a], first[b]));
	}

	int minPos(int node, int left, int right, int a, int b) {
		if (a > right || b < left)
			return -1;
		if (a <= left && right <= b)
			return minPos[node];
		int mid = (left + right) >> 1;
		int p1 = minPos(node * 2 + 1, left, mid, a, b);
		int p2 = minPos(node * 2 + 2, mid + 1, right, a, b);
		if (p1 == -1)
			return p2;
		if (p2 == -1)
			return p1;
		return depth[p1] < depth[p2] ? p1 : p2;
	}

	// Random test
	public static void main(String[] args) {
		Random rnd = new Random();
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(50) + 1;
			List<Integer>[] tree = getRandomTree(n, rnd);
			int[] depth = new int[n];
			Arrays.fill(depth, -1);
			int root = rnd.nextInt(n);
			calcDepth(tree, depth, root, 0);
			LCA q = new LCA(tree, root);
			for (int i = 0; i < 1000; i++) {
				int a = rnd.nextInt(n);
				int b = rnd.nextInt(n);
				List<Integer> path = new ArrayList<Integer>();
				getPath(tree, a, b, -1, path);
				int res1 = q.lca(a, b);
				int res2 = a;
				for (int u : path)
					if (depth[res2] > depth[u])
						res2 = u;
				if (res1 != res2)
					throw new RuntimeException("error");
			}
		}
		System.out.println("Test passed");
	}

	static boolean getPath(List<Integer>[] tree, int a, int b, int p, List<Integer> path) {
		path.add(a);
		if (a == b)
			return true;
		for (int u : tree[a])
			if (u != p && getPath(tree, u, b, a, path))
				return true;
		path.remove(path.size() - 1);
		return false;
	}

	static List<Integer>[] getRandomTree(int n, Random rnd) {
		List<Integer>[] t = new List[n];
		for (int i = 0; i < n; i++)
			t[i] = new ArrayList<Integer>();
		List<Integer> p = new ArrayList<Integer>();
		for (int i = 0; i < n; i++)
			p.add(i);
		Collections.shuffle(p, rnd);
		for (int i = 1; i < n; i++) {
			int child = p.get(i);
			int parent = p.get(rnd.nextInt(i));
			t[parent].add(child);
			t[child].add(parent);
		}
		return t;
	}

	static void calcDepth(List<Integer>[] tree, int[] depth, int u, int d) {
		depth[u] = d;
		for (int v : tree[u])
			if (depth[v] == -1)
				calcDepth(tree, depth, v, d + 1);
	}
}
