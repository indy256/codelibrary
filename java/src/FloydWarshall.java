public class FloydWarshall {
	
	public static int[][] floydWarshall(int[][] d) {
		int n = d.length;
		int[][] pred = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				pred[i][j] = (d[i][j] != Integer.MAX_VALUE && i != j) ? i : -1;
			}
		}
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				if (d[i][k] == Integer.MAX_VALUE) {
					continue;
				}
				for (int j = 0; j < n; j++) {
					if (d[k][j] == Integer.MAX_VALUE) {
						continue;
					}
					if (d[i][j] > d[i][k] + d[k][j]) {
						d[i][j] = d[i][k] + d[k][j];
						pred[i][j] = pred[k][j];
					}
				}
			}
		}
		return pred;
	}

	// Usage example
	public static void main(String[] args) {
		int[][] distances = { { 0, 3, 2 }, { 0, 0, 1 }, { 0, 0, 0 } };
		int[][] pred = floydWarshall(distances);

		System.out.println(distances[0][0] == 0);
		System.out.println(distances[0][1] == 2);
		System.out.println(distances[0][2] == 2);
		System.out.println(pred[0][0] == -1);
		System.out.println(pred[0][1] == 2);
		System.out.println(pred[0][2] == 0);
	}
}
