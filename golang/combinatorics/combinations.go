package combinatorics

// NextCombination generates the next combination in lexicographic order
// p is a k-element combination from {0, 1, ..., n-1}
// Returns false if no next combination exists
func NextCombination(p []int, n int) bool {
	k := len(p)
	for i := k - 1; i >= 0; i-- {
		if p[i] < n-k+i {
			p[i]++
			for j := i + 1; j < k; j++ {
				p[j] = p[j-1] + 1
			}
			return true
		}
	}
	return false
}

// NextPermutation generates the next permutation in lexicographic order
// Returns false if no next permutation exists
func NextPermutation(a []int) bool {
	n := len(a)
	i := n - 2
	for i >= 0 && a[i] >= a[i+1] {
		i--
	}
	if i < 0 {
		return false
	}
	j := n - 1
	for a[j] <= a[i] {
		j--
	}
	a[i], a[j] = a[j], a[i]
	// reverse a[i+1:]
	for l, r := i+1, n-1; l < r; l, r = l+1, r-1 {
		a[l], a[r] = a[r], a[l]
	}
	return true
}

// Binomial computes the binomial coefficient C(n, k)
func Binomial(n, k int) int64 {
	if k > n-k {
		k = n - k
	}
	result := int64(1)
	for i := 0; i < k; i++ {
		result = result * int64(n-i) / int64(i+1)
	}
	return result
}
