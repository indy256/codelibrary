// https://en.wikipedia.org/wiki/Persistent_data_structure
object PersistentTree {

    data class Node(
            val left: Node? = null,
            val right: Node? = null,
            val sum: Int = (left?.sum ?: 0) + (right?.sum ?: 0)
    )

    fun build(left: Int, right: Int): Node {
        if (left == right)
            return Node(null, null)
        val mid = left + right shr 1
        return Node(build(left, mid), build(mid + 1, right))
    }

    fun sum(a: Int, b: Int, root: Node, left: Int, right: Int): Int {
        if (a == left && b == right)
            return root.sum
        val mid = left + right shr 1
        var s = 0
        if (a <= mid)
            s += sum(a, minOf(b, mid), root.left!!, left, mid)
        if (b > mid)
            s += sum(maxOf(a, mid + 1), b, root.right!!, mid + 1, right)
        return s
    }

    fun set(pos: Int, value: Int, root: Node, left: Int, right: Int): Node {
        if (left == right)
            return Node(null, null, value)
        val mid = left + right shr 1
        return if (pos <= mid)
            Node(set(pos, value, root.left!!, left, mid), root.right)
        else
            Node(root.left, set(pos, value, root.right!!, mid + 1, right))
    }

    // Usage example
    @JvmStatic
    fun main(args: Array<String>) {
        val n = 10
        val t1 = build(0, n - 1)
        val t2 = set(0, 1, t1, 0, n - 1)
        println(0 == sum(0, 9, t1, 0, n - 1))
        println(1 == sum(0, 9, t2, 0, n - 1))
    }
}
