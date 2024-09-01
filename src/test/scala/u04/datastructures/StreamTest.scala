package scala.u04.datastructures

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.funsuite.AnyFunSuite
import u04.datastructures.Streams.*
import Stream.*

import scala.util.Random

class StreamTest extends AnyFunSuite:
  
  def randomStream(length: Int): Stream[Int] = {
    if (length <= 0) Empty()
    else cons(Random.nextInt(100), randomStream(length - 1))
  }

  def length[A](stream: Stream[A]): Int = stream match {
    case Empty() => 0
    case Cons(_, tail) => 1 + length(tail())
  }


  // Test parametrizzato per verificare la lunghezza di uno stream
  test("Stream length is non-negative") {
    for (_ <- 1 to 10) { // Esegui il test per 10 stream generati casualmente
      val size = Random.nextInt(100) // Lunghezza casuale fino a 100
      val stream = randomStream(size)
      assert(length(stream) >= 0)
    }
  }

  // Test parametrizzato per verificare che la somma rimanga invariata dopo il filtraggio
  test("Sum remains unchanged after filtering") {
    for (_ <- 1 to 10) { // Esegui il test per 10 stream generati casualmente
      val length = Random.nextInt(100) // Lunghezza casuale fino a 100
      val stream = randomStream(length)
      val originalSum = stream.toList.sum
      val filteredSum = stream.filter(_ => true).toList.sum
      assert(originalSum == filteredSum)
    }
  }


