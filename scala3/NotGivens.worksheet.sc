def processList[A, B](la: List[A], lb: List[B]): List[(A, B)] = for
  a <- la
  b <- lb
yield (a, b)

object EnforcingTheSameType:
  def processListV1[A](la: List[A], lb: List[A]): List[(A, A)] =
    processList(la, lb)

  def processListV2[A](la: List[A], lb: List[A]): List[(A, A)] = for
    a <- la
    b <- lb
  yield (a, b)

  def processListV3[A, B](la: List[A], lb: List[B])(using
      A =:= B
  ): List[(A, B)] =
    for
      a <- la
      b <- lb
    yield (a, b)

EnforcingTheSameType.processListV3(List(1, 2, 3), List(4, 5, 6))

// Not allowed
// EnforcingTheSameType.processListV3(List(1, 2, 3), List("4", "5", "6"))

object EnforcingDifferentTypes:
  // The compiler generates an instance of NotGiven[A =:= B]
  // if it cannnot find a given instance of A =:= B
  import scala.util.NotGiven

  def processListDifferent[A, B](la: List[A], lb: List[B])(using
      NotGiven[A =:= B]
  ): List[(A, B)] =
    for
      a <- la
      b <- lb
    yield (a, b)

EnforcingDifferentTypes.processListDifferent(List(1, 2, 3), List("4", "5", "6"))

// Not allowed
// EnforcingDifferentTypes.processListDifferent(List(1, 2, 3), List(4, 5, 6))
