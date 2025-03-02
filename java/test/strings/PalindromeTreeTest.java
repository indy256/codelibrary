package test.strings;

import strings.PalindromeTree;

public class PalindromeTreeTest {

    public static void main(String[] args) {
        // Test 1: Normal Test - Palindrome tree for the string "abcbab"
        PalindromeTree ps1 = new PalindromeTree("abcbab");
        System.out.println("Test 1: Palindrome Tree for 'abcbab'");
        ps1.findPalindromes(); // Expected: "bcb", "aba", "bab" and others

        // Test 2: Normal Test - Palindrome tree for the string "racecar"
        PalindromeTree ps2 = new PalindromeTree("racecar");
        System.out.println("Test 2: Palindrome Tree for 'racecar'");
        ps2.findPalindromes(); // Expected: "racecar", "aceca", "cec"

        // Test 3: Normal Test - Palindrome tree for the string "hello"
        PalindromeTree ps3 = new PalindromeTree("hello");
        System.out.println("Test 3: Palindrome Tree for 'hello'");
        ps3.findPalindromes(); // Expected: "h", "e", "l", "o"

        // Test 4: Edge Test - String with no palindromes "abc"
        PalindromeTree ps4 = new PalindromeTree("abc");
        System.out.println("Test 4: Palindrome Tree for 'abc'");
        ps4.findPalindromes(); // Expected: "a", "b", "c"

        // Test 5: Edge Test - String with two identical characters "aa"
        PalindromeTree ps5 = new PalindromeTree("aa");
        System.out.println("Test 5: Palindrome Tree for 'aa'");
        ps5.findPalindromes(); // Expected: "a", "a", "aa"

        // Test 6: Boundary Test - Empty string
        PalindromeTree ps6 = new PalindromeTree("");
        System.out.println("Test 6: Palindrome Tree for ''");
        ps6.findPalindromes(); // Expected: No output

        // Test 7: Boundary Test - Large input of 1000 'a' characters
        String largeInput = "a".repeat(1000);
        PalindromeTree ps7 = new PalindromeTree(largeInput);
        System.out.println("Test 7: Palindrome Tree for a large input string");
        ps7.findPalindromes(); // Expected: All substrings of length 1 to 1000 are palindromes
    }
}