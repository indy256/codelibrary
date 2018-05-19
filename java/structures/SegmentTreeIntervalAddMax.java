package structures;

public class SegmentTreeIntervalAddMax {

    int n;
    int[] tmax;
    int[] tadd; // tadd[i] applies to tmax[i], tadd[2*i+1] and tadd[2*i+2]

    void push(int root) {
        tmax[root] += tadd[root];
        tadd[2 * root + 1] += tadd[root];
        tadd[2 * root + 2] += tadd[root];
        tadd[root] = 0;
    }

    public SegmentTreeIntervalAddMax(int n) {
        this.n = n;
        tmax = new int[4 * n];
        tadd = new int[4 * n];
    }

    public int max(int from, int to) {
        return max(from, to, 0, 0, n - 1);
    }

    int max(int from, int to, int root, int left, int right) {
        if (from > right || left > to)
            return Integer.MIN_VALUE;
        if (from <= left && right <= to) {
            return tmax[root] + tadd[root];
        }
        push(root);
        int mid = (left + right) >> 1;
        int res = Math.max(
                max(from, to, 2 * root + 1, left, mid),
                max(from, to, 2 * root + 2, mid + 1, right));
        return res;
    }

    public void add(int from, int to, int delta) {
        add(from, to, delta, 0, 0, n - 1);
    }

    void add(int from, int to, int delta, int root, int left, int right) {
        if (from > right || left > to)
            return;
        if (from <= left && right <= to) {
            tadd[root] += delta;
            return;
        }
        push(root); // this push may be omitted for add, but is necessary for other operations such as set
        int mid = (left + right) >> 1;
        add(from, to, delta, 2 * root + 1, left, mid);
        add(from, to, delta, 2 * root + 2, mid + 1, right);
        tmax[root] = Math.max(tmax[2 * root + 1] + tadd[2 * root + 1], tmax[2 * root + 2] + tadd[2 * root + 2]);
    }

    // tests
    public static void main(String[] args) {
        SegmentTreeIntervalAddMax t = new SegmentTreeIntervalAddMax(10);
        t.add(0, 9, 1);
        t.add(2, 4, 2);
        t.add(3, 5, 3);
        System.out.println(6 == t.max(0, 9));
        System.out.println(6 == t.tmax[0] + t.tadd[0]);
        System.out.println(1 == t.max(0, 0));
    }
}
