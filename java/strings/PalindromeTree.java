package strings;

class PalindromeNode {
    int start, end;
    int length;
    int[] insertEdg = new int[26];
    int suffixEdg;
}

public class PalindromeTree {
    static final int MAXN = 1000;
    PalindromeNode root1 = new PalindromeNode();
    PalindromeNode root2 = new PalindromeNode();
    PalindromeNode[] tree = new PalindromeNode[MAXN];
    int currNode;
    String s;
    int ptr;

    // Constructor to initialize the palindrome tree for a given string
    public PalindromeTree(String s) {
        this.s = s;
        root1.length = -1;
        root1.suffixEdg = 1;
        root2.length = 0;
        root2.suffixEdg = 1;
        tree[1] = root1;
        tree[2] = root2;
        ptr = 2;
        currNode = 1;
    }

    // Method to insert a character and build the palindrome tree
    void insert(int idx) {
        int tmp = currNode;
        while (true) {
            int curLength = tree[tmp].length;
            if (idx - curLength >= 1 && s.charAt(idx) == s.charAt(idx - curLength - 1))
                break;
            tmp = tree[tmp].suffixEdg;
        }
        if (tree[tmp].insertEdg[s.charAt(idx) - 'a'] != 0) {
            currNode = tree[tmp].insertEdg[s.charAt(idx) - 'a'];
            return;
        }
        ptr++;
        tree[tmp].insertEdg[s.charAt(idx) - 'a'] = ptr;
        tree[ptr] = new PalindromeNode();
        tree[ptr].length = tree[tmp].length + 2;
        tree[ptr].end = idx;
        tree[ptr].start = idx - tree[ptr].length + 1;
        tmp = tree[tmp].suffixEdg;
        currNode = ptr;
        if (tree[currNode].length == 1) {
            tree[currNode].suffixEdg = 2;
            return;
        }
        while (true) {
            int curLength = tree[tmp].length;
            if (idx - curLength >= 1 && s.charAt(idx) == s.charAt(idx - curLength - 1))
                break;
            tmp = tree[tmp].suffixEdg;
        }
        tree[currNode].suffixEdg = tree[tmp].insertEdg[s.charAt(idx) - 'a'];
    }

    // Method to find all distinct palindromic substrings for the string
    public void findPalindromes() {
        int l = s.length();
        for (int i = 0; i < l; i++) {
            insert(i);
        }
        System.out.println("All distinct palindromic substrings for " + s + " :");
        for (int i = 3; i <= ptr; i++) {
            System.out.print((i - 2) + ") ");
            for (int j = tree[i].start; j <= tree[i].end; j++) {
                System.out.print(s.charAt(j));
            }
            System.out.println();
        }
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        // Create instances of PalindromicSubstrings for different strings
        PalindromeTree ps1 = new PalindromeTree("abcbab");
        PalindromeTree ps2 = new PalindromeTree("racecar");
        PalindromeTree ps3 = new PalindromeTree("hello");

        // Call findPalindromes on each instance
        ps1.findPalindromes();
        ps2.findPalindromes();
        ps3.findPalindromes();
    }
}
