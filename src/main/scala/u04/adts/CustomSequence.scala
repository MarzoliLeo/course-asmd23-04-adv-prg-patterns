package scala.u04.adts


//This is for task 2.

object CustomSequence {
  // Private implementation hidden from outside
  private enum NewSequenceImpl[A] {
    case Cons(head: A, tail: NewSequenceImpl[A])
    case Nil()
  }

  // Opaque type alias
  opaque type NewSequenceADT[A] = NewSequenceImpl[A]

  // Constructors
  def cons[A](head: A, tail: NewSequenceADT[A]): NewSequenceADT[A] = NewSequenceImpl.Cons(head, tail)
  def nil[A]: NewSequenceADT[A] = NewSequenceImpl.Nil[A]()

  // Operations
  extension [A](seq: NewSequenceADT[A]) {
    def map[B](f: A => B): NewSequenceADT[B] = seq match {
      case NewSequenceImpl.Cons(head, tail) => NewSequenceImpl.Cons(f(head), tail.map(f))
      case NewSequenceImpl.Nil() => NewSequenceImpl.Nil()
    }

    def filter(p: A => Boolean): NewSequenceADT[A] = seq match {
      case NewSequenceImpl.Cons(head, tail) =>
        if (p(head)) NewSequenceImpl.Cons(head, tail.filter(p))
        else tail.filter(p)
      case NewSequenceImpl.Nil() => NewSequenceImpl.Nil()
    }

    def flatMap[B](f: A => NewSequenceADT[B]): NewSequenceADT[B] = seq match {
      case NewSequenceImpl.Cons(head, tail) => f(head).concat(tail.flatMap(f))
      case NewSequenceImpl.Nil() => NewSequenceImpl.Nil()
    }

    def foldLeft[B](acc: B)(op: (B, A) => B): B = seq match {
      case NewSequenceImpl.Cons(head, tail) => tail.foldLeft(op(acc, head))(op)
      case NewSequenceImpl.Nil() => acc
    }

    def foldRight[B](acc: B)(op: (A, B) => B): B = seq match {
      case NewSequenceImpl.Cons(head, tail) => op(head, tail.foldRight(acc)(op))
      case NewSequenceImpl.Nil() => acc
    }

    def reduce(op: (A, A) => A): A = seq match {
      case NewSequenceImpl.Cons(head, tail) => tail.foldLeft(head)(op)
      case NewSequenceImpl.Nil() => throw new UnsupportedOperationException("empty.reduce")
    }

    def concat(other: NewSequenceADT[A]): NewSequenceADT[A] = seq match {
      case NewSequenceImpl.Cons(head, tail) => NewSequenceImpl.Cons(head, tail.concat(other))
      case NewSequenceImpl.Nil() => other
    }
  }
}

