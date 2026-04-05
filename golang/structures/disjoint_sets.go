package structures

// DisjointSets - Union-Find with path compression and union by rank
// https://en.wikipedia.org/wiki/Disjoint-set_data_structure
type DisjointSets struct {
	parent []int
	rank   []int
}

func NewDisjointSets(size int) *DisjointSets {
	ds := &DisjointSets{
		parent: make([]int, size),
		rank:   make([]int, size),
	}
	for i := range ds.parent {
		ds.parent[i] = i
	}
	return ds
}

func (ds *DisjointSets) Find(x int) int {
	if ds.parent[x] != x {
		ds.parent[x] = ds.Find(ds.parent[x])
	}
	return ds.parent[x]
}

func (ds *DisjointSets) Unite(a, b int) bool {
	a = ds.Find(a)
	b = ds.Find(b)
	if a == b {
		return false
	}
	if ds.rank[a] < ds.rank[b] {
		a, b = b, a
	}
	ds.parent[b] = a
	if ds.rank[a] == ds.rank[b] {
		ds.rank[a]++
	}
	return true
}
