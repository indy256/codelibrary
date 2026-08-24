package structures

// SegmentTree with lazy propagation (Add/Max operations)
type SegmentTree struct {
	n     int
	value []int
	delta []int
}

func NewSegmentTree(n int) *SegmentTree {
	return &SegmentTree{
		n:     n,
		value: make([]int, 4*n),
		delta: make([]int, 4*n),
	}
}

func (t *SegmentTree) pushDelta(root, left, right int) {
	t.value[root] += t.delta[root]
	t.delta[2*root+1] += t.delta[root]
	t.delta[2*root+2] += t.delta[root]
	t.delta[root] = 0
}

func (t *SegmentTree) Query(from, to int) int {
	return t.query(from, to, 0, 0, t.n-1)
}

func (t *SegmentTree) query(from, to, root, left, right int) int {
	if from == left && to == right {
		return t.value[root] + t.delta[root]
	}
	t.pushDelta(root, left, right)
	mid := (left + right) >> 1
	if from <= mid && to > mid {
		l := t.query(from, min(to, mid), root*2+1, left, mid)
		r := t.query(max(from, mid+1), to, root*2+2, mid+1, right)
		if l > r {
			return l
		}
		return r
	} else if from <= mid {
		return t.query(from, min(to, mid), root*2+1, left, mid)
	}
	return t.query(max(from, mid+1), to, root*2+2, mid+1, right)
}

func (t *SegmentTree) Modify(from, to, delta int) {
	t.modify(from, to, delta, 0, 0, t.n-1)
}

func (t *SegmentTree) modify(from, to, delta, root, left, right int) {
	if from == left && to == right {
		t.delta[root] += delta
		return
	}
	t.pushDelta(root, left, right)
	mid := (left + right) >> 1
	if from <= mid {
		t.modify(from, min(to, mid), delta, 2*root+1, left, mid)
	}
	if to > mid {
		t.modify(max(from, mid+1), to, delta, 2*root+2, mid+1, right)
	}
	lv := t.value[2*root+1] + t.delta[2*root+1]
	rv := t.value[2*root+2] + t.delta[2*root+2]
	if lv > rv {
		t.value[root] = lv
	} else {
		t.value[root] = rv
	}
}
