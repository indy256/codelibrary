package structures;

public class FenwickTree3D {

    public static void add(int[][][] t, int x, int y, int z, int value) {
        for (int i = x; i < t.length; i |= i + 1)
            for (int j = y; j < t[0].length; j |= j + 1)
                for (int k = z; k < t[0][0].length; k |= k + 1)
                    t[i][j][k] += value;
    }

    // sum[(0, 0, 0), (x, y, z)]
    public static int sum(int[][][] t, int x, int y, int z) {
        int res = 0;
        for (int i = x; i >= 0; i = (i & (i + 1)) - 1)
            for (int j = y; j >= 0; j = (j & (j + 1)) - 1)
                for (int k = z; k >= 0; k = (k & (k + 1)) - 1)
                    res += t[i][j][k];
        return res;
    }

    public static void main(String[] args) {
        int[][][] t = new int[10][20][30];
        add(t, 0, 0, 0, 1);
        add(t, 9, 19, 29, -2);
        System.out.println(-1 == sum(t, 9, 19, 29));
    }
}
