package optimization;

public class ConvexHullOptimization {

    long[] A = new long[1000000];
    long[] B = new long[1000000];
    int len;
    int ptr;

    // a descends
    public void addLine(long a, long b) {
        // intersection of (A[len-2],B[len-2]) with (A[len-1],B[len-1]) must lie to the left of intersection of (A[len-1],B[len-1]) with (a,b)
        while (len >= 2 && (B[len - 2] - B[len - 1]) * (a - A[len - 1]) >= (B[len - 1] - b) * (A[len - 1] - A[len - 2])) {
            --len;
        }
        A[len] = a;
        B[len] = b;
        ++len;
    }

    // x ascends
    public long minValue(long x) {
        ptr = Math.min(ptr, len - 1);
        while (ptr + 1 < len && A[ptr + 1] * x + B[ptr + 1] <= A[ptr] * x + B[ptr]) {
            ++ptr;
        }
        return A[ptr] * x + B[ptr];
    }

    // Usage example
    public static void main(String[] args) {
        ConvexHullOptimization h = new ConvexHullOptimization();
        h.addLine(3, 0);
        h.addLine(2, 1);
        h.addLine(3, 2);
        h.addLine(0, 6);
        System.out.println(h.minValue(0));
        System.out.println(h.minValue(1));
        System.out.println(h.minValue(2));
        System.out.println(h.minValue(3));
    }
}
