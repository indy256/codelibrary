class BinaryHeap:
    def __init__(self, n):
        self.heap = [0] * n
        self.size = 0

    def remove_min(self):
        removed = self.heap[0]
        self.size -= 1
        self.heap[0] = self.heap[self.size]
        self._down(0)
        return removed

    def add(self, value):
        self.heap[self.size] = value
        self._up(self.size)
        self.size += 1

    def _up(self, pos):
        while pos > 0:
            parent = (pos - 1) // 2
            if self.heap[pos] >= self.heap[parent]:
                break
            self.heap[pos], self.heap[parent] = self.heap[parent], self.heap[pos]
            pos = parent

    def _down(self, pos):
        while True:
            child = 2 * pos + 1
            if child >= self.size:
                break
            if child + 1 < self.size and self.heap[child + 1] < self.heap[child]:
                child += 1
            if self.heap[pos] <= self.heap[child]:
                break
            self.heap[pos], self.heap[child] = self.heap[child], self.heap[pos]
            pos = child


def test():
    h = BinaryHeap(10)
    h.add(3)
    h.add(1)
    h.add(2)
    print(h.remove_min())
    print(h.remove_min())
    print(h.remove_min())


test()
