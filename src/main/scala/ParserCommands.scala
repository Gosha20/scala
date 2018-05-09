import QuestionTypes.QuestionTypes
import atto.Atto._
import atto._
import cats.implicits._

object ParserCommands {

  Parser
  def getFixedIntAmount(n:Int): Parser[Int] =
    count(n, digit).map(_.mkString).flatMap { s =>
      try ok(s.toInt) catch { case e: NumberFormatException => err(e.toString) }
    }

  def booleanString(_true : String, _false : String, empty : Boolean = true):Parser[Boolean]=
    stringCI(_true).map(_ => true) | stringCI(_false).map(_ => false) | endOfInput.map(_=> empty)

  val whitespaces : Parser[Unit] =
    takeWhile(c => c == ' ').void

  val date: Parser[Date] =
    (getFixedIntAmount(4) <~ char('-'), getFixedIntAmount(2) <~ char('-'), getFixedIntAmount(2)).mapN(Date.apply)

  val time: Parser[Time] =
    (getFixedIntAmount(2) <~ char(':'), getFixedIntAmount(2) <~ char(':'), getFixedIntAmount(2)).mapN(Time.apply)

  val dateTime: Parser[DateTime] =
    (date <~ spaceChar, time).mapN(DateTime.apply) | endOfInput.map(_ => DateTime.emptyTime)

  val word: Parser[String] =
    takeWhile(c => c != ' ').map(s => s)
  // should be changed to normal code with digit check

  val wordForQuestion: Parser[String] = char('<') ~> takeWhile1(c => c != '>')

  val createPoll: Parser[Command] =
    (stringCI("/create_poll ") ~> word <~ whitespaces,
    booleanString("yes", "no") <~ whitespaces,
    booleanString("afterstop", "continuous", false ) <~ whitespaces,
    dateTime <~ whitespaces,
    dateTime).mapN(CommonCommands.CreatePoll.apply)

  val list: Parser[Command] =
    stringCI("/list").map(_=>CommonCommands.ToList.apply())

  val deletePoll: Parser[Command] =
    (stringCI("/delete_poll ") ~> int) map CommonCommands.DeletePoll.apply

  val startPoll:Parser[Command] =
    (stringCI("/start_poll ") ~> int) map CommonCommands.StartPoll.apply

  val stopPoll:Parser[Command] =
    (stringCI("/stop_poll ") ~> int) map CommonCommands.StopPoll.apply
  
  val result:Parser[Command] =
    (stringCI("/result ") ~> int) map CommonCommands.GetResults.apply

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
    (stringCI("/add_question ") ~> wordForQuestion, spaceChar ~>  questionType, many(anyChar)).mapN(
      (name, questionType, variantsChars) => {
        val variants = variantsChars.mkString.split("\n").drop(1).toList
        ContextCommands.AddQuestion(name, questionType, variants)})

  val command : Parser[Command] = choice(createPoll,
                                          list,
                                          deletePoll,
                                          startPoll,
                                          stopPoll,
                                          result,
                                          begin,
                                          end,
                                          view,
                                          addQuestion)

  def getCommand(strCmd:String) : Option[Command] = command.parseOnly(strCmd).option


}
