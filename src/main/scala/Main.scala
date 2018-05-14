import java.time.LocalDateTime

import bot_handler._

import scala.io.Source

object Main {
  def main(args: Array[String]) : Unit = {
    val token = Source.fromFile("input.txt").getLines.mkString
    val bot = new EchoBot(token)
    bot.run()

  }
}
