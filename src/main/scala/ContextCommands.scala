import QuestionTypes.QuestionTypes

object ContextCommands {
  case class Begin(id: Int) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return "havent poll with this id")
      if (poll.creator != userHandler.User){
        "You cant work with poll, you are not creator"
      }
      else{
        PollsStore.setBeginWork(userHandler.User, poll)
        "You worked with poll id"
      }
    }
  }
  case class End() extends Command {
    override def perform(userHandler: UserHandler): String =
      if (PollsStore.userWorkWithPoll.contains(userHandler.User)){
        PollsStore.setEndWork(userHandler.User)
        "You stoped work with poll"
      }
    else{
        "you i ne nachinal boy"
      }
  }
  case class View() extends Command {
    override def perform(userHandler: UserHandler): String = userHandler.User.getPollToView

    }
  case class AddQuestion(name:String, questionType: QuestionTypes, answers:List[String]) extends Command {
    override def perform(userHandler: UserHandler): String = {
      print(name)
      print(questionType)
      name
    }
  }
}


