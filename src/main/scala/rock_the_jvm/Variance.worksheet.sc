// covariance
class Animal
class Dog(name: String) extends Animal
class Capybara(name: String) extends Animal

val rex: Dog = new Dog("Rex")
val olo: Dog = new Dog("Olo")

val animal: Animal = rex
val animals: List[Animal] = List[Dog](rex, olo)

class CovariantList[+T]
val covariantList: CovariantList[Animal] = new CovariantList[Dog]

// invariance

class InvariantList[T]

val myDogs: InvariantList[Capybara] = new InvariantList[Capybara]

// Won't compile
// val myDogs: InvariantList[Animal] = new InvariantList[Dog]

// contravariance
class ContravariantList[-T]

val myOtherDogs: ContravariantList[Dog] = new ContravariantList[Animal]

// Example

trait Vet[-T <: Animal]:
  def treat(animal: T): Unit

val myDog = new Dog("Buddy")
val myVet: Vet[Dog] = new Vet[Animal]:
  def treat(animal: Animal): Unit = println("Treated animal")

myVet.treat(myDog)

// Another example

class Stuff
case class Wood(name: String) extends Stuff
case class Metal(name: String) extends Stuff

// If A :> B, then F[A] :> F[B]
// Used in containers

class CovariantBag[+T]

val lw: CovariantBag[Stuff] = new CovariantBag[Metal]

// If A :> B, then F[B] =/= F[A]

class InvariantBag[T]

val li: InvariantBag[Wood] = new InvariantBag[Wood]

// If A :> B, then F[B] :> F[A]

class ContravariantBag[-T]

val lc: ContravariantBag[Wood] = new ContravariantBag[Stuff]

trait Press[-T <: Stuff]:
  def press(stuff: T): Unit

def pressMetal(metal: Metal, device: Press[Metal]): Unit = device.press(metal)

val iron = Metal("Iron")
val copper = Metal("Copper")

val metalPress = new Press[Metal]:
  def press(stuff: Metal): Unit = println(
    s"Pressing ${stuff.name} with 100 N power"
  )

val woodPress = new Press[Wood]:
  def press(stuff: Wood): Unit = println(
    s"Pressing ${stuff.name} with 10 N power"
  )

val universalPress = new Press[Stuff]:
  def press(stuff: Stuff): Unit = println(
    s"Pressing stuff with 1000000000000000000 N power"
  )

pressMetal(iron, metalPress)
// pressMetal(copper, woodPress)
pressMetal(iron, universalPress)
