package graphs

import "math"

// BellmanFord computes shortest paths from source s, handling negative weights
// Returns true if no negative cycle exists
// https://en.wikipedia.org/wiki/Bellman-Ford_algorithm
func BellmanFord(graph [][]Edge, s int, dist []int, pred []int) bool {
	n := len(graph)
	for i := range dist {
		dist[i] = math.MaxInt32
		pred[i] = -1
	}
	dist[s] = 0

	for iter := 0; iter < n-1; iter++ {
		for u := 0; u < n; u++ {
			if dist[u] == math.MaxInt32 {
				continue
			}
			for _, e := range graph[u] {
				if newDist := dist[u] + e.Cost; dist[e.To] > newDist {
					dist[e.To] = newDist
					pred[e.To] = u
				}
			}
		}
	}

	// Check for negative cycles
	for u := 0; u < n; u++ {
		if dist[u] == math.MaxInt32 {
			continue
		}
		for _, e := range graph[u] {
			if dist[u]+e.Cost < dist[e.To] {
				return false
			}
		}
	}
	return true
}
