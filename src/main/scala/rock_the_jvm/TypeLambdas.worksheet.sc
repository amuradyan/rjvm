// Type lambdas

/*
  Scala types belong to kinds. Kind is a type of a type.

    - Plain types like Int, String, non-generic case classes are of a value-level kind /can be attached to a value/
    - Generic types like List, Option, Map are of a generic kind /cannot be attached to a value w\o a generic type/
    - Functor, Monad, Applicative are of a higher kind /generics of generics/
 */

class Functor[F[_]]

val functor = Functor[Option]

type MyList = [T] =>> List[T]

val myList: MyList[Int] = List(1, 2, 3)

// The two types below are equivalent
type MyMap = [T] =>> Map[String, T]
type MyMap2[T] = Map[String, T]

val addressBook: MyMap[String] = Map()
val addressBook2: MyMap2[String] = Map()

type SpecialEither = [T, E] =>> Either[E, Option[T]]

val specialEitherWithValue: SpecialEither[Int, String] = Right(Some(3))
val specialEitherWithError: SpecialEither[Int, String] = Left("oops")

trait Mnd[M[_]]:
  def pure[A](v: A): M[A]
  def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]

class EitherMnd[E] extends Mnd[[T] =>> Either[E, T]]:
  override def pure[A](v: A) = Right(v)
  override def flatMap[A, B](ema: Either[E, A])(f: A => Either[E, B]) =
    ema.flatMap(f)

val eitherMnd = new EitherMnd[String]

val either = eitherMnd.pure(15)

val either2 = eitherMnd.flatMap(either)(_ => Left("error"))
// val either3 = eitherMnd.flatMap(either)(_ => Left(15)) - won't compile. Left has to contain a string
