# https://en.wikipedia.org/wiki/Treap

import random

import time


class Treap:
    def __init__(self, key):
        self.key = key
        self.prio = random.randint(0, 1000000000)
        self.size = 1
        self.left = None
        self.right = None

    def update(self):
        self.size = 1 + size(self.left) + size(self.right)


def size(treap):
    return 0 if treap is None else treap.size


def split(root, minRight):
    if root is None:
        return None, None
    if root.key >= minRight:
        left, right = split(root.left, minRight)
        root.left = right
        root.update()
        return left, root
    else:
        left, right = split(root.right, minRight)
        root.right = left
        root.update()
        return root, right


def merge(left, right):
    if left is None:
        return right
    if right is None:
        return left
    if left.prio > right.prio:
        left.right = merge(left.right, right)
        left.update()
        return left
    else:
        right.left = merge(left, right.left)
        right.update()
        return right


def insert(root, key):
    left, right = split(root, key)
    return merge(merge(left, Treap(key)), right)


def remove(root, key):
    left, right = split(root, key)
    return merge(left, split(right, key + 1)[1])


def kth(root, k):
    if k < size(root.left):
        return kth(root.left, k)
    elif k > size(root.left):
        return kth(root.right, k - size(root.left) - 1)
    return root.key


def print_treap(root):
    def dfs_print(root):
        if root is None:
            return
        dfs_print(root.left)
        print(str(root.key) + ' ', end='')
        dfs_print(root.right)

    dfs_print(root)
    print()


def test():
    start = time.time()
    treap = None
    s = set()
    for i in range(100000):
        key = random.randint(0, 10000)
        if random.randint(0, 1) == 0:
            if key in s:
                treap = remove(treap, key)
                s.remove(key)
        elif key not in s:
            treap = insert(treap, key)
            s.add(key)
        assert len(s) == size(treap)

    for i in range(size(treap)):
        assert kth(treap, i) in s
    print(time.time() - start)


test()
