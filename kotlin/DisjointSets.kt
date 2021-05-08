object DisjointSets {

    fun root(p: IntArray, x: Int): Int {
        return if (x == p[x]) {
            x
        } else {
            p[x] = root(p, p[x])
            p[x]
        }
    }

    fun unite(p: IntArray, a: Int, b: Int): Boolean {
        val ra = root(p, a)
        val rb = root(p, b)
        p[ra] = rb
        return ra != rb
    }

    fun createSets(size: Int) = IntArray(size) { it }

    // Usage example
    @JvmStatic
    fun main(args: Array<String>) {
        val p = createSets(10)
        println(false == (root(p, 0) == root(p, 9)))
        unite(p, 0, 9)
        println(true == (root(p, 0) == root(p, 9)))
    }
}
