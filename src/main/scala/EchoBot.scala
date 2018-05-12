
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._

/**
  * Echo, ohcE
  */
object EchoBot extends TelegramBot with Polling {
  lazy val token = "579195535:AAF_xTvql4h3XRCAvB9Obhs016fb17wY7yo"
  val users = List[UserHandler]
  override def receiveMessage(msg: Message): Unit = {
    for (text <- msg.text) {
      val user = User(msg.source.toString)
      val userHand = UserHandler(user)

      val cmd = userHand.performCommand(msg.text.get)
      request(SendMessage(msg.source, cmd))
    }
  }
//
//  def parse(cmdStr:String, id: Int) : Command = {
//    val user =
//  }

}