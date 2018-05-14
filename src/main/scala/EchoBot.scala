
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._

/**
  * Echo, ohcE
  */
object EchoBot extends TelegramBot with Polling {
  lazy val token = "579195535:AAELB3_J5izTN5sR5X9CixO0G6xzI0GVt8A"
//  val users = List[UserHandler]
  override def receiveMessage(msg: Message): Unit = {
    print(msg)
    for (text <- msg.text) {
//      val user = User(msg.source.toString)
//      val userHand = UserHandler(user)
//
//      val cmd = userHand.performCommand(msg.text.get)
      request(SendMessage(msg.source, text))
    }
  }
//
//  def parse(cmdStr:String, id: Int) : Command = {
//    val user =
//  }

}