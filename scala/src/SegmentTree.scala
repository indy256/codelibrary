import java.util.Random

class SegmentTree(n: Int) {
  // Modify the following 5 methods to implement your custom operations on the tree.
  // This example implements Add/Max operations. Operations like Add/Sum, Set/Max can also be implemented.
  def modifyOperation(x: Int, y: Int) = x + y

  // query (or combine) operation
  def queryOperation(leftValue: Int, rightValue: Int): Int = math.max(leftValue, rightValue)

  def deltaEffectOnSegment(delta: Int, segmentLength: Int): Int = {
    if (delta == getNeutralDelta) return getNeutralDelta
    // Here you must write a fast equivalent of following slow code:
    // int result = delta;
    // for (int i = 1; i < segmentLength; i++) result = queryOperation(result, delta);
    // return result;
    delta
  }

  def getNeutralDelta = 0

  def getInitValue = 0

  val value = new Array[Int](4 * n)
  val delta = new Array[Int](4 * n) // delta[i] affects value[i], delta[2*i+1] and delta[2*i+2]

  init(0, 0, n - 1)

  def init(root: Int, left: Int, right: Int) {
    if (left == right) {
      value(root) = getInitValue
      delta(root) = getNeutralDelta
    } else {
      val mid = (left + right) >> 1
      init(2 * root + 1, left, mid)
      init(2 * root + 2, mid + 1, right)
      value(root) = queryOperation(value(2 * root + 1), value(2 * root + 2))
      delta(root) = getNeutralDelta
    }
  }

  def joinValueWithDelta(value: Int, delta: Int): Int = {
    if (delta == getNeutralDelta) return value
    modifyOperation(value, delta)
  }

  def joinDeltas(delta1: Int, delta2: Int): Int = {
    if (delta1 == getNeutralDelta) return delta2
    if (delta2 == getNeutralDelta) return delta1
    modifyOperation(delta1, delta2)
  }

  def pushDelta(root: Int, left: Int, right: Int) {
    value(root) = joinValueWithDelta(value(root), deltaEffectOnSegment(delta(root), right - left + 1))
    delta(2 * root + 1) = joinDeltas(delta(2 * root + 1), delta(root))
    delta(2 * root + 2) = joinDeltas(delta(2 * root + 2), delta(root))
    delta(root) = getNeutralDelta
  }

  def query(from: Int, to: Int): Int = query(from, to, 0, 0, n - 1)

  def query(from: Int, to: Int, root: Int, left: Int, right: Int): Int = {
    if (from == left && to == right) return joinValueWithDelta(value(root), deltaEffectOnSegment(delta(root), right - left + 1))
    pushDelta(root, left, right)
    val mid: Int = (left + right) >> 1
    if (from <= mid && to > mid) queryOperation(query(from, math.min(to, mid), root * 2 + 1, left, mid), query(math.max(from, mid + 1), to, root * 2 + 2, mid + 1, right))
    else if (from <= mid) query(from, math.min(to, mid), root * 2 + 1, left, mid)
    else if (to > mid) query(math.max(from, mid + 1), to, root * 2 + 2, mid + 1, right)
    else throw new RuntimeException("Incorrect query from " + from + " to " + to)
  }

  def modify(from: Int, to: Int, delta: Int) {
    modify(from, to, delta, 0, 0, n - 1)
  }

  def modify(from: Int, to: Int, delta: Int, root: Int, left: Int, right: Int) {
    if (from == left && to == right) {
      this.delta(root) = joinDeltas(this.delta(root), delta)
      return
    }
    pushDelta(root, left, right)
    val mid: Int = (left + right) >> 1
    if (from <= mid) modify(from, math.min(to, mid), delta, 2 * root + 1, left, mid)
    if (to > mid) modify(math.max(from, mid + 1), to, delta, 2 * root + 2, mid + 1, right)
    value(root) = queryOperation(joinValueWithDelta(value(2 * root + 1), deltaEffectOnSegment(this.delta(2 * root + 1), mid - left + 1)), joinValueWithDelta(value(2 * root + 2), deltaEffectOnSegment(this.delta(2 * root + 2), right - mid)))
  }
}

object SegmentTree {
  // Usage example
  def main(args: Array[String]) {
    val rnd = new Random(1)
    for (step <- 0 until 1000) {
      val n = rnd.nextInt(50) + 1
      val t = new SegmentTree(n)
      val x = Array.fill(n)(t.getInitValue)
      for (i <- 0 until 1000) {
        val b = rnd.nextInt(n)
        val a = rnd.nextInt(b + 1)
        val cmd = rnd.nextInt(3)
        if (cmd == 0) {
          val delta = rnd.nextInt(100) - 50
          t.modify(a, b, delta)
          for (j <- a to b)
            x(j) = t.joinValueWithDelta(x(j), delta)
        } else if (cmd == 1) {
          val res1 = t.query(a, b)
          var res2 = x(a)
          for (j <- a to b)
            res2 = t.queryOperation(res2, x(j))
          if (res1 != res2) throw new RuntimeException
        } else {
          for (j <- 0 until n)
            if (t.query(j, j) != x(j))
              throw new RuntimeException
        }
      }
    }
    println("Test passed")
  }
}
