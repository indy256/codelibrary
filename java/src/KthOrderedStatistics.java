import mooc.EdxIO;

public class KthOrderedStatistics {
    static class Sorter {
        private final int[] a;
        private final int min, max;

        public Sorter(int[] a, int min, int max) {
            this.a = a;
            this.min = min;
            this.max = max;
        }
        public void sort(int left, int right) {
            int pivot = a[(left + right) >>> 1];
            int l = left, r = right;
            while (l <= r) {
                while (a[l] < pivot) ++l;
                while (a[r] > pivot) --r;
                if (l <= r) {
                    int tmp = a[l];
                    a[l] = a[r];
                    a[r] = tmp;
                    ++l;
                    --r;
                }
            }
            if (min <= r && left < r) {
                sort(left, r);
            }
            if (l <= max && l < right) {
                sort(l, right);
            }
        }
    }

    public static void main(String[] args) {
        try (EdxIO io = EdxIO.create()) {
            int n = io.nextInt();
            int kMin = io.nextInt() - 1;
            int kMax = io.nextInt() - 1;
            int genA = io.nextInt();
            int genB = io.nextInt();
            int genC = io.nextInt();
            int[] a = new int[n];
            a[0] = io.nextInt();
            a[1] = io.nextInt();
            for (int i = 2; i < n; ++i) {
                a[i] = genA * a[i - 2] + genB * a[i - 1] + genC;
            }
            new Sorter(a, kMin, kMax).sort(0, n - 1);
            for (int i = kMin; i <= kMax; ++i) {
                io.print(a[i]);
                if (i == kMax) {
                    io.println();
                } else {
                    io.print(" ");
                }
            }
        }
    }
}