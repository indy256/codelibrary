package structures;

public class Tree2d {
    Treap.Node[] t;

    public Tree2d(int n) {
        t = new Treap.Node[2 * n];
    }

    public long query(int x1, int x2, int y1, int y2) {
        long res = 0;
        for (x1 += t.length / 2, x2 += t.length / 2; x1 <= x2; x1 = (x1 + 1) >> 1, x2 = (x2 - 1) >> 1) {
            if ((x1 & 1) != 0) {
                Treap.TreapAndResult treapAndResult = Treap.query(t[x1], y1, y2);
                t[x1] = treapAndResult.treap;
                res += treapAndResult.sum;
            }
            if ((x2 & 1) == 0) {
                Treap.TreapAndResult treapAndResult = Treap.query(t[x2], y1, y2);
                t[x2] = treapAndResult.treap;
                res += treapAndResult.sum;
            }
        }
        return res;
    }

    public void insert(int x, int y, int value) {
        x += t.length / 2;
        for (; x > 0; x >>= 1) t[x] = Treap.insert(t[x], y, value);
    }

    public void modify(int x1, int x2, int y1, int y2, int delta) {
        for (x1 += t.length / 2, x2 += t.length / 2; x1 <= x2; x1 = (x1 + 1) >> 1, x2 = (x2 - 1) >> 1) {
            if ((x1 & 1) != 0) {
                t[x1] = Treap.modify(t[x1], y1, y2, delta);
            }
            if ((x2 & 1) == 0) {
                t[x2] = Treap.modify(t[x2], y1, y2, delta);
            }
        }
    }

    // Usage example
    public static void main(String[] args) {
        Tree2d t = new Tree2d(10);
        t.insert(1, 5, 3);
        t.insert(3, 3, 2);
        t.insert(2, 6, 1);
        t.modify(0, 9, 0, 9, 1);
        System.out.println(t.query(0, 9, 0, 9));
    }
}
