package structures;

public class RmqSparseTable {

    int[] logTable;
    int[][] rmq;
    int[] a;

    public RmqSparseTable(int[] a) {
        this.a = a;
        int n = a.length;

        logTable = new int[n + 1];
        for (int i = 2; i <= n; i++)
            logTable[i] = logTable[i >> 1] + 1;

        rmq = new int[logTable[n] + 1][n];

        for (int i = 0; i < n; ++i)
            rmq[0][i] = i;

        for (int k = 1; (1 << k) < n; ++k) {
            for (int i = 0; i + (1 << k) <= n; i++) {
                int x = rmq[k - 1][i];
                int y = rmq[k - 1][i + (1 << k - 1)];
                rmq[k][i] = a[x] <= a[y] ? x : y;
            }
        }
    }

    public int minPos(int i, int j) {
        int k = logTable[j - i];
        int x = rmq[k][i];
        int y = rmq[k][j - (1 << k) + 1];
        return a[x] <= a[y] ? x : y;
    }

    public static void main(String[] args) {
        int[] a = {1, 5, -2, 3};
        RmqSparseTable st = new RmqSparseTable(a);

        System.out.println(2 == st.minPos(0, 3));
    }
}
