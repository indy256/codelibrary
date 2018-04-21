package combinatorics;

import java.util.Arrays;

public class SetPartitions {

    public static int[][] generateSetPartitions(int n) {
        if (n == 1)
            return new int[][]{{0}};
        int[][] p = generateSetPartitions(n - 1);
        int[] m = new int[p.length];
        int len = 0;
        for (int i = 0; i < p.length; i++) {
            for (int v : p[i])
                m[i] = Math.max(m[i], 1 + v);
            len += m[i] + 1;
        }
        int[][] res = new int[len][n];
        for (int i = 0, pos = 0; i < p.length; i++) {
            for (int j = 0; j <= m[i]; j++, pos++) {
                System.arraycopy(p[i], 0, res[pos], 0, n - 1);
                res[pos][n - 1] = (i % 2 == 1 ? j + 1 : m[i] - j + 1) % (m[i] + 1);
            }
        }
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        for (int i = 1; i < 5; i++)
            System.out.println(Arrays.deepToString(generateSetPartitions(i)));
    }
}
