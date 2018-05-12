import java.time.LocalDateTime

import bot_handler.EchoBot
import scala.io.Source

object Main {
  def main(args: Array[String]) : Unit = {
    val token = Source.fromFile("input.txt").getLines.mkString
    val bot = new EchoBot(token)
    bot.run()

//    val user = user_handler.User("gosha")
//    val user2 = user_handler.User("pepte")
//    val userHandler2 = user_handler.UserHandler(user2)
//    val userHandler = user_handler.UserHandler(user)
////
//    println(userHandler.performCommand("/create_poll test1 no"))
//    println(userHandler.performCommand("/create_poll test2"))
//    println(userHandler.performCommand("/list"))
//    println(userHandler.performCommand("/begin 0"))
//    println(userHandler.performCommand("/add_question <go?> choice \nda\nnet"))
//    println(userHandler.performCommand("/add_question <orNot?> open"))
//      println(userHandler.performCommand("/answer 0 <0>"))
//    println(userHandler.performCommand("/answer 0 <0>"))
//    println(userHandler2.performCommand("/begin 0"))
//    println(userHandler2.performCommand("/add_question <go?> choice \nda\nnet"))
//    println(userHandler2.performCommand("/answer 0 <0>"))
//    println(userHandler2.performCommand("/answer 0 <0>"))
//    println(userHandler2.performCommand("/answer 1 <go why not>"))
//    println(userHandler.performCommand("/answer 1 <go why not>"))
//    println(PollsStore.pollQuestion(PollsStore.polls(0)))

//    println(PollsStore.pollQuestion.toString())
//    println(userHandler.performCommand("/answer 5 <answer>"))
  }
}
