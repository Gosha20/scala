import java.time.LocalDateTime

import bot_handler._
import poll_store.PollsStore
import user_handler.User

import scala.io.Source

object Main {
  def main(args: Array[String]) : Unit = {
//    val token = ""
//    val bot = new EchoBot(token)
//    bot.run()
    val userHandler1 = user_handler.UserHandler(User("aga"))
    println(userHandler1.performCommand("/create_poll <test 3> yes continuous 2018-05-19 14:17:00"))
    println(userHandler1.performCommand("/list"))
    println(userHandler1.performCommand("/list"))

    //    println(userHandler1.performCommand("/add_question <name poll> choice \nyes\nno"))
//    println(userHandler1.performCommand("/end"))
//    println(userHandler1.performCommand("/start_poll 0"))
//    println(userHandler1.performCommand("/begin 0"))
//    println(userHandler1.performCommand("/answer 0 <yes>"))

  }
}
