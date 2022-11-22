class Outer:
  class InnerClass
  object InnerObject
  type InnerType

  val innerClass = new InnerClass

  def process(inner: InnerClass): Unit = println(inner)
  def processGeneral(inner: Outer#InnerClass): Unit = println(inner)

val outer = new Outer
val inner = new outer.InnerClass

val outerA = new Outer
val outerB = new Outer

// The code below won't compile, since each instance of `Outer` has IT'S OWN `InnerClass` type
// val innerA: outerA.InnerClass = new outerB.InnerClass

// Inner objects are also different
outerA.InnerObject == outerB.InnerObject

// The types below are instance-dependent
val innerA = new outerA.InnerClass
val innerB = new outerB.InnerClass

outerA.process(innerA)

// The line below won't compile, since `innerB` is of a different type /taken from a different instance/
// outerA.process(innerB)

// The lines below compile, since the `processGeneral` method takes a path-dependent type
outerA.processGeneral(innerA)
outerA.processGeneral(innerB)

// dependent methods
trait Record:
  type Key

// Allows to return different types, based on the argument
// Another way of doing so are the generics
def getIdentifier(record: Record): record.Key = ???

// dependent function types
val getIdentifierFunction: Function1[Record, Record#Key] = getIdentifier

// The line above is a syntax sugar in Scala 3 for the line below
class DependentFunctionType extends Function1[Record, Record#Key]:
  def apply(record: Record): record.Key = getIdentifier(record)

// or this, if we go anonymous
val getIdentifierFunction2 = new Function1[Record, Record#Key]:
  def apply(record: Record): record.Key = getIdentifier(record)
