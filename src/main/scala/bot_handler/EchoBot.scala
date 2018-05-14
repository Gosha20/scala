package bot_handler


import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._
import user_handler._
import scala.io.Source

class EchoBot(val token:String) extends TelegramBot with Polling {

  override def receiveMessage(msg: Message): Unit = {
    print(msg)
    for (text <- msg.text) {
      val user = user_handler.User(msg.source.toString)
      val userHand = UserHandler(user)
      val cmd = userHand.performCommand(msg.text.get)
      request(SendMessage(msg.source, cmd))
    }
  }
}
}