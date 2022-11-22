// Enumerations

enum Color:
  case Red, Green, Blue

val red: Color = Color.Red

enum PermissionsWithBits(val bit: Int):
  case Read extends PermissionsWithBits(11)
  case Write extends PermissionsWithBits(12)
  case Execute extends PermissionsWithBits(14)
  case None extends PermissionsWithBits(0)

  def toHex: String = Integer.toHexString(bit)

object PermissionsWithBits:
  def fromHex(hex: String): PermissionsWithBits =
    Integer.parseInt(hex, 16) match
      case 11 => Read
      case 12 => Write
      case 14 => Execute
      case _  => None

val read: PermissionsWithBits = PermissionsWithBits.Read
val readBits = read.bit

val write: PermissionsWithBits = PermissionsWithBits.Write
val writeBits = write.toHex

val execute: PermissionsWithBits = PermissionsWithBits.fromHex("14")

val first = PermissionsWithBits.Read.ordinal
val all = PermissionsWithBits.values.foreach(println)
val readPermission = PermissionsWithBits.valueOf("Read")
