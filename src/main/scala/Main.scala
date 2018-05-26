import java.time.LocalDateTime

import bot_handler._
import poll_store.PollsStore
import user_handler.User

import scala.io.Source

object Main {
  def main(args: Array[String]) : Unit = {
    val token = Source.fromFile("input.txt").getLines().mkString
    val bot = new EchoBot(token)
    bot.run()
//      val userh = user_handler.UserHandler(User("Georgy"))
//    userh.performCommand("/create_poll <test 1> yes continuous")
//    userh.performCommand("/begin 0")
//    userh.performCommand("/add_question <gogo gogo??> multi\nyes\nda\nmb")
//    userh.performCommand("/add_question <gogo gogo2??> choice\nyes\nda\nmb")
//    userh.performCommand("/add_question <gogo gogo3??> open")
//    userh.performCommand("/end")
//    println(userh.performCommand("/view"))
//    userh.performCommand("/start_poll 0")
//    userh.performCommand("/begin 0")
//    println(userh.performCommand("/view"))
//    println(userh.performCommand("/answer 0 <1 2>"))
//
//    println(userh.performCommand("/answer 1 <1 2>"))
//    println(userh.performCommand("/answer 1 <1>"))
//    println(userh.performCommand("/answer 2 <ya poidu>"))
//    println(userh.performCommand("/result 0"))
//    println(PollsStore.pollQuestion)
  }
}
