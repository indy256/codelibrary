package graphs.spanningtree;

import java.util.Arrays;

// https://en.wikipedia.org/wiki/Prim%27s_algorithm in O(V^2)
public class Prim {

    public static long mstPrim(int[][] d) {
        int n = d.length;
        int[] prev = new int[n];
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;
        boolean[] visited = new boolean[n];
        long res = 0;
        for (int i = 0; i < n; i++) {
            int u = -1;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && (u == -1 || dist[u] > dist[j]))
                    u = j;
            }
            res += dist[u];
            visited[u] = true;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && dist[j] > d[u][j]) {
                    dist[j] = d[u][j];
                    prev[j] = u;
                }
            }
        }
        return res;
    }

    // Usage example
    public static void main(String[] args) {
    }
}
