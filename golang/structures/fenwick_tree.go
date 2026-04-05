package structures

// FenwickTree (Binary Indexed Tree) for prefix sum queries and point updates
// https://en.wikipedia.org/wiki/Fenwick_tree
type FenwickTree struct {
	tree []int
	n    int
}

func NewFenwickTree(n int) *FenwickTree {
	return &FenwickTree{tree: make([]int, n), n: n}
}

func (f *FenwickTree) Update(i, delta int) {
	for ; i < f.n; i |= i + 1 {
		f.tree[i] += delta
	}
}

func (f *FenwickTree) PrefixSum(i int) int {
	sum := 0
	for ; i >= 0; i = (i & (i + 1)) - 1 {
		sum += f.tree[i]
	}
	return sum
}

func (f *FenwickTree) RangeSum(from, to int) int {
	res := f.PrefixSum(to)
	if from > 0 {
		res -= f.PrefixSum(from - 1)
	}
	return res
}
