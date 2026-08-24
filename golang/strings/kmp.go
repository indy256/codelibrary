package strings

// PrefixFunction computes the KMP failure function
// https://en.wikipedia.org/wiki/Knuth-Morris-Pratt_algorithm
func PrefixFunction(s string) []int {
	p := make([]int, len(s))
	k := 0
	for i := 1; i < len(s); i++ {
		for k > 0 && s[k] != s[i] {
			k = p[k-1]
		}
		if s[k] == s[i] {
			k++
		}
		p[i] = k
	}
	return p
}

// FindSubstring finds the first occurrence of needle in haystack using KMP
// Returns -1 if not found
func FindSubstring(haystack, needle string) int {
	m := len(needle)
	if m == 0 {
		return 0
	}
	p := PrefixFunction(needle)
	k := 0
	for i := 0; i < len(haystack); i++ {
		for k > 0 && needle[k] != haystack[i] {
			k = p[k-1]
		}
		if needle[k] == haystack[i] {
			k++
		}
		if k == m {
			return i + 1 - m
		}
	}
	return -1
}
