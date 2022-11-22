case class Product(code: String, description: String)

trait Backend:
  def findByCode(code: String): Option[Product]
  def findByDescription(description: String): List[Product]

val aCode = "123-123"
val aDescription = "Cherry tomatoes"

val aBackend = new Backend:
  def findByCode(code: String): Option[Product] = None
  def findByDescription(description: String): List[Product] = Nil

// This is fine
aBackend.findByCode(aCode)

// This is a logical error, since we pass a description instead of a code
aBackend.findByCode(aDescription)

// Scala 2 solution

// Solution 1: use case classes
case class Code(code: String)
object Code:
  def apply(code: String) = Either.cond(
    code.matches("[0-9]{3}-[0-9]{3}"),
    new Code(code),
    "Code must be in the format 123-123"
  )

case class Description(description: String)

trait BackendV2:
  def findByCode(code: Code): Option[Product]
  def findByDescription(description: Description): List[Product]

val aBackendV2 = new BackendV2:
  def findByCode(code: Code): Option[Product] = None
  def findByDescription(description: Description): List[Product] = Nil

// Won't compile, since we require a Code, not a String
// aBackendV2.findByCode(aCode)

// In both cases we need to handle the error properly, otherwise the lines below won't compile
// aBackendV2.findByCode(Code(aCode)) //Works fine

// But this is still an issue, the previous one, but under a different layer of indirection
// aBackendV2.findByCode(Code(aDescription))

// Solution 2: use value classes

// case class CodeVC(code: String) extends AnyVal:
//   def countryCode: Char = code.charAt(0)

// Scala 3 solution

opaque type CodeOQ = String

object CodeOQ:
  def apply(code: String): Either[String, CodeOQ] = Either.cond(
    code.matches("[0-9]{3}-[0-9]{3}"),
    code,
    "Code must be in the format ddd-ddd"
  )

val bc = CodeOQ("123-123").foreach(println)
