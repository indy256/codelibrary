package graphs.flows;

// https://en.wikipedia.org/wiki/Ford–Fulkerson_algorithm in O(V^2 * flow)
public class MaxFlowFordFulkersonSimple {

    public static int maxFlow(int[][] cap, int s, int t) {
        for (int flow = 0; ; ++flow)
            if (!augmentPath(cap, new boolean[cap.length], s, t))
                return flow;
    }

    static boolean augmentPath(int[][] cap, boolean[] vis, int i, int t) {
        if (i == t)
            return true;
        vis[i] = true;
        for (int j = 0; j < vis.length; j++)
            if (!vis[j] && cap[i][j] > 0 && augmentPath(cap, vis, j, t)) {
                --cap[i][j];
                ++cap[j][i];
                return true;
            }
        return false;
    }

    // Usage example
    public static void main(String[] args) {
        int[][] capacity = {{0, 1, 1, 0}, {1, 0, 1, 1}, {1, 1, 0, 1}, {0, 1, 1, 0}};
        System.out.println(2 == maxFlow(capacity, 0, 3));
    }
}
