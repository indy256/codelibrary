package structures;

public class FenwickTree {

    // T[i] += value
    public static void add(int[] t, int i, int value) {
        for (; i < t.length; i |= i + 1)
            t[i] += value;
    }

    // sum[0..i]
    public static int sum(int[] t, int i) {
        int res = 0;
        for (; i >= 0; i = (i & (i + 1)) - 1)
            res += t[i];
        return res;
    }

    ///////////////////////////////////////////////////

    // T[i] = max(T[i], value)
    public static void set(int[] t, int i, int value) {
        for (; i < t.length; i |= i + 1)
            t[i] = Math.max(t[i], value);
    }

    // max[0..i]
    public static int max(int[] t, int i) {
        int res = Integer.MIN_VALUE;
        for (; i >= 0; i = (i & (i + 1)) - 1)
            res = Math.max(res, t[i]);
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        int[] t = new int[10];
        add(t, 0, 1);
        add(t, 9, -2);
        System.out.println(-1 == sum(t, 9));
    }
}
