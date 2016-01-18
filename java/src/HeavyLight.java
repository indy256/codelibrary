import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class HeavyLight {

	// true - values on vertices, false - values on edges
	static boolean VALUES_ON_VERTICES = true;

	int getNeutralValue() {
		return Integer.MIN_VALUE;
	}

	List<Integer>[] tree;
	SegmentTree segmentTree;
	int[] parent;
	int[] heavy;
	int[] depth;
	int[] pathRoot;
	int[] pos;

	public HeavyLight(List<Integer>[] tree) {
		this.tree = tree;
		int n = tree.length;
		this.segmentTree = new SegmentTree(n);

		parent = new int[n];
		heavy = new int[n];
		depth = new int[n];
		pathRoot = new int[n];
		pos = new int[n];

		Arrays.fill(heavy, -1);
		parent[0] = -1;
		depth[0] = 0;
		dfs(0);
		for (int u = 0, p = 0; u < n; u++) {
			if (parent[u] == -1 || heavy[parent[u]] != u) {
				for (int v = u; v != -1; v = heavy[v]) {
					pathRoot[v] = u;
					pos[v] = p++;
				}
			}
		}
	}

	int dfs(int u) {
		int size = 1;
		int maxSubtree = 0;
		for (int v : tree[u]) {
			if (v != parent[u]) {
				parent[v] = u;
				depth[v] = depth[u] + 1;
				int subtree = dfs(v);
				if (maxSubtree < subtree) {
					maxSubtree = subtree;
					heavy[u] = v;
				}
				size += subtree;
			}
		}
		return size;
	}

	public int query(int u, int v) {
		AtomicInteger res = new AtomicInteger(getNeutralValue()); // mutable integer
		processPath(u, v, (a, b) -> res.set(segmentTree.queryOperation(res.get(), segmentTree.query(a, b))));
		return res.get();
	}

	public void modify(int u, int v, int delta) {
		processPath(u, v, (a, b) -> segmentTree.modify(a, b, delta));
	}

	void processPath(int u, int v, BiConsumer<Integer, Integer> op) {
		for (; pathRoot[u] != pathRoot[v]; v = parent[pathRoot[v]]) {
			if (depth[pathRoot[u]] > depth[pathRoot[v]]) {
				int t = u;
				u = v;
				v = t;
			}
			op.accept(pos[pathRoot[v]], pos[v]);
		}
		if (!VALUES_ON_VERTICES && u == v) return;
		op.accept(Math.min(pos[u], pos[v]) + (VALUES_ON_VERTICES ? 0 : 1), Math.max(pos[u], pos[v]));
	}

	static class SegmentTree {
		// Modify the following 5 methods to implement your custom operations on the tree.
		// This example implements Add/Max operations. Operations like Add/Sum, Set/Max can also be implemented.
		int modifyOperation(int x, int y) {
			return x + y;
		}

		// query (or combine) operation
		int queryOperation(int leftValue, int rightValue) {
			return Math.max(leftValue, rightValue);
		}

		int deltaEffectOnSegment(int delta, int segmentLength) {
			if (delta == getNeutralDelta()) return getNeutralDelta();
			// Here you must write a fast equivalent of following slow code:
			// int result = delta;
			// for (int i = 1; i < segmentLength; i++) result = queryOperation(result, delta);
			// return result;
			return delta;
		}

		int getNeutralDelta() {
			return 0;
		}

		int getInitValue() {
			return 0;
		}

		// generic code
		int n;
		int[] value;
		int[] delta; // delta[i] affects value[i], delta[2*i+1] and delta[2*i+2]

		int joinValueWithDelta(int value, int delta) {
			if (delta == getNeutralDelta()) return value;
			return modifyOperation(value, delta);
		}

		int joinDeltas(int delta1, int delta2) {
			if (delta1 == getNeutralDelta()) return delta2;
			if (delta2 == getNeutralDelta()) return delta1;
			return modifyOperation(delta1, delta2);
		}

		void pushDelta(int root, int left, int right) {
			value[root] = joinValueWithDelta(value[root], deltaEffectOnSegment(delta[root], right - left + 1));
			delta[2 * root + 1] = joinDeltas(delta[2 * root + 1], delta[root]);
			delta[2 * root + 2] = joinDeltas(delta[2 * root + 2], delta[root]);
			delta[root] = getNeutralDelta();
		}

		public SegmentTree(int n) {
			this.n = n;
			value = new int[4 * n];
			delta = new int[4 * n];
			init(0, 0, n - 1);
		}

		void init(int root, int left, int right) {
			if (left == right) {
				value[root] = getInitValue();
				delta[root] = getNeutralDelta();
			} else {
				int mid = (left + right) >> 1;
				init(2 * root + 1, left, mid);
				init(2 * root + 2, mid + 1, right);
				value[root] = queryOperation(value[2 * root + 1], value[2 * root + 2]);
				delta[root] = getNeutralDelta();
			}
		}

		public int query(int from, int to) {
			return query(from, to, 0, 0, n - 1);
		}

		int query(int from, int to, int root, int left, int right) {
			if (from == left && to == right)
				return joinValueWithDelta(value[root], deltaEffectOnSegment(delta[root], right - left + 1));
			pushDelta(root, left, right);
			int mid = (left + right) >> 1;
			if (from <= mid && to > mid)
				return queryOperation(
						query(from, Math.min(to, mid), root * 2 + 1, left, mid),
						query(Math.max(from, mid + 1), to, root * 2 + 2, mid + 1, right));
			else if (from <= mid)
				return query(from, Math.min(to, mid), root * 2 + 1, left, mid);
			else if (to > mid)
				return query(Math.max(from, mid + 1), to, root * 2 + 2, mid + 1, right);
			else
				throw new RuntimeException("Incorrect query from " + from + " to " + to);
		}

		public void modify(int from, int to, int delta) {
			modify(from, to, delta, 0, 0, n - 1);
		}

		void modify(int from, int to, int delta, int root, int left, int right) {
			if (from == left && to == right) {
				this.delta[root] = joinDeltas(this.delta[root], delta);
				return;
			}
			pushDelta(root, left, right);
			int mid = (left + right) >> 1;
			if (from <= mid)
				modify(from, Math.min(to, mid), delta, 2 * root + 1, left, mid);
			if (to > mid)
				modify(Math.max(from, mid + 1), to, delta, 2 * root + 2, mid + 1, right);
			value[root] = queryOperation(
					joinValueWithDelta(value[2 * root + 1], deltaEffectOnSegment(this.delta[2 * root + 1], mid - left + 1)),
					joinValueWithDelta(value[2 * root + 2], deltaEffectOnSegment(this.delta[2 * root + 2], right - mid)));
		}
	}

	// Random test
	public static void main(String[] args) {
		Random rnd = new Random(1);
		VALUES_ON_VERTICES = true;
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(50) + 1;
			List<Integer>[] tree = getRandomTree(n, rnd);
			HeavyLight hl = new HeavyLight(tree);
			int[] x = new int[n];
			Arrays.fill(x, hl.segmentTree.getInitValue());
			for (int i = 0; i < 1000; i++) {
				int a = rnd.nextInt(n);
				int b = rnd.nextInt(n);
				List<Integer> path = new ArrayList<>();
				getPathFromAtoB(tree, a, b, -1, path);
				if (rnd.nextBoolean()) {
					int delta = rnd.nextInt(50) - 100;
					hl.modify(a, b, delta);
					for (int u : path)
						x[u] = hl.segmentTree.joinValueWithDelta(x[u], delta);
				} else {
					int res1 = hl.query(a, b);
					int res2 = hl.getNeutralValue();
					for (int u : path) {
						res2 = hl.segmentTree.queryOperation(res2, x[u]);
					}
					if (res1 != res2)
						throw new RuntimeException();
				}
			}
		}

		VALUES_ON_VERTICES = false;
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(50) + 1;
			List<Integer>[] tree = getRandomTree(n, rnd);
			HeavyLight hl = new HeavyLight(tree);
			Map<Long, Integer> x = new HashMap<>();
			for (int u = 0; u < tree.length; u++)
				for (int v : tree[u])
					x.put(edge(u, v), hl.segmentTree.getInitValue());
			for (int i = 0; i < 1000; i++) {
				int a = rnd.nextInt(n);
				int b = rnd.nextInt(n);
				List<Integer> path = new ArrayList<>();
				getPathFromAtoB(tree, a, b, -1, path);
				if (rnd.nextBoolean()) {
					int delta = rnd.nextInt(50) - 100;
					hl.modify(a, b, delta);
					for (int j = 0; j + 1 < path.size(); j++) {
						long key = edge(path.get(j), path.get(j + 1));
						x.put(key, hl.segmentTree.joinValueWithDelta(x.get(key), delta));
					}
				} else {
					int res1 = hl.query(a, b);
					int res2 = hl.getNeutralValue();
					for (int j = 0; j + 1 < path.size(); j++) {
						long key = edge(path.get(j), path.get(j + 1));
						res2 = hl.segmentTree.queryOperation(res2, x.get(key));
					}
					if (res1 != res2)
						throw new RuntimeException();
				}
			}
		}
		System.out.println("Test passed");
	}

	static long edge(int u, int v) {
		return ((long) Math.min(u, v) << 16) + Math.max(u, v);
	}

	static boolean getPathFromAtoB(List<Integer>[] tree, int a, int b, int p, List<Integer> path) {
		path.add(a);
		if (a == b)
			return true;
		for (int u : tree[a])
			if (u != p && getPathFromAtoB(tree, u, b, a, path))
				return true;
		path.remove(path.size() - 1);
		return false;
	}

	static List<Integer>[] getRandomTree(int n, Random rnd) {
		List<Integer>[] t = new List[n];
		for (int i = 0; i < n; i++)
			t[i] = new ArrayList<>();
		int[] p = new int[n];
		for (int i = 0, j; i < n; j = rnd.nextInt(i + 1), p[i] = p[j], p[j] = i, i++) ; // random permutation
		for (int i = 1; i < n; i++) {
			int parent = p[rnd.nextInt(i)];
			t[parent].add(p[i]);
			t[p[i]].add(parent);
		}
		return t;
	}
}