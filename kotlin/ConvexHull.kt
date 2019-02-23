data class Point(val x: Int, val y: Int)

// Convex hull in O(n*log(n))
fun convexHull(points: Array<Point>): Array<Point> {

    fun isNotRightTurn(p3: List<Point>): Boolean {
        val (a, b, c) = p3
        val cross = (a.x - b.x).toLong() * (c.y - b.y) - (a.y - b.y).toLong() * (c.x - b.x)
        val dot = (a.x - b.x).toLong() * (c.x - b.x) + (a.y - b.y).toLong() * (c.y - b.y)
        return cross < 0 || cross == 0L && dot <= 0
    }

    val sortedPoints = points.sortedArrayWith(compareBy({ it.x }, { it.y }))
    val n = sortedPoints.size
    val hull = arrayListOf<Point>()
    for (i in 0 until 2 * n) {
        val j = if (i < n) i else 2 * n - 1 - i
        while (hull.size >= 2 && isNotRightTurn(hull.takeLast(2) + sortedPoints[j]))
            hull.removeAt(hull.size - 1)
        hull.add(sortedPoints[j])
    }
    return hull.subList(0, hull.size - 1).toTypedArray()
}

// Usage example
fun main() {
    val points = arrayOf(Point(0, 0), Point(2, 2), Point(1, 1), Point(0, 1), Point(1, 0), Point(0, 0))
    val hull = convexHull(points)
    println(hull.contentToString())
}
