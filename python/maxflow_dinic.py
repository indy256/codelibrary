# https://en.wikipedia.org/wiki/Dinic%27s_algorithm in O(V^2 * E)

class Edge:
    def __init__(self, t, rev, cap):
        self.t = t
        self.rev = rev
        self.cap = cap
        self.f = 0


def add_edge(graph, s, t, cap):
    graph[s].append(Edge(t, len(graph[t]), cap))
    graph[t].append(Edge(s, len(graph[s]) - 1, 0))


def dinic_bfs(graph, src, dest, dist):
    dist[:] = [-1] * len(dist)
    dist[src] = 0
    Q = [0] * len(graph)
    size_q = 0
    Q[size_q] = src
    size_q += 1
    i = 0
    while i < size_q:
        u = Q[i]
        for e in graph[u]:
            if dist[e.t] < 0 and e.f < e.cap:
                dist[e.t] = dist[u] + 1
                Q[size_q] = e.t
                size_q += 1
        i += 1
    return dist[dest] >= 0


def dinic_dfs(graph, ptr, dist, dest, u, f):
    if u == dest:
        return f
    while ptr[u] < len(graph[u]):
        e = graph[u][ptr[u]]
        if dist[e.t] == dist[u] + 1 and e.f < e.cap:
            df = dinic_dfs(graph, ptr, dist, dest, e.t, min(f, e.cap - e.f))
            if df > 0:
                e.f += df
                graph[e.t][e.rev].f -= df
                return df
        ptr[u] += 1
    return 0


def max_flow(graph, src, dest):
    flow = 0
    dist = [0] * len(graph)
    while dinic_bfs(graph, src, dest, dist):
        ptr = [0] * len(graph)
        while True:
            df = dinic_dfs(graph, ptr, dist, dest, src, float('inf'))
            if df == 0:
                break
            flow += df
    return flow


def test():
    graph = [[] for _ in range(3)]
    add_edge(graph, 0, 1, 3)
    add_edge(graph, 0, 2, 2)
    add_edge(graph, 1, 2, 2)
    assert 4 == max_flow(graph, 0, 2)


test()
