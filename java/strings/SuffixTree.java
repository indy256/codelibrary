package strings;

// See http://codeforces.ru/blog/entry/16780 for description
public class SuffixTree {
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789\1\2";

    public static class Node {
        public int begin;
        public int end;
        public int depth; // distance in characters from root to this node
        public Node parent;
        public Node[] children;
        public Node suffixLink;

        Node(int begin, int end, int depth, Node parent) {
            this.begin = begin;
            this.end = end;
            this.parent = parent;
            this.depth = depth;
            children = new Node[ALPHABET.length()];
        }
    }

    public static Node buildSuffixTree(CharSequence s) {
        int n = s.length();
        byte[] a = new byte[n];
        for (int i = 0; i < n; i++) a[i] = (byte) ALPHABET.indexOf(s.charAt(i));
        Node root = new Node(0, 0, 0, null);
        Node node = root;
        for (int i = 0, tail = 0; i < n; i++, tail++) {
            Node last = null;
            while (tail >= 0) {
                Node ch = node.children[a[i - tail]];
                while (ch != null && tail >= ch.end - ch.begin) {
                    tail -= ch.end - ch.begin;
                    node = ch;
                    ch = ch.children[a[i - tail]];
                }
                if (ch == null) {
                    node.children[a[i]] = new Node(i, n, node.depth + node.end - node.begin, node);
                    if (last != null) last.suffixLink = node;
                    last = null;
                } else {
                    byte afterTail = a[ch.begin + tail];
                    if (afterTail == a[i]) {
                        if (last != null) last.suffixLink = node;
                        break;
                    } else {
                        Node splitNode = new Node(ch.begin, ch.begin + tail, node.depth + node.end - node.begin, node);
                        splitNode.children[a[i]] = new Node(i, n, ch.depth + tail, splitNode);
                        splitNode.children[afterTail] = ch;
                        ch.begin += tail;
                        ch.depth += tail;
                        ch.parent = splitNode;
                        node.children[a[i - tail]] = splitNode;
                        if (last != null) last.suffixLink = splitNode;
                        last = splitNode;
                    }
                }
                if (node == root) {
                    --tail;
                } else {
                    node = node.suffixLink;
                }
            }
        }
        return root;
    }
}
