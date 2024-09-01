package scala.u04.datastructures.Task2

import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.scalacheck.{Prop, Properties}

import scala.u04.adts.{CustomSequence, NewListSequence}
import org.scalacheck.{Arbitrary, Gen}

import scala.collection.immutable.List

implicit val arbFunction: Arbitrary[Int => CustomSequence.NewSequenceADT[Int]] = Arbitrary {
  for {
    n <- Arbitrary.arbitrary[Int]
  } yield (x: Int) => CustomSequence.cons(n + x, CustomSequence.nil)
}

object SequenceProperties extends Properties("CustomSequence") {

  // Importing implementations
  import scala.u04.adts.CustomSequence._
  import scala.u04.adts.NewListSequence._


  /* 1. Map Operation
  Axiom 1: map(nil, f) = nil
  Axiom 2: map(cons(h, t), f) = cons(f(h), map(t, f)) */

  property("map_nil") = forAll { (f: Int => Int) =>
    val customResult = CustomSequence.nil[Int].map(f)
    val listResult = NewListSequence.nil[Int].map(f)

    customResult == CustomSequence.nil[Int] &&
      listResult == NewListSequence.nil[Int]
  }

  property("map_cons") = forAll { (h: Int, t: List[Int], f: Int => Int) =>
    val customSeq = t.foldRight(CustomSequence.cons(h, CustomSequence.nil[Int]))((a, acc) => CustomSequence.cons(a, acc))
    val listSeq = h :: t

    val customResult = customSeq.map(f)
    val listResult = listSeq.map(f)

    customResult == t.foldRight(CustomSequence.cons(f(h), CustomSequence.nil[Int]))((a, acc) => CustomSequence.cons(f(a), acc)) &&
      listResult == (f(h) :: t.map(f))
  }

  /* 2. Filter Operation
  Axiom 1: filter(nil, p) = nil
  Axiom 2: filter(cons(h, t), p) = if (p(h)) cons(h, filter(t, p)) else filter(t, p)*/

  property("filter_nil") = forAll { (p: Int => Boolean) =>
    val customResult = CustomSequence.nil[Int].filter(p)
    val listResult = NewListSequence.nil[Int].filter(p)

    customResult == CustomSequence.nil[Int] &&
      listResult == NewListSequence.nil[Int]
  }

  property("filter_cons") = forAll { (h: Int, t: List[Int], p: Int => Boolean) =>
    val customSeq = t.foldRight(CustomSequence.cons(h, CustomSequence.nil[Int]))((a, acc) => CustomSequence.cons(a, acc))
    val listSeq = h :: t

    val customResult = customSeq.filter(p)
    val listResult = listSeq.filter(p)

    val expectedCustomResult = t.foldRight(if (p(h)) CustomSequence.cons(h, CustomSequence.nil[Int]) else CustomSequence.nil[Int])((a, acc) => if (p(a)) CustomSequence.cons(a, acc) else acc)

    val expectedListResult = listSeq.filter(p)

    customResult == expectedCustomResult && listResult == expectedListResult
  }

  /* 3. FlatMap Operation
  Axiom 1: flatMap(nil, f) = nil
  Axiom 2: flatMap(cons(h, t), f) = concat(f(h), flatMap(t, f)) */

  property("flatMap_nil") = forAll { (f: Int => CustomSequence.NewSequenceADT[Int]) =>
    val customResult = CustomSequence.nil[Int].flatMap(f)
    val expectedCustomResult = CustomSequence.nil[Int]

    val listResult = NewListSequence.nil[Int].flatMap(x => NewListSequence.nil)

    customResult == expectedCustomResult &&
      listResult == NewListSequence.nil[Int]
  }

