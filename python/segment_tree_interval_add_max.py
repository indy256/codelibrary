class SegmentTree:
    def __init__(self, n):
        self.n = n
        self.tmax = [0] * (4 * n)
        self.tadd = [0] * (4 * n)

    def max(self, a, b, node=0, left=0, right=None):
        if right is None:
            right = self.n - 1
        if a == left and b == right:
            return self.tmax[node] + self.tadd[node]
        self.push(node)
        mid = (left + right) >> 1
        res = float('-inf')
        if a <= mid:
            res = max(res, self.max(a, min(mid, b), 2 * node + 1, left, mid))
        if b > mid:
            res = max(res, self.max(max(a, mid + 1), b, 2 * node + 2, mid + 1, right))
        return res

    def add(self, a, b, delta, node=0, left=0, right=None):
        if right is None:
            right = self.n - 1
        if a == left and b == right:
            self.tadd[node] += delta
            return
        self.push(node)
        mid = (left + right) >> 1
        if a <= mid:
            self.add(a, min(mid, b), delta, 2 * node + 1, left, mid)
        if b > mid:
            self.add(max(a, mid + 1), b, delta, 2 * node + 2, mid + 1, right)
        self.tmax[node] = max(self.tmax[2 * node + 1] + self.tadd[2 * node + 1],
                              self.tmax[2 * node + 2] + self.tadd[2 * node + 2])

    def push(self, node):
        self.tmax[node] += self.tadd[node]
        self.tadd[2 * node + 1] += self.tadd[node]
        self.tadd[2 * node + 2] += self.tadd[node]
        self.tadd[node] = 0


def test():
    tree = SegmentTree(10)
    tree.add(0, 9, 1)
    tree.add(2, 4, 2)
    tree.add(3, 5, 3)
    assert 6 == tree.max(0, 9)
    assert 6 == tree.tmax[0] + tree.tadd[0]
    assert 1 == tree.max(0, 0)


test()
