# https://en.wikipedia.org/wiki/Dijkstra's_algorithm in O(E*log(V))
from heapq import *


class Edge:
    def __init__(self, t, cost):
        self.t = t
        self.cost = cost


def dijkstra(graph, s):
    n = len(graph)
    pred = [-1] * n
    prio = [float('inf')] * n
    prio[s] = 0
    q = [(0, s)]
    while q:
        (prio_u, u) = heappop(q)
        if prio_u != prio[u]:
            continue
        for e in graph[u]:
            v = e.t
            nprio = prio[u] + e.cost
            if prio[v] > nprio:
                prio[v] = nprio
                pred[v] = u
                heappush(q, (nprio, v))
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
