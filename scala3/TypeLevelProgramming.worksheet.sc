trait Nat

// Peano numbers

class _0 extends Nat
class Succ[N <: Nat] extends Nat

type _1 = Succ[_0]
type _2 = Succ[_1]
type _3 = Succ[_2]
type _4 = Succ[_3]
type _5 = Succ[_4]

trait <[A <: Nat, B <: Nat]

object `<`:

  // Axioms
  given basic[A <: Nat]: <[_0, Succ[A]] with {}
  given induction[A <: Nat, B <: Nat](using <[A, B]): <[Succ[A], Succ[B]] with {}

  def apply[A <: Nat, B <: Nat](using lt: <[A, B]): <[A, B] = lt

val lt1 = <[_1, _2]
val lt2 = <[_2, _3]
val lt3 = <[_3, _4]

// Not allowed
// val lt3 = <[_3, _2]

/*
  the compiler will be able to compile <[_2, _4] if:

  - it can create a given instance of <[_2, _4], which it can if
  - it can create a given instance of <[_1, _3], which it can if
  - it can create a given instance of <[_0, _2],

  which it can because the (basic) axiom allows that /zero with any number/
 */
