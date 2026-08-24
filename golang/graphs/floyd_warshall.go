package graphs

import "math"

const INF = math.MaxInt32 / 2

// FloydWarshall computes all-pairs shortest paths in O(V^3)
// Modifies dist in-place. Returns predecessor matrix, or nil if negative cycle exists.
// https://en.wikipedia.org/wiki/Floyd-Warshall_algorithm
func FloydWarshall(dist [][]int) [][]int {
	n := len(dist)
	pred := make([][]int, n)
	for i := range pred {
		pred[i] = make([]int, n)
		for j := range pred[i] {
			if i == j || dist[i][j] == INF {
				pred[i][j] = -1
			} else {
				pred[i][j] = i
			}
		}
	}

	for k := 0; k < n; k++ {
		for i := 0; i < n; i++ {
			for j := 0; j < n; j++ {
				if dist[i][j] > dist[i][k]+dist[k][j] {
					dist[i][j] = dist[i][k] + dist[k][j]
					pred[i][j] = pred[k][j]
				}
			}
		}
	}

	for i := 0; i < n; i++ {
		if dist[i][i] < 0 {
			return nil
		}
	}
	return pred
}
