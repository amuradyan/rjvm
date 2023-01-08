// Given\Using

case class Citizen(surname: String, name: String, age: Int)

val citizenOrdering: Ordering[Citizen] = new Ordering[Citizen]:
  override def compare(x: Citizen, y: Citizen): Int =
    x.surname.compareTo(y.surname)

def listCitizens(citizens: List[Citizen])(
    ordering: Ordering[Citizen]
): Seq[Citizen] = ???
def anotherListOfCitizens(alice: Citizen, bob: Citizen)(
    ordering: Ordering[Citizen]
): Seq[Citizen] = ???

def aMethodRequiringStandardOrdering(citizens: List[Citizen])(using
    ordering: Ordering[Citizen]
): Seq[Citizen] = citizens.sortBy(identity)
def aMethodRequiringStandardOrderingOverOptions(
    citizens: List[Option[Citizen]]
)(using ordering: Ordering[Option[Citizen]]): Seq[Option[Citizen]] =
  citizens.sortBy(identity)

object StandardValues:
  given standardCitizenOrdering: Ordering[Citizen] with
    override def compare(x: Citizen, y: Citizen): Int = x.name.compareTo(y.name)

// All the three imports below will work
//
// import StandardValues.standardCitizenOrdering
// import StandardValues.{given Ordering[Citizen]} - has to be the only given for that TYPE
import StandardValues.{given}

aMethodRequiringStandardOrdering(
  List(Citizen("Smith", "John", 30), Citizen("Amith", "Jane", 25))
)

given optionOrdering[T](using normalOrdering: Ordering[T]): Ordering[Option[T]]
  with
  override def compare(oA: Option[T], oB: Option[T]): Int = (oA, oB) match
    case (None, None)       => 0
    case (None, _)          => -1
    case (_, None)          => 1
    case (Some(a), Some(b)) => normalOrdering.compare(a, b)

val maybeCitizens = List(
  Some(Citizen("Smith", "John", 30)),
  Some(Citizen("Amith", "Jane", 25)),
  None
)

aMethodRequiringStandardOrderingOverOptions(maybeCitizens)

case class Persson(name: String):
  def greet: String = s"Hello, my name is $name"

// implicit def stringToPersson(string: String): Persson = Persson(string)

given Conversion[String, Persson] with
  def apply(string: String): Persson = Persson(string)

import scala.language.implicitConversions

"Alice".greet

// The compiler looks for the `given`-s and implicits in the following /exact same/ order:
// 1. The current scope
// 2. The scope of all the explicitly imported classes, objects and packages
// 3. The scope of the companion object of the class, whose method is being called
// 4. The scope of the companion objects of all the types, involved in the method call, if the method is generic

def aMethodWithAGivenArg[T](using instance: T): T = instance

// If the two lines below are uncommented, the following line will not compile
//
// given intInstance: Int = 42
implicit val intInstance: Int = 43

aMethodWithAGivenArg[Int]
