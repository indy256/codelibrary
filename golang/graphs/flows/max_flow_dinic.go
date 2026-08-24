package flows

import "math"

// MaxFlowDinic implements Dinic's algorithm for maximum flow in O(V^2 * E)
// https://en.wikipedia.org/wiki/Dinic%27s_algorithm
type MaxFlowDinic struct {
	graph [][]Edge
	dist  []int
}

type Edge struct {
	to, rev, cap, f int
}

func NewMaxFlowDinic(n int) *MaxFlowDinic {
	return &MaxFlowDinic{
		graph: make([][]Edge, n),
		dist:  make([]int, n),
	}
}

func (mf *MaxFlowDinic) AddEdge(s, t, cap int) {
	mf.graph[s] = append(mf.graph[s], Edge{t, len(mf.graph[t]), cap, 0})
	mf.graph[t] = append(mf.graph[t], Edge{s, len(mf.graph[s]) - 1, 0, 0})
}

func (mf *MaxFlowDinic) bfs(src, dest int) bool {
	for i := range mf.dist {
		mf.dist[i] = -1
	}
	mf.dist[src] = 0
	queue := []int{src}
	for i := 0; i < len(queue); i++ {
		u := queue[i]
		for _, e := range mf.graph[u] {
			if mf.dist[e.to] < 0 && e.f < e.cap {
				mf.dist[e.to] = mf.dist[u] + 1
				queue = append(queue, e.to)
			}
		}
	}
	return mf.dist[dest] >= 0
}

func (mf *MaxFlowDinic) dfs(ptr []int, dest, u, f int) int {
	if u == dest {
		return f
	}
	for ; ptr[u] < len(mf.graph[u]); ptr[u]++ {
		e := &mf.graph[u][ptr[u]]
		if mf.dist[e.to] == mf.dist[u]+1 && e.f < e.cap {
			df := mf.dfs(ptr, dest, e.to, min(f, e.cap-e.f))
			if df > 0 {
				e.f += df
				mf.graph[e.to][e.rev].f -= df
				return df
			}
		}
	}
	return 0
}

func (mf *MaxFlowDinic) MaxFlow(src, dest int) int {
	flow := 0
	for mf.bfs(src, dest) {
		ptr := make([]int, len(mf.graph))
		for {
			df := mf.dfs(ptr, dest, src, math.MaxInt32)
			if df == 0 {
				break
			}
			flow += df
		}
	}
	return flow
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}
