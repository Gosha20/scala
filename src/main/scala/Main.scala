import scala.io.Source
object Main {
  def main(args: Array[String]) : Unit = {
    for (line <- Source.fromFile("input.txt").getLines)
      println(ParseCommands.parseLine(line))
  }
}
