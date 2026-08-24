package spanningtree

import (
	"container/heap"
	"math"
)

// Edge represents a weighted edge
type Edge struct {
	To   int
	Cost int
}

// Prim computes minimum spanning tree using Prim's algorithm with heap in O(E*log(V))
// https://en.wikipedia.org/wiki/Prim%27s_algorithm
func Prim(edges [][]Edge) (int64, []int) {
	n := len(edges)
	pred := make([]int, n)
	for i := range pred {
		pred[i] = -1
	}
	used := make([]bool, n)
	prio := make([]int, n)
	for i := range prio {
		prio[i] = math.MaxInt32
	}
	prio[0] = 0

	pq := &priorityQueue{{priority: 0, vertex: 0}}
	heap.Init(pq)
	var res int64

	for pq.Len() > 0 {
		item := heap.Pop(pq).(*pqItem)
		u := item.vertex
		if used[u] {
			continue
		}
		used[u] = true
		res += int64(item.priority)
		for _, e := range edges[u] {
			if !used[e.To] && prio[e.To] > e.Cost {
				prio[e.To] = e.Cost
				pred[e.To] = u
				heap.Push(pq, &pqItem{priority: e.Cost, vertex: e.To})
			}
		}
	}
	return res, pred
}

type pqItem struct {
	priority int
	vertex   int
}

type priorityQueue []*pqItem

func (pq priorityQueue) Len() int            { return len(pq) }
func (pq priorityQueue) Less(i, j int) bool  { return pq[i].priority < pq[j].priority }
func (pq priorityQueue) Swap(i, j int)       { pq[i], pq[j] = pq[j], pq[i] }
func (pq *priorityQueue) Push(x interface{}) { *pq = append(*pq, x.(*pqItem)) }
func (pq *priorityQueue) Pop() interface{} {
	old := *pq
	n := len(old)
	item := old[n-1]
	*pq = old[:n-1]
	return item
}
