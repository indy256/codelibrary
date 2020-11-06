import java.util.PriorityQueue
import kotlin.random.Random

class BinaryHeap(val heap: IntArray, var size: Int) {
    constructor(n: Int) : this(IntArray(n), 0)

    constructor(heap: IntArray) : this(heap, heap.size) {
        for (pos in heap.size / 2 - 1 downTo 0)
            down(pos)
    }

    fun removeMin(): Int {
        val removed = heap[0]
        heap[0] = heap[--size]
        down(0)
        return removed
    }

    fun add(value: Int) {
        heap[size] = value
        up(size++)
    }

    fun up(pos: Int) {
        var i = pos
        while (i > 0) {
            val parent = (i - 1) / 2
            if (heap[i] >= heap[parent])
                break
            heap[i] = heap[parent].also { heap[parent] = heap[i] }
            i = parent
        }
    }

    fun down(pos: Int) {
        var i = pos
        while (true) {
            var child = 2 * i + 1
            if (child >= size)
                break
            if (child + 1 < size && heap[child + 1] < heap[child])
                ++child
            if (heap[i] <= heap[child])
                break
            heap[i] = heap[child].also { heap[child] = heap[i] }
            i = child
        }
    }
}

// random test
fun main() {
    val rnd = Random(1)
    for (step in 1..1000) {
        val n = rnd.nextInt(100) + 1
        val q = PriorityQueue<Int>()
        val h = BinaryHeap(n)
        for (op in 0..999) {
            if (rnd.nextBoolean() && q.size < n) {
                val v = rnd.nextInt()
                q.add(v)
                h.add(v)
            } else if (!q.isEmpty()) {
                if (q.remove() != h.removeMin())
                    throw RuntimeException()
            }
        }
    }
}
