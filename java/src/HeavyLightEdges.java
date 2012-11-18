import java.util.*;

public class HeavyLightEdges {

	// specific code
	static final int INIT_VALUE = 0;
	static final int NEUTRAL_VALUE = Integer.MIN_VALUE;
	static final int NEUTRAL_DELTA = 0;

	static int joinValues(int leftValue, int rightValue) {
		return Math.max(leftValue, rightValue);
	}

	static int joinDeltas(int oldDelta, int newDelta) {
		return oldDelta + newDelta;
	}

	static int joinValueWithDelta(int value, int delta, int length) {
		return value + delta;
	}

	// generic code
	int[][] value;
	int[][] delta;
	int[][] len;

	List<Integer>[] graph;
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

	public HeavyLightEdges(List<Integer>[] graph) {
		this.graph = graph;
		int n = graph.length;

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
			Arrays.fill(value[i], INIT_VALUE);
			delta[i] = new int[2 * m];
			Arrays.fill(delta[i], NEUTRAL_DELTA);
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
		for (int v : graph[u])
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
		for (int v : graph[u]) {
			if (v != parent[u])
				buildPaths(v, 2 * size[v] >= size[u] ? path : newPath(v));
		}
	}

	void applyDelta(int path, int i, int delta) {
		value[path][i] = joinValueWithDelta(value[path][i], delta, len[path][i]);
		this.delta[path][i] = joinDeltas(this.delta[path][i], delta);
	}

	void pushDelta(int path, int i) {
		int d = 0;
		for (; (i >> d) > 0; d++)
			;
		for (d -= 2; d >= 0; d--) {
			int x = i >> d;
			applyDelta(path, x, delta[path][x >> 1]);
			applyDelta(path, x ^ 1, delta[path][x >> 1]);
			delta[path][x >> 1] = NEUTRAL_DELTA;
		}
	}

	void modifyPath(int path, int a, int b, int delta) {
		a += value[path].length >> 1;
		b += value[path].length >> 1;
		pushDelta(path, a);
		pushDelta(path, b);
		int ta = -1;
		int tb = -1;
		for (; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			if ((a & 1) != 0) {
				applyDelta(path, a, delta);
				if (ta == -1)
					ta = a;
			}
			if ((b & 1) == 0) {
				applyDelta(path, b, delta);
				if (tb == -1)
					tb = b;
			}
		}
		for (int i = ta; i > 1; i >>= 1)
			value[path][i >> 1] = joinValues(value[path][i], value[path][i ^ 1]);
		for (int i = tb; i > 1; i >>= 1)
			value[path][i >> 1] = joinValues(value[path][i], value[path][i ^ 1]);
	}

	int queryPath(int path, int a, int b) {
		a += value[path].length >> 1;
		b += value[path].length >> 1;
		pushDelta(path, a);
		pushDelta(path, b);
		int res = NEUTRAL_VALUE;
		for (; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			if ((a & 1) != 0)
				res = joinValues(res, value[path][a]);
			if ((b & 1) == 0)
				res = joinValues(res, value[path][b]);
		}
		return res;
	}

	boolean isAncestor(int p, int ch) {
		return tin[p] <= tin[ch] && tout[ch] <= tout[p];
	}

	public void modify(int a, int b, int delta) {
		for (int root; !isAncestor(root = pathRoot[path[a]], b); a = parent[root])
			modifyPath(path[a], 0, pathPos[a], delta);
		for (int root; !isAncestor(root = pathRoot[path[b]], a); b = parent[root])
			modifyPath(path[b], 0, pathPos[b], delta);
		modifyPath(path[a], Math.min(pathPos[a], pathPos[b]), Math.max(pathPos[a], pathPos[b]), delta);
	}

	public int query(int a, int b) {
		int res = NEUTRAL_VALUE;
		for (int root; !isAncestor(root = pathRoot[path[a]], b); a = parent[root])
			res = joinValues(res, queryPath(path[a], 0, pathPos[a]));
		for (int root; !isAncestor(root = pathRoot[path[b]], a); b = parent[root])
			res = joinValues(res, queryPath(path[b], 0, pathPos[b]));
		return joinValues(res, queryPath(path[a], Math.min(pathPos[a], pathPos[b]), Math.max(pathPos[a], pathPos[b])));
	}

	// Random test
	public static void main(String[] args) {
		Random rnd = new Random();
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(50) + 1;
			List<Integer>[] tree = getRandomTree(n, rnd);
			int[] x = new int[n];
			Arrays.fill(x, INIT_VALUE);
			HeavyLightEdges hl = new HeavyLightEdges(tree);
			for (int i = 0; i < 1000; i++) {
				int a = rnd.nextInt(n);
				int b = rnd.nextInt(n);
				List<Integer> path = new ArrayList<Integer>();
				getPath(tree, a, b, -1, path);
				if (rnd.nextBoolean()) {
					int delta = rnd.nextInt(50) - 100;
					hl.modify(a, b, delta);
					for (int u : path)
						x[u] = joinValueWithDelta(x[u], delta, 1);
				} else {
					int res1 = hl.query(a, b);
					int res2 = NEUTRAL_VALUE;
					for (int u : path)
						res2 = joinValues(res2, x[u]);
					if (res1 != res2) {
						System.err.println("error");
						return;
					}
				}
			}
		}
		System.out.println("Test passed");
	}

	static boolean getPath(List<Integer>[] tree, int a, int b, int p, List<Integer> path) {
		path.add(a);
		if (a == b)
			return true;
		for (int u : tree[a]) {
			if (u != p && getPath(tree, u, b, a, path))
				return true;
		}
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
}
