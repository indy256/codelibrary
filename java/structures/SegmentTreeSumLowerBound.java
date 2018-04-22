package structures;

public class SegmentTreeSumLowerBound {
    int[] s;
    int n;

    public SegmentTreeSumLowerBound(int n) {
        this.n = n;
        s = new int[4 * n];
        buildTree(1, 0, n - 1);
    }

    void buildTree(int node, int left, int right) {
        if (left != right) {
            int mid = (left + right) >> 1;
            buildTree(node * 2, left, mid);
            buildTree(node * 2 + 1, mid + 1, right);
            s[node] = s[node * 2] + s[node * 2 + 1];
        }
    }

    // T[i] += value, assuming value >= 0 (otherwise lower_bound will not work)
    public void add(int i, int value) {
        add(i, value, 1, 0, n - 1);
    }

    void add(int i, int value, int node, int left, int right) {
        if (left == right) {
            s[node] += value;
            return;
        }
        int mid = (left + right) >> 1;
        if (i <= mid)
            add(i, value, node * 2, left, mid);
        else
            add(i, value, node * 2 + 1, mid + 1, right);
        s[node] = s[node * 2] + s[node * 2 + 1];
    }

    // Returns min(p | p<=b && sum[a..p]>=sum). If no such p exists, returns ~sum[a..b].
    public int lower_bound(int a, int b, int sum) {
        return lower_bound(a, b, sum, 1, 0, n - 1);
    }

    int lower_bound(int a, int b, int sum, int node, int left, int right) {
        if (left > b || right < a)
            return ~0;
        if (left >= a && right <= b && s[node] < sum)
            return ~s[node];
        if (left == right)
            return left;
        int mid = (left + right) >> 1;
        int res1 = lower_bound(a, b, sum, node * 2, left, mid);
        if (res1 >= 0)
            return res1;
        int res2 = lower_bound(a, b, sum - ~res1, node * 2 + 1, mid + 1, right);
        if (res2 >= 0)
            return res2;
        return ~(~res1 + ~res2);
    }

    // sum[a..b]
    public int sum(int a, int b) {
        return sum(a, b, 1, 0, n - 1);
    }

    int sum(int a, int b, int node, int left, int right) {
        if (left >= a && right <= b)
            return s[node];
        int mid = (left + right) >> 1;
        int res = 0;
        if (a <= mid)
            res += sum(a, b, node * 2, left, mid);
        if (b > mid)
            res += sum(a, b, node * 2 + 1, mid + 1, right);
        return res;
    }

    // T[i]
    public int get(int i) {
        return sum(i, i);
    }

    // T[i] = value
    public void set(int i, int value) {
        add(i, -get(i) + value);
    }

    // Usage example
    public static void main(String[] args) {
        SegmentTreeSumLowerBound t = new SegmentTreeSumLowerBound(4);
        t.set(0, 1);
        t.set(1, 5);
        t.set(2, 2);
        t.set(3, 3);
        System.out.println(1 == t.lower_bound(1, 3, 5));
        t.set(1, 3);
        System.out.println(3 == t.get(1));
        System.out.println(2 == t.lower_bound(1, 3, 5));
    }
}
