import java.util.*;

// https://en.wikipedia.org/wiki/Point_location
public class PointLocation {

	public static int[] locatePoints(int[][] polygonX, int[][] polygonY, int[] qx, int[] qy) {
		// preprocessing
		Map<Long, Integer> endPoints = new HashMap<>();
		Map<Integer, NavigableSet<Segment>> verticalSegments = new HashMap<>();
		List<Event> events = new ArrayList<>();
		for (int k = 0; k < polygonX.length; ++k) {
			int n = polygonX[k].length;
			for (int i = 0, j = n - 1; i < n; j = i++) {
				endPoints.put(((long) polygonX[k][i] << 32) + polygonY[k][i], k);
				Segment s = new Segment(polygonX[k][i], polygonY[k][i], polygonX[k][j], polygonY[k][j], k);
				if (s.x1 != s.x2) {
					events.add(new Event(s.x1, s.y1, 1, s));
					events.add(new Event(s.x2, s.y2, -1, s));
				} else {
					if (!verticalSegments.containsKey(s.x1)) {
						verticalSegments.put(s.x1, new TreeSet<>(segmentComparator));
					}
					verticalSegments.get(s.x1).add(s);
				}
			}
		}
		Collections.sort(events, eventComparator);
		int[] eventsX = new int[events.size()];
		Treap[] treaps = new Treap[events.size()];
		int cnt = 0;
		Treap treap = null;
		for (Event event : events) {
			if (cnt == 0 || eventsX[cnt - 1] < event.x)
				++cnt;
			if (event.type == 1) {
				treap = insert(treap, event.segment);
			} else if (event.type == -1) {
				treap = remove(treap, event.segment);
			}
			eventsX[cnt - 1] = event.x;
			treaps[cnt - 1] = treap;
		}
		// answering queries
		int[] res = new int[qx.length];
		for (int i = 0; i < qx.length; i++) {
			Integer polygonId = endPoints.get(((long) qx[i] << 32) + qy[i]);
			if (polygonId != null) {
				res[i] = polygonId;
				continue;
			}
			NavigableSet<Segment> vSegments = verticalSegments.get(qx[i]);
			if (vSegments != null) {
				SortedSet<Segment> head = vSegments.headSet(new Segment(qx[i], qy[i], qx[i], qy[i], 0), true);
				if (!head.isEmpty() && head.last().contains(qx[i], qy[i])) {
					res[i] = head.last().polygonId;
					continue;
				}
			}

			int lo = Arrays.binarySearch(eventsX, 0, cnt, qx[i]);
			if (lo < 0) lo = -lo - 2;
			if (lo == -1 || lo == cnt - 1) {
				res[i] = -1;
				continue;
			}

			Segment minRight = new Segment(qx[i], qy[i], qx[i], qy[i], 0);
			TreapPair treapPair = split(treaps[lo], minRight, true);
			Treap max = max(treapPair.left);
			Treap min = min(treapPair.right);
			if (min != null && min.key.contains(qx[i], qy[i])) {
				res[i] = min.key.polygonId;
			} else if (max != null && max.key.contains(qx[i], qy[i])) {
				res[i] = max.key.polygonId;
			} else if (getSize(treapPair.left) % 2 == 1) {
				res[i] = max.key.polygonId;
			} else {
				res[i] = -1;
			}
		}
		return res;
	}

	static final Comparator<Segment> segmentComparator = (a, b) -> {
		if (a.x1 == b.x1 && a.y1 == b.y1) {
			long v = cross(a.x1, a.y1, a.x2, a.y2, b.x2, b.y2);
			if (v != 0)
				return v > 0 ? -1 : 1;
		} else if (a.x1 < b.x1) {
			long v = cross(a.x1, a.y1, a.x2, a.y2, b.x1, b.y1);
			if (v != 0)
				return v > 0 ? -1 : 1;
		} else {
			long v = cross(b.x1, b.y1, b.x2, b.y2, a.x1, a.y1);
			if (v != 0)
				return v < 0 ? -1 : 1;
		}
		return Integer.compare(a.y1, b.y1);
	};

