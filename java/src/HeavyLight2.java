import java.util.*;
import java.util.stream.Stream;

public class HeavyLight2 {

	// true - values on vertices, false - values on edges
	static boolean VALUES_ON_VERTICES = true;

	// Modify the following 6 methods to implement your custom operations on the tree.
	// This example implements Add/Max operations. Operations like Add/Sum, Set/Max can also be implemented.
	static int modifyOperation(int x, int y) {
		return x + y;
	}

	// query (or combine) operation
	static int queryOperation(int leftValue, int rightValue) {
		return Math.max(leftValue, rightValue);
	}

	static int deltaEffectOnSegment(int delta, int segmentLength) {
		if (delta == getNeutralDelta()) return getNeutralDelta();
		// Here you must write a fast equivalent of following slow code:
		// int result = delta;
		// for (int i = 1; i < segmentLength; i++) result = queryOperation(result, delta);
		// return result;
		return delta;
	}

	static int getNeutralDelta() {
		return 0;
	}

	static int getInitValue() {
		return 0;
	}

	static int getNeutralValue() {
		return Integer.MIN_VALUE;
	}

	// generic code
	static int joinValueWithDelta(int value, int delta) {
		if (delta == getNeutralDelta()) return value;
		return modifyOperation(value, delta);
	}

	static int joinDeltas(int delta1, int delta2) {
		if (delta1 == getNeutralDelta()) return delta2;
		if (delta2 == getNeutralDelta()) return delta1;
		return modifyOperation(delta1, delta2);
	}

	int[][] value;
	int[][] delta; // delta[i] affects value[i], delta[2*i+1] and delta[2*i+2]
	int[][] len;

	List<Integer>[] tree;
	int[] size;
	int[] parent;
	int[] tin;
	int[] tout;
	int time;
	int[] path;
	int[] pathSize;
	int[] pathPos;
	int[] pathRoot;
	int pathCount;

	public HeavyLight2(List<Integer>[] tree) {
		this.tree = tree;
		int n = tree.length;

		size = new int[n];
		parent = new int[n];
		tin = new int[n];
		tout = new int[n];
		calcSizeParentTinTout(0, -1);

		path = new int[n];
		pathSize = new int[n];
		pathPos = new int[n];
		pathRoot = new int[n];
		buildPaths(0, newPath(0));

		value = new int[pathCount][];
		delta = new int[pathCount][];
		len = new int[pathCount][];

		for (int i = 0; i < pathCount; i++) {
			int m = pathSize[i];

			value[i] = new int[2 * m];
			for (int j = 0; j < m; j++)
				value[i][j + m] = getInitValue();
			for (int j = 2 * m - 1; j > 1; j -= 2)
				value[i][j >> 1] = queryOperation(value[i][j], value[i][j ^ 1]);

			delta[i] = new int[2 * m];
			Arrays.fill(delta[i], getNeutralDelta());

			len[i] = new int[2 * m];
			Arrays.fill(len[i], m, 2 * m, 1);
			for (int j = 2 * m - 1; j > 1; j -= 2)
				len[i][j >> 1] = len[i][j] + len[i][j ^ 1];
		}
	}

	void calcSizeParentTinTout(int u, int p) {
		tin[u] = time++;
		parent[u] = p;
		size[u] = 1;
		for (int v : tree[u])
			if (v != p) {
				calcSizeParentTinTout(v, u);
				size[u] += size[v];
			}
		tout[u] = time++;
	}

	int newPath(int u) {
		pathRoot[pathCount] = u;
		return pathCount++;
	}

	void buildPaths(int u, int path) {
		this.path[u] = path;
		pathPos[u] = pathSize[path]++;
		for (int v : tree[u]) {
			if (v != parent[u])
				buildPaths(v, 2 * size[v] >= size[u] ? path : newPath(v));
		}
	}

	void pushDelta(int path, int i) {
		int d = 0;
		for (; (i >> d) > 0; d++)
			;
		for (d -= 2; d >= 0; d--) {
			int x = i >> d;
			value[path][x >> 1] = joinValueWithDelta0(path, x >> 1);
			delta[path][x] = joinDeltas(delta[path][x], delta[path][x >> 1]);
			delta[path][x ^ 1] = joinDeltas(delta[path][x ^ 1], delta[path][x >> 1]);
			delta[path][x >> 1] = getNeutralDelta();
		}
	}

	int joinValueWithDelta0(int path, int i) {
		return joinValueWithDelta(value[path][i], deltaEffectOnSegment(delta[path][i], len[path][i]));
	}

