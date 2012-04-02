import java.util.Arrays;

public class Prim2 {

	public static long mstPrim(int[][] d) {
		int n = d.length;
		int[] prev = new int[n];
		int[] dist = new int[n];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[0] = 0;
		boolean[] vis = new boolean[n];
		long res = 0;
		for (int steps = 0; steps < n; steps++) {
			int bdist = Integer.MAX_VALUE;
			int bi = -1;
			for (int i = 0; i < n; i++) {
				if (!vis[i] && bdist > dist[i]) {
					bdist = dist[i];
					bi = i;
				}
			}
			res += bdist;
			vis[bi] = true;
			for (int i = 0; i < n; i++) {
				if (!vis[i] && dist[i] > d[bi][i]) {
					dist[i] = d[bi][i];
					prev[i] = bi;
				}
			}
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
	}
}
