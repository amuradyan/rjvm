def lastDigitOf(n: BigInt): Int = (n % 10).toInt

def lastCharOf(s: String): Char =
  if s.isEmpty then throw new NoSuchElementException("Empty string")
  else s.last

def lastElementOf[A](as: List[A]): A =
  if as.isEmpty then throw new NoSuchElementException("Empty list")
  else as.last

type ConstituentPartOf[T] = T match
  case BigInt  => Int
  case String  => Char
  case List[t] => t

val aDigit: ConstituentPartOf[BigInt] = 1
val aChar: ConstituentPartOf[String] = 'a'
val anInt: ConstituentPartOf[List[Int]] = 1

def lastComponentOf[T](t: T): ConstituentPartOf[T] = t match
  case n: BigInt => (n % 10).toInt
  case s: String =>
    if s.isEmpty then throw new NoSuchElementException("Empty string")
    else s.last
  case l: List[_] =>
    if l.isEmpty then throw new NoSuchElementException("Empty list")
    else l.last

val aDigit2: ConstituentPartOf[BigInt] = lastComponentOf(BigInt(123))
val aChar2: ConstituentPartOf[String] = lastComponentOf("Scala")
val anInt2: ConstituentPartOf[List[Int]] = lastComponentOf(List(1, 2, 3))

// Type-level recursion

type LowestLevelPartOf[T] = T match
  case List[t] => LowestLevelPartOf[t]
  case _       => T

val deepestElementInANestedList: LowestLevelPartOf[List[List[List[Int]]]] = 1

// Compiler can also take care of the recursion
// The code below won't compile
//
// type InfinitelyRecursiveType[T] = T match
//   case _ => InfinitelyRecursiveType[T]

// The compiler will also complain in the following case
// It will try to expand the type to the infinite depth
// and will fail with a stack overflow
//
// type AnotherIllegalRecursiveType[T] = T match
//   case Int => AnotherIllegalRecursiveType[T]

// def anIllegalMethod[T](t: T): AnotherIllegalRecursiveType[T] = ???

// val anIllegalCall = anIllegalMethod(1)