  property("flatMap_cons") = forAll { (h: Int, t: List[Int], f: Int => CustomSequence.NewSequenceADT[Int]) =>
    val customSeq = t.foldRight(CustomSequence.cons(h, CustomSequence.nil[Int]))((a, acc) => CustomSequence.cons(a, acc))
    val customResult = customSeq.flatMap(f)

    val listSeq = t.foldRight(NewListSequence.cons(h, NewListSequence.nil[Int]))((a, acc) => NewListSequence.cons(a, acc))
    val listResult = listSeq.flatMap(f.andThen(seq => NewListSequence.cons(h, NewListSequence.nil)))

    // Confrontiamo i risultati
    customResult == customSeq.flatMap(f) &&
      listResult == listSeq.flatMap(f.andThen(seq => NewListSequence.cons(h, NewListSequence.nil)))
  }

  /* Fold Operation
  Axiom 1: fold(nil, z)(f) = z
  Axiom 2: fold(cons(h, t), z)(f) = fold(t, f(z, h)) */

  property("foldLeft_nil") = forAll { (z: Int, f: (Int, Int) => Int) =>
    val customResult = CustomSequence.nil[Int].foldLeft(z)(f)
    val listResult = NewListSequence.nil[Int].foldLeft(z)(f)
    customResult == listResult
  }

  property("foldLeft_cons") = forAll { (h: Int, t: List[Int], z: Int, f: (Int, Int) => Int) =>
    val customSeq = t.foldRight(CustomSequence.cons(h, CustomSequence.nil[Int]))((a, acc) => CustomSequence.cons(a, acc))
    val listSeq = t.foldRight(NewListSequence.cons(h, NewListSequence.nil[Int]))((a, acc) => NewListSequence.cons(a, acc))
    val customResult = customSeq.foldLeft(z)(f)
    val listResult = listSeq.foldLeft(z)(f)
    customResult == listResult
  }

  property("foldRight_nil") = forAll { (z: Int, f: (Int, Int) => Int) =>
    val customResult = CustomSequence.nil[Int].foldRight(z)(f)
    val listResult = NewListSequence.nil[Int].foldRight(z)(f)
    customResult == listResult
  }

  property("foldRight_cons") = forAll { (h: Int, t: List[Int], z: Int, f: (Int, Int) => Int) =>
    val customSeq = t.foldRight(CustomSequence.cons(h, CustomSequence.nil[Int]))((a, acc) => CustomSequence.cons(a, acc))
    val listSeq = t.foldRight(NewListSequence.cons(h, NewListSequence.nil[Int]))((a, acc) => NewListSequence.cons(a, acc))
    val customResult = customSeq.foldRight(z)(f)
    val listResult = listSeq.foldRight(z)(f)
    customResult == listResult
  }

  /* Concat Operation
  Axiom 1: concat(nil, l) = l
  Axiom 2: concat(cons(h, t), l) = cons(h, concat(t, l)) */

  property("concat_nil") = forAll { (seq: List[Int]) =>
    val customSeq = seq.foldRight(CustomSequence.nil[Int])((a, acc) => CustomSequence.cons(a, acc))
    val customResult = CustomSequence.nil[Int].concat(customSeq)
    val expectedCustomResult = customSeq

    val listSeq = seq.foldRight(NewListSequence.nil[Int])((a, acc) => NewListSequence.cons(a, acc))
    val listResult = NewListSequence.nil[Int].concat(listSeq)

    customResult == expectedCustomResult &&
      listResult == listSeq
  }

  property("concat_cons") = forAll { (h: Int, t: List[Int], seq2: List[Int]) =>
    val customSeq1 = t.foldRight(CustomSequence.cons(h, CustomSequence.nil[Int]))((a, acc) => CustomSequence.cons(a, acc))
    val customSeq2 = seq2.foldRight(CustomSequence.nil[Int])((a, acc) => CustomSequence.cons(a, acc))

    val customResult = customSeq1.concat(customSeq2)
    val listSeq1 = h :: t
    val listSeq2 = seq2
    val listResult = listSeq1 ++ listSeq2 // Uso `++` per concatenare le liste

    val expectedCustomResult = t.foldRight(CustomSequence.cons(h, customSeq2))((a, acc) => CustomSequence.cons(a, acc))

    customResult == expectedCustomResult && listResult == (h :: t ++ seq2)
  }

}

