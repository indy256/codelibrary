import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.StringTokenizer;

public class MedianSum implements Runnable {
    static Random random = new Random(94731597435431L + System.nanoTime());

    static class Treap {
        long x;
        long y;
        long sum;
        int count;
        Treap left = null;
        Treap right = null;

        Treap(long x) {
            this.x = x;
            y = random.nextLong();
            sum = x;
            count = 1;
        }

        void update() {
            sum = x + getSum(left) + getSum(right);
            count = 1 + getCount(left) + getCount(right);
        }
    }

    static class TreapPair {
        Treap left;
        Treap right;

        TreapPair(Treap left, Treap right) {
            this.left = left;
            this.right = right;
        }
    }

    static TreapPair split(Treap root, long minRight) {
        if (root == null) return new TreapPair(null, null);
        if (root.x >= minRight) {
            TreapPair sub = split(root.left, minRight);
            root.left = sub.right;
            root.update();
            sub.right = root;
            return sub;
        } else {
            TreapPair sub = split(root.right, minRight);
            root.right = sub.left;
            root.update();
            sub.left = root;
            return sub;
        }
    }

    static Treap merge(Treap left, Treap right) {
        if (left == null) return right;
        if (right == null) return left;
        if (left.y > right.y) {
            left.right = merge(left.right, right);
            left.update();
            return left;
        } else {
            right.left = merge(left, right.left);
            right.update();
            return right;
        }
    }

    private void solve() throws IOException {
        int numOps = nextInt();
        Treap[] byRem = new Treap[5];
        TreapPair[] sep = new TreapPair[5];
        for (int step = 0; step < numOps; ++step) {
            String what = nextToken();
            if (what.charAt(0) == 'a') {
                long x = nextLong();
                Treap toAdd = new Treap(x);
                int pos = 0;
                for (int i = 0; i < 5; ++i) {
                    sep[i] = split(byRem[i], x);
                    pos += getCount(sep[i].left);
                }
                for (int i = 0; i < 5; ++i) {
                    Treap cur = sep[i].left;
                    if (i == pos % 5)
                        cur = merge(cur, toAdd);
                    cur = merge(cur, sep[(i + 4) % 5].right);
                    byRem[i] = cur;
                }
            } else if (what.charAt(0) == 'd') {
                long x = nextLong();
                for (int i = 0; i < 5; ++i) {
                    sep[i] = split(byRem[i], x);
                    if (getMin(sep[i].right) == x) {
                        TreapPair tmp = split(sep[i].right, x + 1);
                        sep[i].right = tmp.right;
                    }
                }
                for (int i = 0; i < 5; ++i) {
                    Treap cur = sep[i].left;
                    cur = merge(cur, sep[(i + 1) % 5].right);
                    byRem[i] = cur;
                }
            } else if (what.charAt(0) == 's') {
                writer.println(getSum(byRem[2]));
            }
        }
    }

    private static long getMin(Treap root) {
        if (root == null) return Long.MAX_VALUE;
        while (root.left != null) {
            root = root.left;
        }
        return root.x;
    }

    private static int getCount(Treap root) {
        if (root == null) return 0; else return root.count;
    }

    private static long getSum(Treap root) {
        if (root == null) return 0; else return root.sum;
    }

    public static void main(String[] args) {
        new MedianSum().run();
    }

    BufferedReader reader;
    StringTokenizer tokenizer;
    PrintWriter writer;

    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
            writer = new PrintWriter(System.out);
            solve();
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }

    long nextLong() throws IOException {
        return Long.parseLong(nextToken());
    }

    double nextDouble() throws IOException {
        return Double.parseDouble(nextToken());
    }

    String nextToken() throws IOException {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            tokenizer = new StringTokenizer(reader.readLine());
        }
        return tokenizer.nextToken();
    }
}