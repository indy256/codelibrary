# https://en.wikipedia.org/wiki/Dijkstra's_algorithm in O(V^2)
class Edge:
    def __init__(self, t, cost):
        self.t = t
        self.cost = cost


def dijkstra(graph, s):
    n = len(graph)
    pred = [-1] * n
    prio = [float('inf')] * n
    prio[s] = 0
    visited = [False] * n
    for i in range(n):
        u = -1
        for j in range(n):
            if not visited[j] and (u == -1 or prio[u] > prio[j]):
                u = j

        if prio[u] == float('inf'):
            break

        visited[u] = True

        for e in graph[u]:
            v = e.t
            nprio = prio[u] + e.cost
            if prio[v] > nprio:
                prio[v] = nprio
                pred[v] = u

    return prio, pred


def test():
    g = [[] for _ in range(3)]
    g[0].append(Edge(1, 3))
    g[0].append(Edge(2, 2))
    g[1].append(Edge(2, -2))

    dist, pred = dijkstra(g, 0)
    assert dist == [0, 3, 1]
    assert pred == [-1, 0, 1]


test()
