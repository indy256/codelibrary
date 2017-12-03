import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

// ITMOx: I2CPx -> K Best
public class BinarySearchKBest {
    static class Item implements Comparable<Item> {
        final int value, weight, index;
        double diff;

        Item(int value, int weight, int index) {
            this.value = value;
            this.weight = weight;
            this.index = index;
        }

        public void initAt(double alpha) {
            this.diff = value - alpha * weight;
        }

        public int compareTo(Item that) {
            return Double.compare(diff, that.diff);
        }
    }

    static void kth(Item[] items, int left, int right, int what) {
        Item pivot = items[(left + right) >>> 1];
        int l = left, r = right;
        while (l <= r) {
            while (items[l].compareTo(pivot) < 0) ++l;
            while (items[r].compareTo(pivot) > 0) --r;
            if (l <= r) {
                Item tmp = items[l];
                items[l] = items[r];
                items[r] = tmp;
                ++l;
                --r;
            }
        }
        if (left < r && what <= r) {
            kth(items, left, r, what);
        } else if (l < right && what >= l) {
            kth(items, l, right, what);
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader("input.txt"));
             PrintWriter out = new PrintWriter("output.txt")) {
            StringTokenizer first = new StringTokenizer(in.readLine());
            int n = Integer.parseInt(first.nextToken());
            int k = Integer.parseInt(first.nextToken());
            Item[] items = new Item[n];
            double max = 0, min = 1e9;
            for (int i = 0; i < n; ++i) {
                StringTokenizer ith = new StringTokenizer(in.readLine());
                int v = Integer.parseInt(ith.nextToken());
                int w = Integer.parseInt(ith.nextToken());
                items[i] = new Item(v, w, i + 1);
                double r = (double) v / w;
                max = Math.max(max, r + 1);
                min = Math.min(min, r);
            }
            for (int t = 0; t < 30; ++t) {
                double mid = (min + max) / 2;
                if (mid == min || mid == max) {
                    break;
                }
                for (Item i : items) {
                    i.initAt(mid);
                }
                kth(items, 0, n - 1, n - k);
                int sumv = 0, sumw = 0;
                for (int i = n - k; i < n; ++i) {
                    sumv += items[i].value;
                    sumw += items[i].weight;
                }
                if (sumv >= sumw * mid) {
                    min = mid;
                } else {
                    max = mid;
                }
            }
            for (Item i : items) {
                i.initAt(min);
            }
            kth(items, 0, n - 1, n - k);
            for (int i = n - k; i < n; ++i) {
                out.print(items[i].index);
                if (i + 1 == n) {
                    out.println();
                } else {
                    out.print(" ");
                }
            }
        }
    }
}