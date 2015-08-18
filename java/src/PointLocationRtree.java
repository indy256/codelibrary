import java.util.*;

// https://en.wikipedia.org/wiki/R-tree
public class PointLocationRtree {

	static class Polygon {
		final int[] x;
		final int[] y;
		final int centerX;
		final int centerY;
		final int id;

		public Polygon(int[] x, int[] y, int id) {
			this.x = x;
			this.y = y;
			this.centerX = average(x);
			this.centerY = average(y);
			this.id = id;
		}

		static int average(int[] a) {
			long res = 0;
			for (int v : a)
				res += v;
			return (int) (res / a.length);
		}
	}

	static class RTree {
		final Polygon[] polygons;
		final int[] minx, maxx, miny, maxy;

		public RTree(Polygon[] polygons) {
			int n = polygons.length;
			this.polygons = polygons;
			minx = new int[n];
			maxx = new int[n];
			miny = new int[n];
			maxy = new int[n];
			Arrays.fill(minx, Integer.MAX_VALUE);
			Arrays.fill(maxx, Integer.MIN_VALUE);
			Arrays.fill(miny, Integer.MAX_VALUE);
			Arrays.fill(maxy, Integer.MIN_VALUE);
			build(0, n, true);
		}

		void build(int low, int high, boolean divX) {
			if (low >= high)
				return;
			int mid = (low + high) >>> 1;
			nth_element(polygons, low, high, mid, divX);

			for (int i = low; i < high; i++) {
				for (int j = 0; j < polygons[i].x.length; j++) {
					minx[mid] = Math.min(minx[mid], polygons[i].x[j]);
					miny[mid] = Math.min(miny[mid], polygons[i].y[j]);
					maxx[mid] = Math.max(maxx[mid], polygons[i].x[j]);
					maxy[mid] = Math.max(maxy[mid], polygons[i].y[j]);
				}
			}

			build(low, mid, !divX);
			build(mid + 1, high, !divX);
		}

		// See: http://www.cplusplus.com/reference/algorithm/nth_element
		static void nth_element(Polygon[] a, int low, int high, int n, boolean divX) {
			while (true) {
				int k = randomizedPartition(a, low, high, divX);
				if (n < k)
					high = k;
				else if (n > k)
					low = k + 1;
				else
					return;
			}
		}

		static int randomizedPartition(Polygon[] a, int low, int high, boolean divX) {
			swap(a, low + random.nextInt(high - low), high - 1);
			double v = divX ? a[high - 1].centerX : a[high - 1].centerY;
			int i = low - 1;
			for (int j = low; j < high; j++)
				if (divX ? a[j].centerX <= v : a[j].centerY <= v)
					swap(a, ++i, j);
			return i;
		}

		static void swap(Polygon[] a, int i, int j) {
			Polygon t = a[i];
			a[i] = a[j];
			a[j] = t;
		}

		int foundPolygon;

		public int findEnclosingPolygon(int x, int y) {
			foundPolygon = -1;
			findEnclosingPolygon(0, polygons.length, x, y, true);
			return foundPolygon;
		}

		void findEnclosingPolygon(int low, int high, int x, int y, boolean divX) {
			if (low >= high)
				return;
			int mid = (low + high) >>> 1;
			if (x < minx[mid] || x > maxx[mid] || y < miny[mid] || y > maxy[mid])
				return;
			int location = pointInPolygon(x, y, polygons[mid].x, polygons[mid].y);
			if (location != -1) {
				foundPolygon = polygons[mid].id;
				return;
			}
			findEnclosingPolygon(low, mid, x, y, !divX);
			findEnclosingPolygon(mid + 1, high, x, y, !divX);
		}
	}

	public static int[] locatePoints(int[][] polygonX, int[][] polygonY, int[] qx, int[] qy) {
		int n = polygonX.length;
		Polygon[] polygons = new Polygon[n];
		for (int i = 0; i < n; i++) {
			polygons[i] = new Polygon(polygonX[i], polygonY[i], i);
		}
		RTree rTree = new RTree(polygons);
		int[] res = new int[qx.length];
		for (int i = 0; i < qx.length; i++) {
			res[i] = rTree.findEnclosingPolygon(qx[i], qy[i]);
		}
		return res;
	}

	static Random random = new Random(1);

