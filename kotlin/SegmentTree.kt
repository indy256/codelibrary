class SegmentTree {
    val n: Int
    val tree: Array<Node>

    class Node(var mx: Long = 0, var sum: Long = 0, var add: Long = 0) {
        fun apply(l: Int, r: Int, v: Long) {
            mx += v
            sum += (r - l + 1) * v;
            add += v
        }
    }

    fun unite(a: Node, b: Node): Node {
        val res = Node()
        res.mx = maxOf(a.mx, b.mx)
        res.sum = a.sum + b.sum
        return res
    }

    fun push(x: Int, l: Int, r: Int) {
        val m = (l + r) shr 1
        val y = x + ((m - l + 1) shl 1)
        if (tree[x].add != 0L) {
            tree[x + 1].apply(l, m, tree[x].add)
            tree[y].apply(m + 1, r, tree[x].add)
            tree[x].add = 0
        }
    }

    fun pull(x: Int, y: Int) {
        tree[x] = unite(tree[x + 1], tree[y])
    }

    constructor(n: Int) {
        this.n = n
        tree = Array(2 * n - 1) { Node() }
        build(0, 0, n - 1)
    }

    constructor(v: LongArray) {
        n = v.size
        tree = Array(2 * n - 1) { Node() }
        build(0, 0, n - 1, v)
    }

    fun build(x: Int, l: Int, r: Int) {
        if (l == r) {
            return
        }
        val m = (l + r) shr 1
        val y = x + ((m - l + 1) shl 1)
        build(x + 1, l, m)
        build(y, m + 1, r)
        pull(x, y)
    }

    fun build(x: Int, l: Int, r: Int, v: LongArray) {
        if (l == r) {
            tree[x].apply(l, r, v[l])
            return
        }
        val m = (l + r) shr 1
        val y = x + ((m - l + 1) shl 1)
        build(x + 1, l, m, v)
        build(y, m + 1, r, v)
        pull(x, y)
    }

    operator fun get(ll: Int, rr: Int, x: Int = 0, l: Int = 0, r: Int = 0): Node {
        if (ll <= l && r <= rr) {
            return tree[x]
        }
        val m = (l + r) shr 1
        val y = x + ((m - l + 1) shl 1)
        push(x, l, r)
        val res: Node
        if (rr <= m) {
            res = get(x + 1, l, m, ll, rr)
        } else {
            if (ll > m) {
                res = get(y, m + 1, r, ll, rr)
            } else {
                res = unite(get(x + 1, l, m, ll, rr), get(y, m + 1, r, ll, rr))
            }
        }
        pull(x, y)
        return res
    }

    fun modify(ll: Int, rr: Int, delta: Long, x: Int = 0, l: Int = 0, r: Int = n - 1) {
        if (ll <= l && r <= rr) {
            tree[x].apply(l, r, delta)
            return
        }
        val m = (l + r) shr 1
        val y = x + ((m - l + 1) shl 1)
        push(x, l, r)
        if (ll <= m) {
            modify(ll, rr, delta, x + 1, l, m)
        }
        if (rr > m) {
            modify(ll, rr, delta, y, m + 1, r)
        }
        pull(x, y)
    }

    // calls all FALSE elements to the left of the sought position exactly once
    fun findFirst(ll: Int, rr: Int, f: (Node) -> Boolean, x: Int = 0, l: Int = 0, r: Int = n - 1): Int {
        if (ll <= l && r <= rr && !f(tree[x])) {
            return -1
        }
        if (l == r) {
            return l
        }
        push(x, l, r)
        val m = (l + r) shr 1
        val y = x + ((m - l + 1) shl 1)
        var res = -1
        if (ll <= m) {
            res = findFirst(ll, rr, f, x + 1, l, m)
        }
        if (rr > m && res == -1) {
            res = findFirst(ll, rr, f, y, m + 1, r)
        }
        pull(x, y)
        return res
    }
}

// Returns min(p | p<=rr && sum[ll..p]>=sum). If no such p exists, returns -1
fun sumLowerBound(t: SegmentTree, ll: Int, rr: Int, sum: Long): Int {
    var sumSoFar: Long = 0
    return t.findFirst(ll, rr, { node ->
        if (sumSoFar + node.sum >= sum) return@findFirst true
        sumSoFar += node.sum
        return@findFirst false
    })
}

// Usage example
fun main() {
    val t = SegmentTree(10)
    t.modify(1, 2, 1)
    t.modify(2, 3, 2)
    assert(t[1, 3].mx == 3L)

    val tt = SegmentTree(longArrayOf(1, 2, 10, 20))
    assert(sumLowerBound(tt, 0, tt.n - 1, 12) == 2)
}
