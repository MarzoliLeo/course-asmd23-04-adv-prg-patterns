package scala.u04.datastructures.Task3

import org.scalacheck.{Arbitrary, Gen, Prop, Properties}
import org.scalacheck.Prop.forAll
import u04.monads.Optionals.{*, given}
import Optional.*

object MonadProperties extends Properties("Monad") {

  // Define Arbitrary instances for your Monad and functions
  implicit val arbMonad: Arbitrary[Option[Int]] = Arbitrary(Gen.option(Gen.posNum[Int]))
  implicit val arbFunction: Arbitrary[Int => Option[Int]] = Arbitrary(Gen.oneOf(
    (x: Int) => Some(x + 1),
    (x: Int) => Some(x * 2),
    (x: Int) => Some(x / 2)
  ))


  // Define Arbitrary instances for your Monad and functions
  implicit val arbOptional: Arbitrary[Optional[Int]] = Arbitrary(Gen.option(Gen.posNum[Int]).map {
    case Some(v) => Just(v)
    case None => Empty()
  })


  implicit val arbFunctionOptional: Arbitrary[Int => Optional[Int]] = Arbitrary(Gen.oneOf(
    (x: Int) => Just(x + 1),
    (x: Int) => Just(x * 2),
    (x: Int) => Just(x / 2)
  ))


  // Left identity law: flatMap(unit(x))(f) == f(x)
  property("left_identity") = forAll { (x: Int, f: Int => Option[Int]) =>
    Option(x).flatMap(f) == f(x)
  }

  // Right identity law: flatMap(m)(unit) == m
  property("right_identity") = forAll { (m: Option[Int]) =>
    m.flatMap(Option(_)) == m
  }

  // Associativity law: flatMap(flatMap(m)(f))(g) == flatMap(m)(x => flatMap(f(x))(g))
  property("associativity") = forAll { (m: Option[Int], f: Int => Option[Int], g: Int => Option[Int]) =>
    m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
  }
  

  // Left identity law: flatMap(unit(x))(f) == f(x)
  property("left_identity_optional") = forAll { (x: Int, f: Int => Optional[Int]) =>
    (for {
      a <- Just(x)
      b <- f(a)
    } yield b) == f(x)
  }

  // Right identity law: flatMap(m)(unit) == m
  property("right_identity_optional") = forAll { (m: Optional[Int]) =>
    (for {
      a <- m
      b <- Just(a)
    } yield b) == m
  }

  // Associativity law: flatMap(flatMap(m)(f))(g) == flatMap(m)(x => flatMap(f(x))(g))
  property("associativity_optional") = forAll { (m: Optional[Int], f: Int => Optional[Int], g: Int => Optional[Int]) =>
    (for {
      a <- m
      b <- f(a)
      c <- g(b)
    } yield c) == (for {
      x <- m
      y <- f(x).flatMap(g)
    } yield y)
  }


}
