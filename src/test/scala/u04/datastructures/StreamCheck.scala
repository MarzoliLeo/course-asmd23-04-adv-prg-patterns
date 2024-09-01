package scala.u04.datastructures

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.scalacheck.{Gen, Properties}
import u04.datastructures.Streams.*
import Stream.*

object StreamCheck extends Properties("Stream"):

  // Generatore per stream di lunghezza massima 100.
  def genStream[A](genElem: Gen[A]): Gen[Stream[A]] = Gen.sized { size =>
    val genEmpty = Gen.const(Empty(): Stream[A])
    val genCons = for {
      head <- genElem
      tail <- Gen.resize(size - 1, genStream(genElem))
    } yield cons(head, tail)
    Gen.frequency((1, genEmpty), (9, genCons))
  }

  def length[A](stream: Stream[A]): Int = stream match {
    case Empty() => 0
    case Cons(_, tail) => 1 + length(tail())
  }

  // Proprietà: la lunghezza di un stream è sempre non negativa
  property("length is non-negative") = forAll(genStream(arbitrary[Int])) { stream =>
    length(stream) >= 0
  }

  // Proprietà: la somma degli interi in uno stream rimane invariata quando filtriamo
  property("sum remains unchanged after filtering") = forAll(genStream(arbitrary[Int])) { stream =>
    val originalSum = stream.toList.sum
    val filteredSum = stream.filter(_ => true).toList.sum
    originalSum == filteredSum
  }

@main def runStreamCheck() =
  StreamCheck.check()
