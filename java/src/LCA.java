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
		for (int v : tree[u]) {
			if (depth[v] == -1) {
				dfs(tree, v, d + 1);
				dfs_order[cnt++] = u;
			}
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

	public static void main(String[] args) {
		List<Integer>[] tree = new List[7];
		for (int i = 0; i < tree.length; i++) {
			tree[i] = new ArrayList<Integer>();
		}
		tree[0].add(1);
		tree[0].add(2);
		tree[1].add(3);
		tree[1].add(4);
		tree[4].add(5);
		tree[4].add(6);
		LCA q = new LCA(tree, 0);

		System.out.println(4 == q.lca(5, 6));
		System.out.println(1 == q.lca(3, 5));
	}
}
