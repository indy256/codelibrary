object FenwickTree {

    // T[pos] += value
    fun add(t: IntArray, pos: Int, value: Int) {
        var i = pos
        while (i < t.size) {
            t[i] += value
            i = i or i + 1
        }
    }

    // sum[0..pos]
    fun sum(t: IntArray, pos: Int): Int {
        var res = 0
        var i = pos
        while (i >= 0) {
            res += t[i]
            i = (i and i + 1) - 1
        }
        return res
    }

    ///////////////////////////////////////////////////

    // T[pos] = max(T[pos], value)
    fun set(t: IntArray, pos: Int, value: Int) {
        var i = pos
        while (i < t.size) {
            t[i] = maxOf(t[i], value)
            i = i or i + 1
        }
    }

    // max[0..pos]
    fun max(t: IntArray, pos: Int): Int {
        var i = pos
        var res = Int.MIN_VALUE
        while (i >= 0) {
            res = maxOf(res, t[i])
            i = (i and i + 1) - 1
        }
        return res
    }

    // Usage example
    @JvmStatic
    fun main(args: Array<String>) {
        val t1 = IntArray(10)
        add(t1, 0, 1)
        add(t1, 9, -2)
        println(-1 == sum(t1, 9))

        val t2 = IntArray(10)
        set(t2, 0, 3)
        set(t2, 3, 2)
        println(3 == max(t2, 9))
    }
}