	// random test
	public static void main(String[] args) {
		for (int step = 0; step < 10_000; step++) {
			int range = 10;
			int R = 3;
			int C = 3;
			int[][] polygonX = new int[R * C][];
			int[][] polygonY = new int[R * C][];
			for (int r = 0; r < R; r++) {
				for (int c = 0; c < C; c++) {
					int n = random.nextInt(10) + 3;
					int[][] xy = getRandomPolygon(n, range, range);
					polygonX[r * C + c] = xy[0];
					polygonY[r * C + c] = xy[1];
					for (int i = 0; i < n; i++) {
						polygonX[r * C + c][i] += c * (range + 1);
						polygonY[r * C + c][i] += r * (range + 1);
					}
				}
			}
			int queries = random.nextInt(10) + 1;
			queries = 1;
			int[] qx = new int[queries];
			int[] qy = new int[queries];
			int[] res1 = new int[queries];
			Arrays.fill(res1, -1);
			for (int i = 0; i < queries; i++) {
				qx[i] = random.nextInt((C + 2) * (range + 1)) - range;
				qy[i] = random.nextInt((R + 2) * (range + 1)) - range;
				for (int j = 0; j < polygonX.length; j++) {
					int v = pointInPolygon(qx[i], qy[i], polygonX[j], polygonY[j]);
					if (v >= 0) {
						res1[i] = j;
						break;
					}
				}
			}
			int[] res2 = locatePoints(polygonX, polygonY, qx, qy);
			if (!Arrays.equals(res1, res2))
				throw new RuntimeException();
		}
	}

	static int pointInPolygon(int qx, int qy, int[] x, int[] y) {
		int n = x.length;
		int cnt = 0;
		for (int i = 0, j = n - 1; i < n; j = i++) {
			if (y[i] == qy && (x[i] == qx || y[j] == qy && (x[i] <= qx || x[j] <= qx) && (x[i] >= qx || x[j] >= qx)))
				return 0; // boundary
			if ((y[i] > qy) != (y[j] > qy)) {
				long det = (long) (x[i] - qx) * (y[j] - qy) - (long) (x[j] - qx) * (y[i] - qy);
				if (det == 0)
					return 0; // boundary
				if ((det > 0) != (y[j] - y[i] > 0))
					++cnt;
			}
		}
		return cnt % 2 == 0 ? -1 /* exterior */ : 1 /* interior */;
	}

	static int[][] getRandomPolygon(int n, int maxWidth, int maxHeight) {
		int[] x = new int[n];
		int[] y = new int[n];
		int[] p = new int[n];
		while (true) {
			for (int i = 0; i < n; i++) {
				x[i] = random.nextInt(maxWidth);
				y[i] = random.nextInt(maxHeight);
				p[i] = i;
			}
			for (boolean improved = true; improved; ) {
				improved = false;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						int[] p1 = p.clone();
						reverse(p1, i, j);
						if (len(x, y, p) > len(x, y, p1)) {
							p = p1;
							improved = true;
						}
					}
				}
			}
			int[] tx = x.clone();
			int[] ty = y.clone();
			for (int i = 0; i < n; i++) {
				x[i] = tx[p[i]];
				y[i] = ty[p[i]];
			}
			boolean ok = true;
			for (int i = 0; i < n; i++) {
				long x1 = x[(i - 1 + n) % n] - x[i];
				long y1 = y[(i - 1 + n) % n] - y[i];
				long x2 = x[(i + 1) % n] - x[i];
				long y2 = y[(i + 1) % n] - y[i];
				ok &= x1 * y2 - x2 * y1 != 0 || x1 * x2 + y1 * y2 <= 0;
			}
			for (int i2 = 0, i1 = p.length - 1; i2 < p.length; i1 = i2++)
				for (int j2 = 0, j1 = p.length - 1; j2 < p.length; j1 = j2++)
					ok &= i1 == j1 || i1 == j2 || i2 == j1
							|| !isCrossOrTouchIntersect(x[i1], y[i1], x[i2], y[i2], x[j1], y[j1], x[j2], y[j2]);
			if (ok)
				return new int[][]{x, y};
		}
	}

	// http://en.wikipedia.org/wiki/2-opt
	static void reverse(int[] p, int i, int j) {
		int n = p.length;
		// reverse order from i to j
		while (i != j) {
			int t = p[j];
			p[j] = p[i];
			p[i] = t;
			i = (i + 1) % n;
			if (i == j) break;
			j = (j - 1 + n) % n;
		}
	}

	static double len(int[] x, int[] y, int[] p) {
		double res = 0;
		for (int i = 0, j = p.length - 1; i < p.length; j = i++) {
			double dx = x[p[i]] - x[p[j]];
			double dy = y[p[i]] - y[p[j]];
			res += Math.sqrt(dx * dx + dy * dy);
		}
		return res;
	}

	static boolean isCrossOrTouchIntersect(long x1, long y1, long x2, long y2, long x3, long y3, long x4, long y4) {
		if (Math.max(x1, x2) < Math.min(x3, x4) || Math.max(x3, x4) < Math.min(x1, x2)
				|| Math.max(y1, y2) < Math.min(y3, y4) || Math.max(y3, y4) < Math.min(y1, y2))
			return false;
		long z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
		long z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);
		long z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
		long z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);
		return (z1 <= 0 || z2 <= 0) && (z1 >= 0 || z2 >= 0) && (z3 <= 0 || z4 <= 0) && (z3 >= 0 || z4 >= 0);
	}
}
