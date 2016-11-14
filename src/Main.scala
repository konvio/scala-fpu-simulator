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
