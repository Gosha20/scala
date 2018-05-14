package bot_handler


import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._
import user_handler._
import scala.io.Source

<<<<<<< HEAD:src/main/scala/EchoBot.scala
/**
  * Echo, ohcE
  */
object EchoBot extends TelegramBot with Polling {
  lazy val token = "579195535:AAELB3_J5izTN5sR5X9CixO0G6xzI0GVt8A"
//  val users = List[UserHandler]
=======
class EchoBot(val token:String) extends TelegramBot with Polling {
>>>>>>> e44a77a027f091325562106f4da51462d8b0865f:src/main/scala/bot_handler/EchoBot.scala
  override def receiveMessage(msg: Message): Unit = {
    print(msg)
    for (text <- msg.text) {
<<<<<<< HEAD:src/main/scala/EchoBot.scala
//      val user = User(msg.source.toString)
//      val userHand = UserHandler(user)
//
//      val cmd = userHand.performCommand(msg.text.get)
      request(SendMessage(msg.source, text))
=======
      println(text)
      val user = user_handler.User(msg.source.toString)
      val userHand = UserHandler(user)
      val cmd = userHand.performCommand(msg.text.get)
      request(SendMessage(msg.source, cmd))
>>>>>>> e44a77a027f091325562106f4da51462d8b0865f:src/main/scala/bot_handler/EchoBot.scala
    }
  }
}