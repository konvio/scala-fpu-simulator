######Кононенко Віталій, К-24
#Лабораторна робота №3
##Стековий сопроцесор
 
###Огляд
Розроблена програмна модель cтекового сопроцесора та реалізована його імітаційна модель.
![demo](http://i.imgur.com/vVHPt5C.png)

###Варіант
* Характеристика: 7 
* Мантиса: 48

###Дані з плаваючою крапкою
Реалізований повний набір команд для чисел з плаваючою крапкою для заданого
формату. Підтримуються числа у денормалізованій формі.

###Команди
* **load** - завантажити число в ST0
* **add** - додати ST0 до ST1
* **sub** - відняти від ST1 ST0
* **mul** - помножити ST1 на ST0
* **div** - поділити ST1 на ST0
* **exch** - поміняти місцями ST0 та ST1
* **dubl** - дублювати ST0
* **pop** - викинути ST0 зі стеку
* **exit** - завершити програму

###Scala
Для реалізації програмної моделі використана мова програмування [Scala](http://www.scala-lang.org/).
Scala компілюціються в байткод JVM та повністю сумісна з Java (класи обох можна спільно використовувати).

###Код
```scala
object Main extends App {

  val fpu = new FPU

  while (true) {
    fpu.printStack()
    val command = io.StdIn.readLine.trim.toLowerCase split "[\\s]+"
    command match {
      case Array("load", _) => fpu.load(command(1).toDouble)
      case Array("add") => fpu.add()
      case Array("sub") => fpu.sub()
      case Array("mul") => fpu.mul()
      case Array("div") => fpu.div()
      case Array("exch") => fpu.exch()
      case Array("dubl") => fpu.dub()
      case Array("pop") => fpu.pop()
      case Array("exit") => System.exit(0)
      case _ => println("Invalid operation")
    }
  }
}
```
```scala
class FPU {
  private val stack = new Array[Double](8)
  private var size = 0

  def load(value: Double) = {
    if (size == 8) {
      for (i <- 0 to 7) stack(i) = stack(i + 1)
      stack(7) = value
    } else {
      stack(size) = value
      size += 1
    }
  }

  def add() = reduce((x, y) => x + y)

  def sub() = reduce((x, y) => x - y)

  def mul() = reduce((x, y) => x * y)

  def div() = reduce((x, y) => x / y)

  def reduce(f: (Double, Double) => Double) = {
    if (size > 1) {
      val st0 = stack(size - 1)
      val st1 = stack(size - 2)
      stack(size - 2) = f(st1, st0)
      size -= 1
    } else {
      println("Invalid operation")
    }
  }

  def exch() = {
    if (size > 1) {
      val t = stack(size - 1)
      stack(size - 1) = stack(size - 2)
      stack(size - 2) = t
    }
  }

  def dub() = {
    if (size > 0) load(stack(size - 1))
  }

  def pop() = {
    if (size > 0) size -= 1
  }

  def printStack() = {
    if (size == 0) println("empty stack")
    for (i <- 0 until size) {
      val sti = Float56(stack(size - 1 - i))
      println(s"ST$i: $sti")
    }
  }
}
```
```scala
class Float56(val float64: Double) {

  val long64 = java.lang.Double.doubleToRawLongBits(float64).toBinaryString
  val raw64 = "0" * (64 - long64.length) + long64
  val sign = raw64(0)
  val rawExponent64 = raw64.substring(1, 12)
  val rawMantissa64 = raw64.substring(12)
  val rawExponent56 = Float56.rawExponent(rawExponent64)
  val rawMantissa56 = Float56.rawMantissa(rawMantissa64)

  override def toString = s"$sign $rawExponent56 $rawMantissa56 [$float64]"
}

object Float56 {
  def apply(float64: Double) = new Float56(float64)

  def rawExponent(rawExponent64: String): String = {
    val exponent64 = Integer.parseInt(rawExponent64, 2)
    if (exponent64 == 0) return "0" * 7
    val bias64 = 1023
    val exponent = exponent64 - bias64
    val bias56 = (1 << 6) - 1
    var exponent56 = exponent + bias56
    if (exponent == 1024) exponent56 = (1 << 7) - 1
    val rawExponent56 = Integer.toBinaryString(exponent56)
    "0" * (7 - rawExponent56.length) + rawExponent56
  }

  def rawMantissa(rawMantissa64: String) = rawMantissa64.substring(0, 48)
}
```
######© 2016