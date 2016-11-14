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