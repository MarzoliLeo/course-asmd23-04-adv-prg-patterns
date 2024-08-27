package scala.u04.adts

//This is for task 2.
trait NewSequenceADT[A] {
  // Constructors
  def cons(head: A, tail: NewSequenceADT[A]): NewSequenceADT[A]
  def nil: NewSequenceADT[A]

  // Operations
  def map[B](f: A => B): NewSequenceADT[B]
  def filter(p: A => Boolean): NewSequenceADT[A]
  def flatMap[B](f: A => NewSequenceADT[B]): NewSequenceADT[B]
  def foldLeft[B](acc: B)(op: (B, A) => B): B
  def foldRight[B](acc: B)(op: (A, B) => B): B
  def concat(other: NewSequenceADT[A]): NewSequenceADT[A]
  
}