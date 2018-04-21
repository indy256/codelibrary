package structures;

public class FenwickTreeExtended {

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

    public static int[] createFromArray(int[] a) {
        int[] t = a.clone();
        for (int i = 0; i < a.length; i++) {
            int j = i | (i + 1);
            if (j < a.length)
                t[j] += t[i];
        }
        return t;
    }

    // sum[a..b]
    public static int sum(int[] t, int a, int b) {
        return sum(t, b) - sum(t, a - 1);
    }

    public static int get(int[] t, int i) {
        int res = t[i];
        int lca = (i & (i + 1)) - 1;
        for (--i; i != lca; i = (i & (i + 1)) - 1)
            res -= t[i];
        return res;
    }

    public static void set(int[] t, int i, int value) {
        add(t, i, -get(t, i) + value);
    }

    ///////////////////////////////////////////////////////
    // interval add
    public static void add(int[] t, int a, int b, int value) {
        add(t, a, value);
        add(t, b + 1, -value);
    }

    // point query
    public static int get1(int[] t, int i) {
        return sum(t, i);
    }
    ///////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////
    // interval add
    public static void add(int[] t1, int[] t2, int a, int b, int value) {
        add(t1, a, value);
        add(t1, b, -value);
        add(t2, a, -value * (a - 1));
        add(t2, b, value * b);
    }

    // interval query
    public static int sum(int[] t1, int[] t2, int i) {
        return sum(t1, i) * i + sum(t2, i);
    }
    ///////////////////////////////////////////////////////

    // Returns min(p | sum[0,p] >= sum)
    public static int lower_bound(int[] t, int sum) {
        int pos = 0;
        for (int blockSize = Integer.highestOneBit(t.length); blockSize != 0; blockSize >>= 1) {
            int p = pos + blockSize - 1;
            if (p < t.length && t[p] < sum) {
                sum -= t[p];
                pos += blockSize;
            }
        }
        return pos;
    }

    // Usage example
    public static void main(String[] args) {
        int[] t = new int[10];
        set(t, 0, 1);
        add(t, 9, -2);
        System.out.println(-1 == sum(t, 0, 9));

        t = createFromArray(new int[]{1, 2, 3, 4, 5, 6});
        for (int i = 0; i < t.length; i++)
            System.out.print(get(t, i) + " ");
        System.out.println();
        t = createFromArray(new int[]{0, 0, 1, 0, 0, 1, 0, 0});
        System.out.println(5 == lower_bound(t, 2));

        int[] t1 = new int[10];
        int[] t2 = new int[10];
        add(t1, t2, 0, 9, 1);
        add(t1, t2, 0, 0, -2);
        System.out.println(sum(t1, t2, 9));
    }
}