	int queryPath(int path, int from, int to) {
		from += value[path].length >> 1;
		to += value[path].length >> 1;
		pushDelta(path, from);
		pushDelta(path, to);
		int res = getNeutralValue();
		for (; from <= to; from = (from + 1) >> 1, to = (to - 1) >> 1) {
			if ((from & 1) != 0)
				res = queryOperation(res, joinValueWithDelta0(path, from));
			if ((to & 1) == 0)
				res = queryOperation(res, joinValueWithDelta0(path, to));
		}
		return res;
	}

	void modifyPath(int path, int from, int to, int delta) {
		from += value[path].length >> 1;
		to += value[path].length >> 1;
		pushDelta(path, from);
		pushDelta(path, to);
		int ta = -1;
		int tb = -1;
		for (; from <= to; from = (from + 1) >> 1, to = (to - 1) >> 1) {
			if ((from & 1) != 0) {
				this.delta[path][from] = joinDeltas(this.delta[path][from], delta);
				if (ta == -1)
					ta = from;
			}
			if ((to & 1) == 0) {
				this.delta[path][to] = joinDeltas(this.delta[path][to], delta);
				if (tb == -1)
					tb = to;
			}
		}
		for (int i = ta; i > 1; i >>= 1)
			value[path][i >> 1] = queryOperation(joinValueWithDelta0(path, i), joinValueWithDelta0(path, i ^ 1));
		for (int i = tb; i > 1; i >>= 1)
			value[path][i >> 1] = queryOperation(joinValueWithDelta0(path, i), joinValueWithDelta0(path, i ^ 1));
	}

	boolean isAncestor(int p, int ch) {
		return tin[p] <= tin[ch] && tout[ch] <= tout[p];
	}

	public int query(int a, int b) {
		int res = getNeutralValue();
		for (int root; !isAncestor(root = pathRoot[path[a]], b); a = parent[root])
			res = queryOperation(res, queryPath(path[a], 0, pathPos[a]));
		for (int root; !isAncestor(root = pathRoot[path[b]], a); b = parent[root])
			res = queryOperation(res, queryPath(path[b], 0, pathPos[b]));
		if (!VALUES_ON_VERTICES && a == b)
			return res;
		return queryOperation(
				res,
				queryPath(path[a], Math.min(pathPos[a], pathPos[b]) + (VALUES_ON_VERTICES ? 0 : 1),
						Math.max(pathPos[a], pathPos[b])));
	}

	public void modify(int a, int b, int delta) {
		for (int root; !isAncestor(root = pathRoot[path[a]], b); a = parent[root])
			modifyPath(path[a], 0, pathPos[a], delta);
		for (int root; !isAncestor(root = pathRoot[path[b]], a); b = parent[root])
			modifyPath(path[b], 0, pathPos[b], delta);
		if (!VALUES_ON_VERTICES && a == b)
			return;
		modifyPath(path[a], Math.min(pathPos[a], pathPos[b]) + (VALUES_ON_VERTICES ? 0 : 1),
				Math.max(pathPos[a], pathPos[b]), delta);
	}

	// Random test
	public static void main(String[] args) {
		Random rnd = new Random(1);
		VALUES_ON_VERTICES = true;
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(50) + 1;
			List<Integer>[] tree = getRandomTree(n, rnd);
			int[] x = new int[n];
			Arrays.fill(x, getInitValue());
			HeavyLight2 hl = new HeavyLight2(tree);
			for (int i = 0; i < 1000; i++) {
				int a = rnd.nextInt(n);
				int b = rnd.nextInt(n);
				List<Integer> path = new ArrayList<>();
				getPathFromAtoB(tree, a, b, -1, path);
				if (rnd.nextBoolean()) {
					int delta = rnd.nextInt(50) - 100;
					hl.modify(a, b, delta);
					for (int u : path)
						x[u] = joinValueWithDelta(x[u], delta);
				} else {
					int res1 = hl.query(a, b);
					int res2 = getNeutralValue();
					for (int u : path)
						res2 = queryOperation(res2, x[u]);
					if (res1 != res2)
						throw new RuntimeException();
				}
			}
		}
		VALUES_ON_VERTICES = false;
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(50) + 1;
			List<Integer>[] tree = getRandomTree(n, rnd);
			Map<Long, Integer> x = new HashMap<>();
			for (int u = 0; u < tree.length; u++)
				for (int v : tree[u])
					x.put(edge(u, v), getInitValue());
			HeavyLight2 hl = new HeavyLight2(tree);
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
						x.put(key, joinValueWithDelta(x.get(key), delta));
					}
				} else {
					int res1 = hl.query(a, b);
					int res2 = getNeutralValue();
					for (int j = 0; j + 1 < path.size(); j++) {
						long key = edge(path.get(j), path.get(j + 1));
						res2 = queryOperation(res2, x.get(key));
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
		List<Integer>[] t = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
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
