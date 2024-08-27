package scala.u04.adts

//This is for task 2.
object NewListSequence {
  // Opaque type alias to Scala's List
  opaque type SequenceADT[A] = List[A]

  // Constructors
  def cons[A](head: A, tail: SequenceADT[A]): SequenceADT[A] = head :: tail
  def nil[A]: SequenceADT[A] = List.empty[A]

  // Operations
  extension [A](seq: SequenceADT[A]) {
    def map[B](f: A => B): SequenceADT[B] = seq.map(f)
    def filter(p: A => Boolean): SequenceADT[A] = seq.filter(p)
    def flatMap[B](f: A => SequenceADT[B]): SequenceADT[B] = seq.flatMap(f)
    def foldLeft[B](acc: B)(op: (B, A) => B): B = seq.foldLeft(acc)(op)
    def foldRight[B](acc: B)(op: (A, B) => B): B = seq.foldRight(acc)(op)
    def concat(other: SequenceADT[A]): SequenceADT[A] = seq ++ other
  }
}
