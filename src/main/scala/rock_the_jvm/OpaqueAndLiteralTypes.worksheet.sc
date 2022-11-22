// Literal types
val number = 3
val three: 3 = 3
val two: 2 = 2

def passNumber(n: Int): Unit = println(n)

passNumber(number)
passNumber(three)

def passThree(n: 3): Unit = println(n)

passThree(three)
// passThree(number) - Won't compile

val truth: true = true

val myFavoriteLanguage: "Scala" = "Scala"
