trait Nat

// Peano numbers

class _0 extends Nat
class Succ[N <: Nat] extends Nat

type _1 = Succ[_0]
type _2 = Succ[_1]
type _3 = Succ[_2]
type _4 = Succ[_3]
type _5 = Succ[_4]
type _6 = Succ[_5]

trait <[A <: Nat, B <: Nat]

object `<`:

  // Axioms
  given basic[A <: Nat]: <[_0, Succ[A]] with {}
  given induction[A <: Nat, B <: Nat](using <[A, B]): <[Succ[A], Succ[B]] with {}

  def apply[A <: Nat, B <: Nat](using lt: <[A, B]): <[A, B] = lt

val lt1 = <[_1, _2]
val lt2 = <[_2, _3]
val lt3 = <[_3, _4]

// Won't compile, no appropriate given value
// val lt3 = <[_3, _2]

trait <=[A <: Nat, B <: Nat]

object `<=`:

  // Axioms
  given basic[A <: Nat]: <=[_0, Succ[A]] with {}
  given induction[A <: Nat, B <: Nat](using <=[A, B]): <=[Succ[A], Succ[B]] with {}

  def apply[A <: Nat, B <: Nat](using lt: <=[A, B]): <=[A, B] = lt

val lte1 = <=[_1, _2]
val lte2 = <=[_2, _3]
val lte3 = <=[_3, _4]

// Won't compile, no appropriate given value
// val lte3 = <=[_3, _2]

/*
  the compiler will be able to compile <[_2, _4] if:

  - it can create a given instance of <[_2, _4], which it can if
  - it can create a given instance of <[_1, _3], which it can if
  - it can create a given instance of <[_0, _2],

  which it can because the (basic) axiom allows that /zero with any number/
 */

// Sorting
//
// We need to be able to
// - partition
// - concat
// - sort

trait HList

class HNil extends HList
class ::[H <: Nat, T <: HList] extends HList

// Concatentaion
//
// axioms
//
// - HNil ++ HL = HL
// - HA ++ HB = O => (N :: HA) ++ HB = N :: O

trait Concat[HA <: HList, HB <: HList, O <: HList]

object Concat:

  given basic[HL <: HList]: Concat[HNil, HL, HL] with {}
  given induction[N <: Nat, HA <: HList, HB <: HList, O <: HList](using
      Concat[HA, HB, O]
  ): Concat[N :: HA, HB, N :: O] with {}

  def apply[HA <: HList, HB <: HList, O <: HList](using
      concat: Concat[HA, HB, O]
  ): Concat[HA, HB, O] = concat

val c1 = Concat[_1 :: HNil, _5 :: HNil, _1 :: _5 :: HNil]

// Won't compile, no appropriate given value
// val c2 = Concat[_1 :: HNil, _5 :: HNil, _5 :: _1 :: HNil]

// Partition
//
// axioms
//
// - [] -> ([], [])
// - [n] -> ([n], [])
// - p :: T -> (p :: L, R), THEN for every n: Nat in T
//   - if n <= p | p :: n :: T -> (p :: n :: L, R)
//   - if n  > p | p :: n :: T -> (p :: L, n :: R)

trait Partition[HL <: HList, L <: HList, R <: HList]

object Partition:

  given empty: Partition[HNil, HNil, HNil] with {}
  given singleton[N <: Nat]: Partition[N :: HNil, N :: HNil, HNil] with {}

  given lte[P <: Nat, N <: Nat, T <: HList, L <: HList, R <: HList](using
      Partition[P :: T, P :: L, R],
      <=[N, P]
  ): Partition[P :: N :: T, P :: N :: L, R] with {}

  given gt[P <: Nat, N <: Nat, T <: HList, L <: HList, R <: HList](using
      Partition[P :: T, P :: L, R],
      <[P, N]
  ): Partition[P :: N :: T, P :: L, N :: R] with {}

  def apply[HL <: HList, L <: HList, R <: HList](using
      partition: Partition[HL, L, R]
  ): Partition[HL, L, R] = partition

val emptyTest = Partition[HNil, HNil, HNil]
val singletonTest = Partition[_1 :: HNil, _1 :: HNil, HNil]

val p1 = Partition[_4 :: _2 :: _5 :: HNil, _4 :: _2 :: HNil, _5 :: HNil]

// Won't compile, no appropriate given value
// val p2 = Partition[_4 :: _2 :: _5 :: HNil, _2 :: _4 :: HNil, _5 :: HNil]

// Sorting

// axioms
//
// - [] -> []
// - n :: T
//  if exists Partition[n :: T, n :: L, R]
//  if exists Sort[L, SL]
//  if exists Sort[R, SR]
//  if exists Concat[SL, n :: SR, O]
//  => Sort[n :: T, O]

trait QSortProof[HL <: HList, O <: HList]

object QSortProof:

  given empty: QSortProof[HNil, HNil] with {}

  given induction[N <: Nat, T <: HList, L <: HList, R <: HList, SL <: HList, SR <: HList, O <: HList](using
      Partition[N :: T, N :: L, R],
      QSortProof[L, SL],
      QSortProof[R, SR],
      Concat[SL, N :: SR, O]
  ): QSortProof[N :: T, O] with {}

  def apply[HL <: HList, O <: HList](using
      qsort: QSortProof[HL, O]
  ): QSortProof[HL, O] = qsort

val q1 = QSortProof[_4 :: _2 :: _5 :: HNil, _2 :: _4 :: _5 :: HNil]
val q2 = QSortProof[_6 :: _3 :: _1 :: HNil, _1 :: _3 :: _6 :: HNil]

// Won't compile, no appropriate given value
// val q2 = QSortProof[_4 :: _2 :: _5 :: HNil, _4 :: _2 :: _5 :: HNil]

trait Sort[L <: HList] {
  type Result <: HList
}

object Sort:
  type QSort[HL <: HList, O <: HList] = Sort[HL] { type Result = O }

  given empty: QSort[HNil, HNil] = new Sort[HNil] { type Result = HNil }

  given induction[N <: Nat, T <: HList, L <: HList, R <: HList, SL <: HList, SR <: HList, O <: HList](using
      Partition[N :: T, N :: L, R],
      QSort[L, SL],
      QSort[R, SR],
      Concat[SL, N :: SR, O]
  ): QSort[N :: T, O] = new Sort[N :: T] { type Result = O }

  def apply[L <: HList](using sort: Sort[L]): QSort[L, sort.Result] =
    new Sort[L] { type Result = sort.Result }

val s1 = Sort[_4 :: _2 :: _5 :: _1 :: HNil]

import org.tpolecat.typename.TypeName

def printType[A](v: A)(using t: TypeName[A]): Unit =
  val typeStart = t.value.indexOf("type Result >:")
  val typeEnd = t.value.lastIndexOf("<:")

  // looks /is/ terrible, helps a lot
  val typeString =
    t.value
      .substring(typeStart + 14, typeEnd)
      .replaceAll("MdocApp.this", "")
      .replaceAll(".::\\[.", " :: ")
      .replaceAll(".HNil\\]\\]\\]\\]", "")
      .replaceAll(",", "")
      .replaceAll("._", "")
      .drop(4)

  println(typeString)

printType(s1)
