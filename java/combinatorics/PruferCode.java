package combinatorics;

import java.util.*;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Pr%C3%BCfer_sequence
public class PruferCode {
    // O(n) complexity
    public static List<Integer>[] pruferCode2Tree(int[] pruferCode) {
        int n = pruferCode.length + 2;
        List<Integer>[] tree = Stream.generate(ArrayList::new).limit(n).toArray(List[] ::new);
        int[] degree = new int[n];
        Arrays.fill(degree, 1);
        for (int v : pruferCode) ++degree[v];
        int ptr = 0;
        while (degree[ptr] != 1) ++ptr;
        int leaf = ptr;
        for (int v : pruferCode) {
            tree[leaf].add(v);
            tree[v].add(leaf);
            --degree[leaf];
            --degree[v];
            if (degree[v] == 1 && v < ptr) {
                leaf = v;
            } else {
                while (degree[++ptr] != 1)
                    ;
                leaf = ptr;
            }
        }
        for (int v = 0; v < n - 1; v++) {
            if (degree[v] == 1) {
                tree[v].add(n - 1);
                tree[n - 1].add(v);
            }
        }
        return tree;
    }

    // precondition: n >= 2
    // O(n) complexity
    public static int[] tree2PruferCode(List<Integer>[] tree) {
        int n = tree.length;
        int[] parent = new int[n];
        parent[n - 1] = -1;
        pruferDfs(tree, parent, n - 1);
        int[] degree = new int[n];
        int ptr = -1;
        for (int i = 0; i < n; ++i) {
            degree[i] = tree[i].size();
            if (degree[i] == 1 && ptr == -1)
                ptr = i;
        }
        int[] res = new int[n - 2];
        for (int i = 0, leaf = ptr; i < n - 2; ++i) {
            int next = parent[leaf];
            res[i] = next;
            --degree[next];
            if (degree[next] == 1 && next < ptr) {
                leaf = next;
            } else {
                while (degree[++ptr] != 1)
                    ;
                leaf = ptr;
            }
        }
        return res;
    }

    static void pruferDfs(List<Integer>[] tree, int[] parent, int u) {
        for (int v : tree[u]) {
            if (v != parent[u]) {
                parent[v] = u;
                pruferDfs(tree, parent, v);
            }
        }
    }

    // Usage example
    public static void main(String[] args) {
        int[] a = new int[5];
        do {
            List<Integer>[] tree = pruferCode2Tree(a);
            int[] b = tree2PruferCode(tree);
            if (!Arrays.equals(a, b))
                throw new RuntimeException();
        } while (Arrangements.nextArrangementWithRepeats(a, a.length + 2));
    }
}
