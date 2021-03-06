package my_parser

import atto.Atto._
import atto._
import cats.implicits._
import poll_store._
import commands._
import question.QuestionTypes.QuestionTypes
import question._
import java.time._

object ParserCommands {

  def getFixedIntAmount(n:Int): Parser[Int] =
    count(n, digit).map(_.mkString).flatMap { s =>
      try ok(s.toInt) catch { case e: NumberFormatException => err(e.toString) }
    }

  def booleanString(_false : String, _true : String, empty : Boolean = true):Parser[Boolean]=
    stringCI(_true).map(_ => true) | stringCI(_false).map(_ => false) | endOfInput.map(_=> empty)

  def resultShownWord(first : String, second: String): Parser[String] =
    string(first)| string(second)|endOfInput.map(_=> "continuous")

  val whitespaces : Parser[Unit] =
    takeWhile(c => c == ' ').void

  val date: Parser[LocalDate] =
    (getFixedIntAmount(4) <~ char('-'), getFixedIntAmount(2) <~ char('-'), getFixedIntAmount(2)).mapN(LocalDate.of)

  val time: Parser[LocalTime] =
    (getFixedIntAmount(2) <~ char(':'), getFixedIntAmount(2) <~ char(':'), getFixedIntAmount(2)).mapN(LocalTime.of)

  val dateTime: Parser[LocalDateTime] =
    (date <~ spaceChar, time).mapN((d, t) => LocalDateTime.of(d, t)) | endOfInput.map(_ => null)

  val word: Parser[String] =
    takeWhile(c => c != ' ').map(s => s)
  // should be changed to normal code with digit check

  val wordIntoBark: Parser[String] = char('<') ~> takeWhile1(c => c != '>')

  val createPoll: Parser[Command] =
    (stringCI("/create_poll ") ~> wordIntoBark <~ string(">"),
      whitespaces ~> booleanString("no", "yes") <~ whitespaces,
      resultShownWord("afterstop", "continuous") <~ whitespaces,
      dateTime <~ whitespaces,
      dateTime).mapN(SimpleCommand.CreatePoll.apply)

  val list: Parser[Command] =
    stringCI("/list").map(_=>SimpleCommand.ToList.apply())

  val deletePoll: Parser[Command] =
    (stringCI("/delete_poll ") ~> int) map SimpleCommand.DeletePoll.apply

  val startPoll:Parser[Command] =
    (stringCI("/start_poll ") ~> int) map SimpleCommand.StartPoll.apply

  val stopPoll:Parser[Command] =
    (stringCI("/stop_poll ") ~> int) map SimpleCommand.StopPoll.apply

  val result:Parser[Command] =
    (stringCI("/result ") ~> int) map SimpleCommand.GetResults.apply

  val begin:Parser[Command] =
    (stringCI("/begin ") ~> int) map ContextCommands.Begin.apply

  val end:Parser[Command] =
    (stringCI("/end") <~ endOfInput).map(_=> ContextCommands.End.apply())

  val view:Parser[Command] =
    (stringCI("/view") <~ endOfInput).map(_=> ContextCommands.View.apply())

  val questionType : Parser[QuestionTypes] =
    endOfInput.map(_ => QuestionTypes.Open) |
      (stringCI("open") ~> endOfInput).map(_ => QuestionTypes.Open) |
      stringCI("choice").map(_ => QuestionTypes.Choice) |
      stringCI("multi").map(_ => QuestionTypes.Multi)

  val addQuestion:Parser[Command] =
    (stringCI("/add_question ") ~> wordIntoBark <~ string(">"),
      whitespaces ~> questionType <~ whitespaces,
      many(anyChar)).mapN(
      (name, questionType, variantsChars) => {
        val variants = variantsChars.mkString.split("\n").drop(1).distinct
        ContextCommands.AddQuestion(name, questionType, variants)})

  val deleteQuestion:Parser[Command] =
    (stringCI("/delete_question ") ~> int) map ContextCommands.DeleteQuestion.apply

  val answerQuestion:Parser[Command] =
    (stringCI("/answer ") ~> int,
      whitespaces ~> wordIntoBark).mapN(ContextCommands.AnswerQuestion.apply)


  val command : Parser[Command] = choice(createPoll, list,
    deletePoll, startPoll,
    stopPoll, result,
    begin, end,
    view, addQuestion,
    deleteQuestion, answerQuestion)

  def getCommand(strCmd:String) : Option[Command] = command.parseOnly(strCmd).option

}