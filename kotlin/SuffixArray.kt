fun suffixArray(S: CharSequence): IntArray {
    val n = S.length

    // Stable sort of characters.
    // Same characters are sorted by their position in descending order.
    // E.g. last character which represents suffix of length 1 should be ordered first among same characters.
    val sa = S.indices.reversed().sortedBy { S[it] }.toIntArray()
    val classes = S.chars().toArray()
    // sa[i] - suffix on i'th position after sorting by first len characters
    // classes[i] - equivalence class of the i'th suffix after sorting by first len characters
    var len = 1
    while (len < n) {
        // Calculate classes for suffixes of length len * 2
        val c = classes.copyOf()
        for (i in 0 until n) {
            // Condition sa[i - 1] + len < n emulates 0-symbol at the end of the string.
            // A separate class is created for each suffix followed by emulated 0-symbol.
            classes[sa[i]] =
                    if (i > 0 && c[sa[i - 1]] == c[sa[i]] && sa[i - 1] + len < n
                            && c[sa[i - 1] + len / 2] == c[sa[i] + len / 2]) classes[sa[i - 1]]
                    else i
        }
        // Suffixes are already sorted by first len characters
        // Now sort suffixes by first len * 2 characters
        val cnt = IntArray(n) { it }
        val s = sa.copyOf()
        for (i in 0 until n) {
            // s[i] - order of suffixes sorted by first len characters
            // (s[i] - len) - order of suffixes sorted only by second len characters
            val s1 = s[i] - len
            // sort only suffixes of length > len, others are already sorted
            if (s1 >= 0)
                sa[cnt[classes[s1]]++] = s1
        }
        len *= 2
    }
    return sa
}

// Usage example
fun main() {
    val s = "abcab"
    val sa = suffixArray(s)
    // print suffixes in lexicographic order

    // print suffixes in lexicographic order
    for (p in sa) println(s.substring(p))
}
