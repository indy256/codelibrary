class SegmentTree {

    val n: Int
    val tree: Array<node>

    class node(var mx: Long = 0, var add: Long = 0) {
        fun apply(l: Int, r: Int, v: Long) {
            mx += v
            add += v
        }
    }

    fun unite(a: node, b: node): node {
        val res = node()
        res.mx = Math.max(a.mx, b.mx)
        return res
    }

    fun push(x: Int, l: Int, r: Int) {
        val y = (l + r) shr 1
        val z = x + ((y - l + 1) shl 1)
        // push from x into (x + 1) and z
        if (tree[x].add != 0L) {
            tree[x + 1].apply(l, y, tree[x].add)
            tree[z].apply(y + 1, r, tree[x].add)
            tree[x].add = 0
        }
    }

    fun pull(x: Int, z: Int) {
        tree[x] = unite(tree[x + 1], tree[z])
    }

    fun build(x: Int, l: Int, r: Int) {
        if (l == r) {
            return
        }
        val y = (l + r) shr 1
        val z = x + ((y - l + 1) shl 1)
        build(x + 1, l, y)
        build(z, y + 1, r)
        pull(x, z)
    }

    fun build(x: Int, l: Int, r: Int, v: LongArray) {
        if (l == r) {
            tree[x].apply(l, r, v[l])
            return
        }
        val y = (l + r) shr 1
        val z = x + ((y - l + 1) shl 1)
        build(x + 1, l, y, v)
        build(z, y + 1, r, v)
        pull(x, z)
    }

    operator fun get(x: Int, l: Int, r: Int, ll: Int, rr: Int): node {
        if (ll <= l && r <= rr) {
            return tree[x]
        }
        val y = (l + r) shr 1
        val z = x + ((y - l + 1) shl 1)
        push(x, l, r)
        val res: node
        if (rr <= y) {
            res = get(x + 1, l, y, ll, rr)
        } else {
            if (ll > y) {
                res = get(z, y + 1, r, ll, rr)
            } else {
                res = unite(get(x + 1, l, y, ll, rr), get(z, y + 1, r, ll, rr))
            }
        }
        pull(x, z)
        return res
    }

    fun modify(x: Int, l: Int, r: Int, ll: Int, rr: Int, v: Long) {
        if (ll <= l && r <= rr) {
            tree[x].apply(l, r, v)
            return
        }
        val y = (l + r) shr 1
        val z = x + ((y - l + 1) shl 1)
        push(x, l, r)
        if (ll <= y) {
            modify(x + 1, l, y, ll, rr, v)
        }
        if (rr > y) {
            modify(z, y + 1, r, ll, rr, v)
        }
        pull(x, z)
    }

    constructor(n: Int) {
        this.n = n
        tree = Array(2 * n - 1, { node() })
        build(0, 0, n - 1)
    }

    constructor(v: LongArray) {
        n = v.size
        tree = Array(2 * n - 1, { node() })
        build(0, 0, n - 1, v)
    }

    operator fun get(ll: Int, rr: Int): node {
        assert(0 <= ll && ll <= rr && rr <= n - 1)
        return get(0, 0, n - 1, ll, rr)
    }

    operator fun get(p: Int): node {
        assert(0 <= p && p <= n - 1)
        return get(0, 0, n - 1, p, p)
    }

    fun modify(ll: Int, rr: Int, v: Long) {
        assert(0 <= ll && ll <= rr && rr <= n - 1)
        modify(0, 0, n - 1, ll, rr, v)
    }
}

// Usage example
fun main() {
    val t = SegmentTree(10)
    t.modify(1, 2, 1)
    t.modify(2, 3, 2)
    println(t[1, 3].mx)
}
