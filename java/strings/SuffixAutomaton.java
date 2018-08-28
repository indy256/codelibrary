package strings;

import java.util.*;

// https://en.wikipedia.org/wiki/Directed_acyclic_word_graph
public class SuffixAutomaton {

    public static class State {
        public int length;
        public int suffLink;
        public List<Integer> invSuffLinks = new ArrayList<>(0);
        public int firstPos = -1;
        public int[] next = new int[128];

        {
            Arrays.fill(next, -1);
        }
    }

    public static State[] buildSuffixAutomaton(CharSequence s) {
        int n = s.length();
        State[] st = new State[Math.max(2, 2 * n - 1)];
        st[0] = new State();
        st[0].suffLink = -1;
        int last = 0;
        int size = 1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int cur = size++;
            st[cur] = new State();
            st[cur].length = i + 1;
            st[cur].firstPos = i;
            int p = last;
            for (; p != -1 && st[p].next[c] == -1; p = st[p].suffLink) {
                st[p].next[c] = cur;
            }
            if (p == -1) {
                st[cur].suffLink = 0;
            } else {
                int q = st[p].next[c];
                if (st[p].length + 1 == st[q].length) {
                    st[cur].suffLink = q;
                } else {
                    int clone = size++;
                    st[clone] = new State();
                    st[clone].length = st[p].length + 1;
                    System.arraycopy(st[q].next, 0, st[clone].next, 0, st[q].next.length);
                    st[clone].suffLink = st[q].suffLink;
                    for (; p != -1 && st[p].next[c] == q; p = st[p].suffLink) {
                        st[p].next[c] = clone;
                    }
                    st[q].suffLink = clone;
                    st[cur].suffLink = clone;
                }
            }
            last = cur;
        }
        for (int i = 1; i < size; i++) {
            st[st[i].suffLink].invSuffLinks.add(i);
        }
        return Arrays.copyOf(st, size);
    }
}
