package matchings

// MaxBipartiteMatching finds maximum matching in bipartite graph in O(V * E)
// https://en.wikipedia.org/wiki/Matching_(graph_theory)#In_unweighted_bipartite_graphs
// graph[u] contains neighbors of left vertex u in the right partition
func MaxBipartiteMatching(graph [][]int, n2 int) int {
	matching := make([]int, n2)
	for i := range matching {
		matching[i] = -1
	}
	matches := 0
	for u := 0; u < len(graph); u++ {
		vis := make([]bool, len(graph))
		if findPath(graph, u, matching, vis) {
			matches++
		}
	}
	return matches
}

func findPath(graph [][]int, u1 int, matching []int, vis []bool) bool {
	vis[u1] = true
	for _, v := range graph[u1] {
		u2 := matching[v]
		if u2 == -1 || (!vis[u2] && findPath(graph, u2, matching, vis)) {
			matching[v] = u1
			return true
		}
	}
	return false
}
