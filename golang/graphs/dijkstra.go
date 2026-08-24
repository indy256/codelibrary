package graphs

import "math"

// Edge represents a weighted directed edge
type Edge struct {
	To   int
	Cost int
}

// Dijkstra computes shortest paths from source s in O(V^2)
// https://en.wikipedia.org/wiki/Dijkstra's_algorithm
func Dijkstra(graph [][]Edge, s int, dist []int, pred []int) {
	n := len(graph)
	for i := range dist {
		dist[i] = math.MaxInt32
		pred[i] = -1
	}
	dist[s] = 0
	visited := make([]bool, n)

	for i := 0; i < n; i++ {
		u := -1
		for j := 0; j < n; j++ {
			if !visited[j] && (u == -1 || dist[u] > dist[j]) {
				u = j
			}
		}
		if dist[u] == math.MaxInt32 {
			break
		}
		visited[u] = true
		for _, e := range graph[u] {
			if newDist := dist[u] + e.Cost; dist[e.To] > newDist {
				dist[e.To] = newDist
				pred[e.To] = u
			}
		}
	}
}
