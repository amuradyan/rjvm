class Outer:
  class Inner

val o1 = Outer()
val o2 = Outer()

val i1 = o1.Inner()
val i2 = o2.Inner()

trait ItemLike:
  type Key

class Item[K] extends ItemLike:
  type Key = K

val item1 = Item[Int]
val item2 = Item[String]

trait ItemAll extends ItemLike:
  override type Key >: Any

trait ItemNothing extends ItemLike:
  override type Key <: Nothing

// Scala 3 does not allow abstract type prjections.
// The line below will compile in Scala 2 leading to some
// absurd but compilable code that will crash at runtime.
//
// def process[T <: ItemLike](item: T#Key): T = ???