	static class Segment {
		final int x1, y1, x2, y2, polygonId;

		public Segment(int x1, int y1, int x2, int y2, int polygonId) {
			if (x1 < x2 || x1 == x2 && y1 < y2) {
				this.x1 = x1;
				this.y1 = y1;
				this.x2 = x2;
				this.y2 = y2;
			} else {
				this.x1 = x2;
				this.y1 = y2;
				this.x2 = x1;
				this.y2 = y1;
			}
			this.polygonId = polygonId;
		}

		public boolean contains(int x, int y) {
			long a = y2 - y1;
			long b = x1 - x2;
			long c = -a * x1 - b * y1;
			return x >= x1 && x <= x2 && (y >= y1 || y >= y2) && (y <= y1 || y <= y2) && a * x + b * y + c == 0;
		}
	}

	static final Comparator<Event> eventComparator = (a, b) -> {
		if (a.x != b.x)
			return a.x < b.x ? -1 : 1;
		if (a.type != b.type)
			return a.type < b.type ? -1 : 1;
		return Integer.compare(a.y, b.y);
	};

	static class Event {
		final int x, y;
		final int type;
		final Segment segment;

		public Event(int x, int y, int type, Segment segment) {
			this.x = x;
			this.y = y;
			this.type = type;
			this.segment = segment;
		}
	}

	static long cross(long ax, long ay, long bx, long by, long cx, long cy) {
		return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
	}

	static Random random = new Random(1);

	// persistent treap
	static class Treap {
		final Segment key;
		final Treap left;
		final Treap right;
		final int size;

		Treap(Segment key) {
			this.key = key;
			left = null;
			right = null;
			size = 1;
		}

		public Treap(Segment key, Treap left, Treap right) {
			this.key = key;
			this.left = left;
			this.right = right;
			this.size = getSize(left) + getSize(right) + 1;
		}
	}

	static int getSize(Treap root) {
		return root == null ? 0 : root.size;
	}

	static class TreapPair {
		Treap left;
		Treap right;

		TreapPair(Treap left, Treap right) {
			this.left = left;
			this.right = right;
		}
	}

	static TreapPair split(Treap root, Segment minRight, boolean inclusive) {
		if (root == null)
			return new TreapPair(null, null);
		int cmp = segmentComparator.compare(root.key, minRight);
		if (cmp > 0 || cmp == 0 && inclusive) {
			TreapPair leftSplit = split(root.left, minRight, inclusive);
			return new TreapPair(leftSplit.left, new Treap(root.key, leftSplit.right, root.right));
		} else {
			TreapPair rightSplit = split(root.right, minRight, inclusive);
			return new TreapPair(new Treap(root.key, root.left, rightSplit.left), rightSplit.right);
		}
	}

	static Treap merge(Treap left, Treap right) {
		if (left == null)
			return right;
		if (right == null)
			return left;
		if (random.nextInt(left.size + right.size) < left.size) {
			return new Treap(left.key, left.left, merge(left.right, right));
		} else {
			return new Treap(right.key, merge(left, right.left), right.right);
		}
	}

	static Treap insert(Treap root, Segment x) {
		TreapPair t = split(root, x, true);
		return merge(merge(t.left, new Treap(x)), t.right);
	}

	static Treap remove(Treap root, Segment x) {
		TreapPair t = split(root, x, true);
		return merge(t.left, split(t.right, x, false).right);
	}

	static Treap min(Treap root) {
		while (root != null && root.left != null)
			root = root.left;
		return root;
	}

	static Treap max(Treap root) {
		while (root != null && root.right != null)
			root = root.right;
		return root;
	}

	// random test
	public static void main(String[] args) {
		for (int step = 0; step < 10_000; step++) {
			int range = 20;
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
