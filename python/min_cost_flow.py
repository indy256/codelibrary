# https://en.wikipedia.org/wiki/Minimum-cost_flow_problem in O(V^2 * E)
from heapq import *


class Edge:
    def __init__(self, to, cap, cost, rev):
        self.to = to
        self.cap = cap
        self.cost = cost
        self.rev = rev
        self.f = 0


def add_edge(graph, s, t, cap, cost):
    graph[s].append(Edge(t, cap, cost, len(graph[t])))
    graph[t].append(Edge(s, 0, -cost, len(graph[s]) - 1))


def bellman_ford(graph, s, dist):
    n = len(graph)
    dist[:] = [float('inf')] * n
    dist[s] = 0
    inqueue = [False] * n
    q = [0] * n
    qt = 0
    q[qt] = s
    qt += 1
    qh = 0
    while (qh - qt) % n != 0:
        u = q[qh % n]
        inqueue[u] = False
        for i in range(len(graph[u])):
            e = graph[u][i]
            if e.cap <= e.f:
                continue
            v = e.to
            ndist = dist[u] + e.cost
            if dist[v] > ndist:
                dist[v] = ndist
                if not inqueue[v]:
                    inqueue[v] = True
                    q[qt % n] = v
                    qt += 1
        qh += 1


def min_cost_flow(graph, s, t, maxf):
    n = len(graph)
    prio = [0] * n
    curflow = [0] * n
    prevedge = [0] * n
    prevnode = [0] * n
    pot = [0] * n

    bellman_ford(graph, s, pot)  # bellmanFord invocation can be skipped if edges costs are non-negative
    flow = 0
    flow_cost = 0
    while flow < maxf:
        q = [(0, s)]
        prio[:] = [float('inf')] * n
        prio[s] = 0
        finished = [False] * n
        curflow[s] = float('inf')
        while not finished[t] and q:
            (prio_u, u) = heappop(q)
            if prio_u != prio[u]:
                continue
            finished[u] = True
            for i in range(len(graph[u])):
                e = graph[u][i]
                if e.f >= e.cap:
                    continue
                v = e.to
                nprio = prio[u] + e.cost + pot[u] - pot[v]
                if prio[v] > nprio:
                    prio[v] = nprio
                    heappush(q, (nprio, v))
                    prevnode[v] = u
                    prevedge[v] = i
                    curflow[v] = min(curflow[u], e.cap - e.f)
        if prio[t] == float('inf'):
            break
        for i in range(n):
            if finished[i]:
                pot[i] += prio[i] - prio[t]
        df = min(curflow[t], maxf - flow)
        flow += df
        v = t
        while v != s:
            e = graph[prevnode[v]][prevedge[v]]
            e.f += df
            graph[v][e.rev].f -= df
            flow_cost += df * e.cost
            v = prevnode[v]
    return flow, flow_cost


def test():
    graph = [[] for _ in range(3)]

    add_edge(graph, 0, 1, 3, 1)
    add_edge(graph, 0, 2, 2, 1)
    add_edge(graph, 1, 2, 2, 1)
    assert (4, 6) == min_cost_flow(graph, 0, 2, float('inf'))


test()
