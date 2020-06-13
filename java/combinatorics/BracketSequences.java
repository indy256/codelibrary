package combinatorics;

import java.util.Arrays;

public class BracketSequences {
    public static boolean nextBracketSequence(char[] s) {
        int n = s.length;
        for (int i = n - 1, balance = 0; i >= 0; i--) {
            balance += s[i] == '(' ? -1 : 1;
            if (s[i] == '(' && balance > 0) {
                --balance;
                int open = (n - i - 1 - balance) / 2;
                int close = n - i - 1 - open;
                s[i] = ')';
                Arrays.fill(s, i + 1, i + 1 + open, '(');
                Arrays.fill(s, i + 1 + open, i + open + close, ')');
                return true;
            }
        }
        return false;
    }

    // Usage example
    public static void main(String[] args) {
        char[] s = "((()))".toCharArray();
        do {
            System.out.println(new String(s));
        } while (nextBracketSequence(s));
    }
}
