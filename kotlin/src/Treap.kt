import java.util.Random
import java.util.TreeSet

// https://en.wikipedia.org/wiki/Treap
object Treap {
    val random = Random()

    data class Treap(
            val key: Int,
            var left: Treap? = null,
            var right: Treap? = null,
            var size: Int = 1,
            val prio: Long = random.nextLong()
    ) {
        fun update() {
            size = 1 + getSize(left) + getSize(right)
        }
    }

    fun getSize(root: Treap?) = root?.size ?: 0

    fun split(root: Treap?, minRight: Int): Pair<Treap?, Treap?> {
        if (root == null)
            return Pair(null, null)
        if (root.key >= minRight) {
            val leftSplit = split(root.left, minRight)
            root.left = leftSplit.second
            root.update()
            return Pair(leftSplit.first, root)
        } else {
            val rightSplit = split(root.right, minRight)
            root.right = rightSplit.first
            root.update()
            return Pair(root, rightSplit.second)
        }
    }

    fun merge(left: Treap?, right: Treap?): Treap? {
        if (left == null)
            return right
        if (right == null)
            return left
        if (left.prio > right.prio) {
            left.right = merge(left.right, right)
            left.update()
            return left
        } else {
            right.left = merge(left, right.left)
            right.update()
            return right
        }
    }

    fun insert(root: Treap?, key: Int): Treap? {
        val t = split(root, key)
        return merge(merge(t.first, Treap(key)), t.second)
    }

    fun remove(root: Treap?, key: Int): Treap? {
        val t = split(root, key)
        return merge(t.first, split(t.second, key + 1).second)
    }

    fun kth(root: Treap, k: Int): Int {
        if (k < getSize(root.left))
            return kth(root.left!!, k)
        else if (k > getSize(root.left))
            return kth(root.right!!, k - getSize(root.left) - 1)
        return root.key
    }

    fun print(root: Treap?) {
        if (root == null)
            return
        print(root.left)
        println(root.key)
        print(root.right)
    }

    // random test
    @JvmStatic
    fun main(args: Array<String>) {
        val time = System.currentTimeMillis()
        var treap: Treap? = null
        val set = TreeSet<Int>()
        for (i in 0..999999) {
            val key = random.nextInt(100000)
            if (random.nextBoolean()) {
                treap = remove(treap, key)
                set.remove(key)
            } else if (!set.contains(key)) {
                treap = insert(treap, key)
                set.add(key)
            }
            if (set.size != getSize(treap))
                throw RuntimeException()
        }
        for (i in 0 until getSize(treap)) {
            if (!set.contains(kth(treap!!, i)))
                throw RuntimeException()
        }
        println(System.currentTimeMillis() - time)
    }
}
