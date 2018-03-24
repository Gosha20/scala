import atto._
import Atto._
import cats.implicits._
import DateTime._
object ParseCommand {

  def fixed(n:Int): Parser[Int] =
    count(n, digit).map(_.mkString).flatMap { s =>
      try ok(s.toInt) catch { case e: NumberFormatException => err(e.toString) }
    }

  def booleanString(_true : String, _false : String, empty : Boolean = true):Parser[Boolean]=
    stringCI(_true).map(_ => true) | stringCI(_false).map(_ => false) | endOfInput.map(_=> empty)

  val whitespaces : Parser[Unit] =
    takeWhile(c => c == ' ').void

  val date: Parser[Date] =
    (fixed(4) <~ char('-'), fixed(2) <~ char('-'), fixed(2)).mapN(Date.apply)

  val time: Parser[Time] =
    (fixed(2) <~ char(':'), fixed(2) <~ char(':'), fixed(2)).mapN(Time.apply)

  val dateTime: Parser[DateTime] =
    (date <~ spaceChar, time).mapN(DateTime.apply) | endOfInput.map(_ => DateTime.emptyTime)

  val word: Parser[String] =
    takeWhile(c => c != ' ').map(s => s.toLowerCase)

  val createPoll: Parser[Command] =
    (stringCI("/create_poll ") ~> word <~ whitespaces,
    booleanString("yes", "no") <~ whitespaces,
    booleanString("afterstop", "continuous", false) <~ whitespaces,
    dateTime <~ whitespaces,
    dateTime).mapN(Commands.CreatePoll.apply)

  val list: Parser[Command] =
    stringCI("/list").map(_=>Commands.List.apply())

  val deletePoll: Parser[Command] =
    (stringCI("/delete_poll ") ~> int) map Commands.DeletePoll.apply

  val startPoll:Parser[Command] =
    (stringCI("/start_poll ") ~> int) map Commands.StartPoll.apply

  val stopPoll:Parser[Command] =
    (stringCI("/stop_poll ") ~> int) map Commands.StopPoll.apply


  val result:Parser[Command] =
    (stringCI("/result ") ~> int) map Commands.Result.apply

  val command : Parser[Command] = choice(createPoll, list, deletePoll, startPoll,stopPoll, result)

  def parseLine(string : String) : String = {
    command.parseOnly(string).option match {
      case Some(cmd) => cmd.perform()
      case None => "Wrong command"
    }
  }



}
