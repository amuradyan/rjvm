case class Person(name: String):
  def greet(): String = s"Hi, my name is $name"

// Adding extra functionality via implicits

// Implicit classes should accept a single argument
implicit class PersonLike(val s: String):
  def greet(): String = Person(s).greet()

// Adding extra functionality via extension methods

extension (s: String) def greet(): String = Person(s).greet()

"Bob".greet()

// Assume the `Tree` has been written in a library somewhere
// How do we add a `filter` functionality to it?

sealed abstract class Tree[+A]
case class Leaf[+A](value: A) extends Tree[A]
case class Branch[+A](left: Tree[A], right: Tree[A]) extends Tree[A]

// The `filter` method below is broken and is needed to show that the code compiles
extension [A](tree: Tree[A])
  def filter(predicate: A => Boolean): Tree[A] = tree match
    case Branch(left, right) =>
      Branch(left.filter(predicate), right.filter(predicate))
    case Leaf(value) if predicate(value) => Leaf(value)
    case _                               => tree

  def map[B](f: A => B): Tree[B] = tree match
    case Branch(left, right) => Branch(left.map(f), right.map(f))
    case Leaf(value)         => Leaf(f(value))

// Below the sum is applicable for trees with numeric values only
extension [A](tree: Tree[A])(using numeric: Numeric[A])
  def sum: A = tree match
    case Branch(l, r) => numeric.plus(l.sum, r.sum)
    case Leaf(v)      => v

val tree = Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4)))

val filteredTree = tree.filter(_ % 2 == 0)

val mappedTree = tree.map(_ * 2)

val sum = tree.sum

val treeOfStrings =
  Branch(Branch(Leaf("a"), Leaf("b")), Branch(Leaf("c"), Leaf("d")))

// The line below won't compile, since there is no implicit Numeric[String]
// treeOfStrings.sum

// We can also use the `using` in the extension method definition

extension [A](tree: Tree[A])
  def difference(using ordering: Ordering[A]): A = tree match
    case Branch(l, r) => ordering.max(l.difference, r.difference)
    case Leaf(v)      => v

treeOfStrings.difference
