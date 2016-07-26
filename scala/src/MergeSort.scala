import scala.annotation.tailrec
import scala.util.Random

object MergeSort {

  def msort(xs: List[Int]): List[Int] = {

    @tailrec
    def merge(res: List[Int], xs: List[Int], ys: List[Int]): List[Int] = (xs, ys) match {
      case (_, Nil) => res.reverse ::: xs
      case (Nil, _) => res.reverse ::: ys
      case (x :: xs1, y :: ys1) =>
        if (x < y) merge(x :: res, xs1, ys)
        else merge(y :: res, xs, ys1)
    }

    val n = xs.length / 2
    if (n == 0) xs
    else {
      val (ys, zs) = xs splitAt n
      merge(Nil, msort(ys), msort(zs))
    }
  }

  def main(args: Array[String]) {
    val list = Seq.fill(10)(Random.nextInt(10)).toList
    println(msort(list))
  }
}
