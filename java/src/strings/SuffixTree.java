package strings;

import java.util.Random;

// See http://codeforces.ru/blog/entry/16780 for description
public class SuffixTree {
    static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789\1\2";

    public static class Node {
        int begin;
        int end;
        int depth; // distance in characters from root to this node
        Node parent;
        Node[] children;
        Node suffixLink;

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

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 100_000; step++) {
            int n1 = rnd.nextInt(10);
            int n2 = rnd.nextInt(10);
            String s1 = getRandomString(n1, rnd);
            String s2 = getRandomString(n2, rnd);
            // build generalized suffix tree
            String s = s1 + '\1' + s2 + '\2';
            Node tree = buildSuffixTree(s);
            lcsLength = 0;
            lcsBeginIndex = 0;
            // find longest common substring
            lcs(tree, s1.length(), s1.length() + s2.length() + 1);
            int res2 = slowLcs(s1, s2);
            if (lcsLength != res2) {
                System.err.println(s.substring(lcsBeginIndex - 1, lcsBeginIndex + lcsLength - 1));
                System.err.println(s1);
                System.err.println(s2);
                System.err.println(lcsLength + " " + res2);
                throw new RuntimeException();
            }
        }
    }

    static int lcsLength;
    static int lcsBeginIndex;

    // traverse suffix tree to find longest common substring
    public static int lcs(Node node, int i1, int i2) {
        if (node.begin <= i1 && i1 < node.end) {
            return 1;
        }
        if (node.begin <= i2 && i2 < node.end) {
            return 2;
        }
        int mask = 0;
        for (char f = 0; f < ALPHABET.length(); f++) {
            if (node.children[f] != null) {
                mask |= lcs(node.children[f], i1, i2);
            }
        }
        if (mask == 3) {
            int curLength = node.depth + node.end - node.begin;
            if (lcsLength < curLength) {
                lcsLength = curLength;
                lcsBeginIndex = node.begin;
            }
        }
        return mask;
    }

    static int slowLcs(String a, String b) {
        int[][] lcs = new int[a.length()][b.length()];
        int res = 0;
        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                if (a.charAt(i) == b.charAt(j))
                    lcs[i][j] = 1 + (i > 0 && j > 0 ? lcs[i - 1][j - 1] : 0);
                res = Math.max(res, lcs[i][j]);
            }
        }
        return res;
    }

    static String getRandomString(int n, Random rnd) {
        return rnd.ints(n, 0, 3).mapToObj(i -> String.valueOf((char) (i + 'a'))).reduce("", (a, b) -> a + b);
    }
}
