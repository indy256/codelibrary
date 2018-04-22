package structures;

import java.util.*;

public class SegmentTree {

    // Modify the following 5 methods to implement your custom operations on the tree.
    // This example implements Add/Max operations. Operations like Add/Sum, Set/Max can also be implemented.
    int modifyOperation(int x, int y) {
        return x + y;
    }

    // query (or combine) operation
    int queryOperation(int leftValue, int rightValue) {
        return Math.max(leftValue, rightValue);
    }

    int deltaEffectOnSegment(int delta, int segmentLength) {
        if (delta == getNeutralDelta()) return getNeutralDelta();
        // Here you must write a fast equivalent of following slow code:
        // int result = delta;
        // for (int i = 1; i < segmentLength; i++) result = queryOperation(result, delta);
        // return result;
        return delta;
    }

    int getNeutralDelta() {
        return 0;
    }

    int getInitValue() {
        return 0;
    }

    // generic code
    int n;
    int[] value;
    int[] delta; // delta[i] affects value[i], delta[2*i+1] and delta[2*i+2]

    int joinValueWithDelta(int value, int delta) {
        if (delta == getNeutralDelta()) return value;
        return modifyOperation(value, delta);
    }

    int joinDeltas(int delta1, int delta2) {
        if (delta1 == getNeutralDelta()) return delta2;
        if (delta2 == getNeutralDelta()) return delta1;
        return modifyOperation(delta1, delta2);
    }

    void pushDelta(int root, int left, int right) {
        value[root] = joinValueWithDelta(value[root], deltaEffectOnSegment(delta[root], right - left + 1));
        delta[2 * root + 1] = joinDeltas(delta[2 * root + 1], delta[root]);
        delta[2 * root + 2] = joinDeltas(delta[2 * root + 2], delta[root]);
        delta[root] = getNeutralDelta();
    }

    public SegmentTree(int n) {
        this.n = n;
        value = new int[4 * n];
        delta = new int[4 * n];
        init(0, 0, n - 1);
    }

    void init(int root, int left, int right) {
        if (left == right) {
            value[root] = getInitValue();
            delta[root] = getNeutralDelta();
        } else {
            int mid = (left + right) >> 1;
            init(2 * root + 1, left, mid);
            init(2 * root + 2, mid + 1, right);
            value[root] = queryOperation(value[2 * root + 1], value[2 * root + 2]);
            delta[root] = getNeutralDelta();
        }
    }

    public int query(int from, int to) {
        return query(from, to, 0, 0, n - 1);
    }

    int query(int from, int to, int root, int left, int right) {
        if (from == left && to == right)
            return joinValueWithDelta(value[root], deltaEffectOnSegment(delta[root], right - left + 1));
        pushDelta(root, left, right);
        int mid = (left + right) >> 1;
        if (from <= mid && to > mid)
            return queryOperation(
                    query(from, Math.min(to, mid), root * 2 + 1, left, mid),
                    query(Math.max(from, mid + 1), to, root * 2 + 2, mid + 1, right));
        else if (from <= mid)
            return query(from, Math.min(to, mid), root * 2 + 1, left, mid);
        else if (to > mid)
            return query(Math.max(from, mid + 1), to, root * 2 + 2, mid + 1, right);
        else
            throw new RuntimeException("Incorrect query from " + from + " to " + to);
    }

    public void modify(int from, int to, int delta) {
        modify(from, to, delta, 0, 0, n - 1);
    }

    void modify(int from, int to, int delta, int root, int left, int right) {
        if (from == left && to == right) {
            this.delta[root] = joinDeltas(this.delta[root], delta);
            return;
        }
        pushDelta(root, left, right);
        int mid = (left + right) >> 1;
        if (from <= mid)
            modify(from, Math.min(to, mid), delta, 2 * root + 1, left, mid);
        if (to > mid)
            modify(Math.max(from, mid + 1), to, delta, 2 * root + 2, mid + 1, right);
        value[root] = queryOperation(
                joinValueWithDelta(value[2 * root + 1], deltaEffectOnSegment(this.delta[2 * root + 1], mid - left + 1)),
                joinValueWithDelta(value[2 * root + 2], deltaEffectOnSegment(this.delta[2 * root + 2], right - mid)));
    }

    // Random test
    public static void main(String[] args) {
        Random rnd = new Random();
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(50) + 1;
            int[] x = new int[n];
            SegmentTree t = new SegmentTree(n);
            Arrays.fill(x, t.getInitValue());
            for (int i = 0; i < 1000; i++) {
                int b = rnd.nextInt(n);
                int a = rnd.nextInt(b + 1);
                int cmd = rnd.nextInt(3);
                if (cmd == 0) {
                    int delta = rnd.nextInt(100) - 50;
                    t.modify(a, b, delta);
                    for (int j = a; j <= b; j++)
                        x[j] = t.joinValueWithDelta(x[j], delta);
                } else if (cmd == 1) {
                    int res1 = t.query(a, b);
                    int res2 = x[a];
                    for (int j = a + 1; j <= b; j++)
                        res2 = t.queryOperation(res2, x[j]);
                    if (res1 != res2)
                        throw new RuntimeException();
                } else {
                    for (int j = 0; j < n; j++) {
                        if (t.query(j, j) != x[j])
                            throw new RuntimeException();
                    }
                }
            }
        }
        System.out.println("Test passed");
    }
}
