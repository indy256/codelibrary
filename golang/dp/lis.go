package dp

import "sort"

// LIS computes the length of the longest increasing subsequence in O(n log n)
func LIS(a []int) int {
	var tail []int
	for _, x := range a {
		pos := sort.SearchInts(tail, x)
		if pos == len(tail) {
			tail = append(tail, x)
		} else {
			tail[pos] = x
		}
	}
	return len(tail)
}
