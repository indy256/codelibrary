package structures;

public class RmqSparseTable {
    int[][] rmq;

    public RmqSparseTable(int[] a) {
        int n = a.length;
        rmq = new int[32 - Integer.numberOfLeadingZeros(n)][];
        rmq[0] = a.clone();
        for (int i = 1; i < rmq.length; i++) {
            rmq[i] = new int[n - (1 << i) + 1];
            for (int j = 0; j < rmq[i].length; j++) rmq[i][j] = Math.min(rmq[i - 1][j], rmq[i - 1][j + (1 << (i - 1))]);
        }
    }

    public int min(int i, int j) {
        int k = 31 - Integer.numberOfLeadingZeros(j - i + 1);
        return Math.min(rmq[k][i], rmq[k][j - (1 << k) + 1]);
    }

    public static void main(String[] args) {
        {
            RmqSparseTable st = new RmqSparseTable(new int[] {1, 5, -2, 3});
            System.out.println(1 == st.min(0, 0));
            System.out.println(-2 == st.min(1, 2));
            System.out.println(-2 == st.min(0, 2));
            System.out.println(-2 == st.min(0, 3));
        }
        {
            RmqSparseTable st = new RmqSparseTable(new int[] {1, 5, -2});
            System.out.println(1 == st.min(0, 0));
            System.out.println(-2 == st.min(1, 2));
            System.out.println(-2 == st.min(0, 2));
        }
    }
}
