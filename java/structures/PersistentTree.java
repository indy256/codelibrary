package structures;

// https://en.wikipedia.org/wiki/Persistent_data_structure
public class PersistentTree {

    public static class Node {
        Node left, right;
        int sum;

        Node(int value) {
            sum = value;
        }

        Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            if (left != null)
                sum += left.sum;
            if (right != null)
                sum += right.sum;
        }
    }

    public static Node build(int left, int right) {
        if (left == right)
            return new Node(0);
        int mid = (left + right) >> 1;
        return new Node(build(left, mid), build(mid + 1, right));
    }

    public static int sum(int from, int to, Node root, int left, int right) {
        if (from > right || left > to)
            return 0;
        if (from <= left && right <= to)
            return root.sum;
        int mid = (left + right) >> 1;
        return sum(from, to, root.left, left, mid) + sum(from, to, root.right, mid + 1, right);
    }

    public static Node set(int pos, int value, Node root, int left, int right) {
        if (left == right)
            return new Node(value);
        int mid = (left + right) >> 1;
        return pos <= mid ?
                new Node(set(pos, value, root.left, left, mid), root.right) :
                new Node(root.left, set(pos, value, root.right, mid + 1, right));
    }

    // Usage example
    public static void main(String[] args) {
        int n = 10;
        Node t1 = build(0, n - 1);
        Node t2 = set(0, 1, t1, 0, n - 1);
        System.out.println(0 == sum(0, 9, t1, 0, n - 1));
        System.out.println(1 == sum(0, 9, t2, 0, n - 1));
    }
}
