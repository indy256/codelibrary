package structures;

import java.util.*;

public class SegmentTreeFast {

    // Modify the following 5 methods to implement your custom operations on the tree.
    // This example implements Add/Sum operations. Operations like Add/Max, Set/Max can also be implemented.
    int modifyOperation(int x, int y) {
        return x + y;
    }

    // query (or combine) operation
    int queryOperation(int leftValue, int rightValue) {
        return leftValue + rightValue;
    }

    int deltaEffectOnSegment(int delta, int segmentLength) {
        if (delta == getNeutralDelta()) return getNeutralDelta();
        // Here you must write a fast equivalent of following slow code:
        // int result = delta;
        // for (int i = 1; i < segmentLength; i++) result = queryOperation(result, delta);
        // return result;
        return delta * segmentLength;
    }

    int getNeutralDelta() {
        return 0;
    }

    int getInitValue() {
        return 0;
    }

    // generic code
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

    void pushDelta(int i) {
        int d = 0;
        for (; (i >> d) > 0; d++) {
        }
        for (d -= 2; d >= 0; d--) {
            int x = i >> d;
            value[x >> 1] = joinNodeValueWithDelta(x >> 1, 1 << (d + 1));
            delta[x] = joinDeltas(delta[x], delta[x >> 1]);
            delta[x ^ 1] = joinDeltas(delta[x ^ 1], delta[x >> 1]);
            delta[x >> 1] = getNeutralDelta();
        }
    }

    public SegmentTreeFast(int n) {
        value = new int[2 * n];
        for (int i = 0; i < n; i++) {
            value[i + n] = getInitValue();
        }
        for (int i = 2 * n - 1; i > 1; i -= 2) {
            value[i >> 1] = queryOperation(value[i], value[i ^ 1]);
        }
        delta = new int[2 * n];
        Arrays.fill(delta, getNeutralDelta());
    }

    int joinNodeValueWithDelta(int i, int len) {
        return joinValueWithDelta(value[i], deltaEffectOnSegment(delta[i], len));
    }

    public int query(int from, int to) {
        from += value.length >> 1;
        to += value.length >> 1;
        pushDelta(from);
        pushDelta(to);
        int res = 0;
        boolean found = false;
        for (int len = 1; from <= to; from = (from + 1) >> 1, to = (to - 1) >> 1, len <<= 1) {
            if ((from & 1) != 0) {
                res = found ? queryOperation(res, joinNodeValueWithDelta(from, len)) : joinNodeValueWithDelta(from, len);
                found = true;
            }
            if ((to & 1) == 0) {
                res = found ? queryOperation(res, joinNodeValueWithDelta(to, len)) : joinNodeValueWithDelta(to, len);
                found = true;
            }
        }
        if (!found) throw new RuntimeException();
        return res;
    }

    public void modify(int from, int to, int delta) {
        from += value.length >> 1;
        to += value.length >> 1;
        pushDelta(from);
        pushDelta(to);
        int a = from;
        int b = to;
        for (; from <= to; from = (from + 1) >> 1, to = (to - 1) >> 1) {
            if ((from & 1) != 0) {
                this.delta[from] = joinDeltas(this.delta[from], delta);
            }
            if ((to & 1) == 0) {
                this.delta[to] = joinDeltas(this.delta[to], delta);
            }
        }
        for (int i = a, len = 1; i > 1; i >>= 1, len <<= 1) {
            value[i >> 1] = queryOperation(joinNodeValueWithDelta(i, len), joinNodeValueWithDelta(i ^ 1, len));
        }
        for (int i = b, len = 1; i > 1; i >>= 1, len <<= 1) {
            value[i >> 1] = queryOperation(joinNodeValueWithDelta(i, len), joinNodeValueWithDelta(i ^ 1, len));
        }
    }

    // Random test
    public static void main(String[] args) {
        Random rnd = new Random();
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(50) + 1;
            int[] x = new int[n];
            SegmentTreeFast t = new SegmentTreeFast(n);
            Arrays.fill(x, t.getInitValue());
            for (int i = 0; i < 1000; i++) {
                int b = rnd.nextInt(n);
                int a = rnd.nextInt(b + 1);
                int cmd = rnd.nextInt(3);
                if (cmd == 0) {
                    int delta = rnd.nextInt(100) - 50;
                    t.modify(a, b, delta);
                    for (int j = a; j <= b; j++) {
                        x[j] = t.joinValueWithDelta(x[j], delta);
                    }
                } else if (cmd == 1) {
                    int res1 = t.query(a, b);
                    int res2 = x[a];
                    for (int j = a + 1; j <= b; j++) {
                        res2 = t.queryOperation(res2, x[j]);
                    }
                    if (res1 != res2) {
                        throw new RuntimeException();
                    }

                } else {
                    for (int j = 0; j < n; j++) {
                        if (t.query(j, j) != x[j]) {
                            throw new RuntimeException();
                        }
                    }
                }
            }
        }
        System.out.println("Test passed");
    }
}
