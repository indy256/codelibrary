class SegmentTreeIntervalAddMax(val n: Int) {
    val tmax = IntArray(4 * n)
    val tadd = IntArray(4 * n) // tadd[i] applies to tmax[i], tadd[2*i+1] and tadd[2*i+2]

    fun max(from: Int, to: Int, node: Int = 0, left: Int = 0, right: Int = n - 1): Int {
        if (from == left && to == right) {
            return tmax[node] + tadd[node]
        }
        push(node)
        val mid = left + right shr 1
        var res = Int.MIN_VALUE
        if (from <= mid)
            res = maxOf(res, max(from, minOf(to, mid), 2 * node + 1, left, mid))
        if (to > mid)
            res = maxOf(res, max(maxOf(from, mid + 1), to, 2 * node + 2, mid + 1, right))
        return res
    }

    fun add(from: Int, to: Int, delta: Int, node: Int = 0, left: Int = 0, right: Int = n - 1) {
        if (from == left && to == right) {
            tadd[node] += delta
            return
        }
        push(node) // this push may be omitted for add, but is necessary for other operations such as set
        val mid = left + right shr 1
        if (from <= mid)
            add(from, minOf(to, mid), delta, 2 * node + 1, left, mid)
        if (to > mid)
            add(maxOf(from, mid + 1), to, delta, 2 * node + 2, mid + 1, right)
        tmax[node] = maxOf(tmax[2 * node + 1] + tadd[2 * node + 1], tmax[2 * node + 2] + tadd[2 * node + 2])
    }

    fun push(node: Int) {
        tmax[node] += tadd[node]
        tadd[2 * node + 1] += tadd[node]
        tadd[2 * node + 2] += tadd[node]
        tadd[node] = 0
    }
}

// Usage example
fun main(args: Array<String>) {
    val t = SegmentTreeIntervalAddMax(10)
    t.add(0, 9, 1)
    t.add(2, 4, 2)
    t.add(3, 5, 3)
    println(6 == t.max(0, 9))
    println(6 == t.tmax[0] + t.tadd[0])
    println(1 == t.max(0, 0))
}
