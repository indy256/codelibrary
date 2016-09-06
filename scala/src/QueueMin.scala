import scala.collection.mutable.ArrayBuffer

class QueueMin[E](implicit val ord: Ordering[E]) {
  val s1 = new ArrayBuffer[(E, E)]
  val s2 = new ArrayBuffer[(E, E)]

  def min: E = {
    if (s1.isEmpty) return s2(s2.size - 1)._2
    if (s2.isEmpty) return s1(s1.size - 1)._2
    ord.min(s1(s1.size - 1)._2, s2(s2.size - 1)._2)
  }

  def addLast(x: E) {
    var minima = x
    if (s1.nonEmpty) minima = ord.min(minima, s1(s1.size - 1)._2)
    s1 += ((x, minima))
  }

  def removeFirst(): E = {
    if (s2.isEmpty) {
      var minima: Option[E] = None
      while (s1.nonEmpty) {
        val x = s1.remove(s1.size - 1)._1
        minima = Option(ord.min(minima.getOrElse(x), x))
        s2 += ((x, minima.get))
      }
    }
    s2.remove(s2.size - 1)._1
  }
}

object QueueMin {

  // Usage example
  def main(args: Array[String]) {
    val q = new QueueMin[Int]
    q.addLast(2)
    q.addLast(3)
    println(2 == q.min)
    q.removeFirst()
    println(3 == q.min)
    q.addLast(1)
    println(1 == q.min)
  }
}
