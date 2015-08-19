import java.util.Random;

public class KdTreePointQuery {

	int[] x;
	int[] y;

	public KdTreePointQuery(int[] x, int[] y) {
		this.x = x;
		this.y = y;
		build(0, x.length, true);
	}

	void build(int low, int high, boolean divX) {
		if (high - low <= 1)
			return;
		int mid = (low + high) >>> 1;
		nth_element(low, high, mid, divX);

		build(low, mid, !divX);
		build(mid + 1, high, !divX);
	}

	// See http://www.cplusplus.com/reference/algorithm/nth_element
	void nth_element(int low, int high, int n, boolean divX) {
		while (true) {
			int k = randomizedPartition(low, high, divX);
			if (n < k)
				high = k;
			else if (n > k)
				low = k + 1;
			else
				return;
		}
	}

	static final Random rnd = new Random(1);

	int randomizedPartition(int low, int high, boolean divX) {
		swap(low + rnd.nextInt(high - low), high - 1);
		int v = divX ? x[high - 1] : y[high - 1];
		int i = low - 1;
		for (int j = low; j < high; j++)
			if ((divX ? x[j] : y[j]) <= v)
				swap(++i, j);
		return i;
	}

	void swap(int i, int j) {
		int t = x[i];
		x[i] = x[j];
		x[j] = t;
		t = y[i];
		y[i] = y[j];
		y[j] = t;
	}

	long bestDist;
	int bestNode;

	public int findNearestNeighbour(int px, int py) {
		bestDist = Long.MAX_VALUE;
		findNearestNeighbour(0, x.length, px, py, true);
		return bestNode;
	}

	void findNearestNeighbour(int low, int high, int px, int py, boolean divX) {
		if (high - low <= 0)
			return;
		int mid = (low + high) >>> 1;
		long dx = px - x[mid];
		long dy = py - y[mid];
		long dist = dx * dx + dy * dy;
		if (bestDist > dist) {
			bestDist = dist;
			bestNode = mid;
		}
		long delta = divX ? dx : dy;
		long delta2 = delta * delta;

		if (delta <= 0) {
			findNearestNeighbour(low, mid, px, py, !divX);
			if (delta2 < bestDist)
				findNearestNeighbour(mid + 1, high, px, py, !divX);
		} else {
			findNearestNeighbour(mid + 1, high, px, py, !divX);
			if (delta2 < bestDist)
				findNearestNeighbour(low, mid, px, py, !divX);
		}
	}

	// random test
	public static void main(String[] args) {
		for (int step = 0; step < 100_000; step++) {
			int qx = rnd.nextInt(100) - 50;
			int qy = rnd.nextInt(100) - 50;
			int n = rnd.nextInt(100) + 1;
			int[] x = new int[n];
			int[] y = new int[n];
			long minDist = Long.MAX_VALUE;
			for (int i = 0; i < n; i++) {
				x[i] = rnd.nextInt(100) - 50;
				y[i] = rnd.nextInt(100) - 50;
				minDist = Math.min(minDist, (long) (x[i] - qx) * (x[i] - qx) + (long) (y[i] - qy) * (y[i] - qy));
			}
			KdTreePointQuery kdTree = new KdTreePointQuery(x, y);
			int index = kdTree.findNearestNeighbour(qx, qy);
			if (minDist != kdTree.bestDist || (long) (x[index] - qx) * (x[index] - qx) + (long) (y[index] - qy) * (y[index] - qy) != minDist)
				throw new RuntimeException();
		}
	}
}
