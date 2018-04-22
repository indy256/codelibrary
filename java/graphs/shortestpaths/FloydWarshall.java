package graphs.shortestpaths;

import java.util.Arrays;

public class FloydWarshall {

    static final int INF = Integer.MAX_VALUE / 2;

    // precondition: d[i][i] == 0
    public static int[][] floydWarshall(int[][] d) {
        int n = d.length;
        int[][] pred = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                pred[i][j] = (i == j || d[i][j] == INF) ? -1 : i;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                // if (d[i][k] == INF) continue;
                for (int j = 0; j < n; j++) {
                    // if (d[k][j] == INF) continue;
                    if (d[i][j] > d[i][k] + d[k][j]) {
                        d[i][j] = d[i][k] + d[k][j];
                        // d[i][j] = Math.max(d[i][j], -INF);
                        pred[i][j] = pred[k][j];
                    }
                }
            }
        }
        for (int i = 0; i < n; i++)
            if (d[i][i] < 0)
                return null;
        return pred;
    }

    public static int[] restorePath(int[][] pred, int i, int j) {
        int n = pred.length;
        int[] path = new int[n];
        int pos = n;
        while (true) {
            path[--pos] = j;
            if (i == j) break;
            j = pred[i][j];
        }
        return Arrays.copyOfRange(path, pos, n);
    }

    // Usage example
    public static void main(String[] args) {
        int[][] dist = {{0, 3, 2}, {0, 0, 1}, {INF, 0, 0}};
        int[][] pred = floydWarshall(dist);
        int[] path = restorePath(pred, 0, 1);

        System.out.println(0 == dist[0][0]);
        System.out.println(2 == dist[0][1]);
        System.out.println(2 == dist[0][2]);
        System.out.println(-1 == pred[0][0]);
        System.out.println(2 == pred[0][1]);
        System.out.println(0 == pred[0][2]);
        System.out.println(Arrays.equals(new int[]{0, 2, 1}, path));
    }
}
