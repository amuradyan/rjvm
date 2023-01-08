// Traits now can have parameters

trait Talker(val subject: String):
  def talkTo(another: Talker): String = ""

class Person(name: String) extends Talker("rock music"):
  override def talkTo(another: Talker): String =
    s"$name is talking about $subject"

val john = Person("John")
val jane = Person("Jane")

john.talkTo(jane)

class RockFan extends Talker("rock music")
class RockFanatic extends RockFan with Talker

trait BrokenRecord extends Talker

class Samuel extends BrokenRecord with Talker("politics")

trait Colour
case object Red extends Colour
case object Green extends Colour
case object Blue extends Colour

val colour = if (43 > 2) then Red else Green
