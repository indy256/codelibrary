package structures;

// https://en.wikipedia.org/wiki/Quadtree
public class QuadTree {

    static class Node {
        int count;
        Node topLeft, topRight, bottomLeft, bottomRight;
    }

    Node root;

    static final int maxx = (1 << 30);
    static final int maxy = (1 << 30);

    // insert point (x,y)
    public void insert(int x, int y) {
        root = insert(root, 0, 0, maxx - 1, maxy - 1, x, y);
    }

    Node insert(Node node, int ax, int ay, int bx, int by, int x, int y) {
        if (ax > x || x > bx || ay > y || y > by)
            return node;
        if (node == null)
            node = new Node();
        ++node.count;
        if (ax == bx && ay == by)
            return node;

        int mx = (ax + bx) >> 1;
        int my = (ay + by) >> 1;

        node.bottomLeft = insert(node.bottomLeft, ax, ay, mx, my, x, y);
        node.topLeft = insert(node.topLeft, ax, my + 1, mx, by, x, y);
        node.bottomRight = insert(node.bottomRight, mx + 1, ay, bx, my, x, y);
        node.topRight = insert(node.topRight, mx + 1, my + 1, bx, by, x, y);

        return node;
    }

    // number of points in [x1,x2] x [y1,y2]
    public int count(int x1, int y1, int x2, int y2) {
        return count(root, 0, 0, maxx - 1, maxy - 1, x1, y1, x2, y2);
    }

    int count(Node node, int ax, int ay, int bx, int by, int x1, int y1, int x2, int y2) {
        if (node == null || ax > x2 || x1 > bx || ay > y2 || y1 > by)
            return 0;
        if (x1 <= ax && bx <= x2 && y1 <= ay && by <= y2)
            return node.count;

        int mx = (ax + bx) >> 1;
        int my = (ay + by) >> 1;

        int res = 0;
        res += count(node.bottomLeft, ax, ay, mx, my, x1, y1, x2, y2);
        res += count(node.topLeft, ax, my + 1, mx, by, x1, y1, x2, y2);
        res += count(node.bottomRight, mx + 1, ay, bx, my, x1, y1, x2, y2);
        res += count(node.topRight, mx + 1, my + 1, bx, by, x1, y1, x2, y2);
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        QuadTree t = new QuadTree();

        t.insert(0, 0);
        t.insert(1, 0);
        t.insert(2, 0);
        t.insert(3, 0);
        System.out.println(4 == t.count(0, 0, 3, 0));
    }
}
