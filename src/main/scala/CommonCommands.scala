import PollsStore._
import Math._
object CommonCommands {
case class CreatePoll (pollTitle : String,
                       isAnon : Boolean,
                       resShown : Boolean,
                       startTime: DateTime,
                       endTime: DateTime) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val pollId = abs(pollTitle.hashCode)
      val poll = Poll(pollTitle, isAnon, resShown, startTime, endTime, pollId, userHandler.User)
      PollsStore.addPoll(poll, userHandler.User)
      pollId.toString
    }
  }


  case class ToList() extends Command {
    override def perform(userHandler: UserHandler): String=PollsStore.getPollsList
  }


  case class DeletePoll(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return "havent poll with this id")
      if (poll.creator != userHandler.User) {
        "You cant delete poll, you are not creator"
      }
      else{
        PollsStore.deletePoll(id, userHandler.User)
        s"delete id:$id"
      }
    }
  }


  case class StartPoll(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return "havent poll with this id")
      if (poll.creator != userHandler.User) {
        "You cant start poll, you are not creator"
      }
      else {
        PollsStore.update(poll.copy(active = true))
        "started"
      }
    }
  }


  case class StopPoll(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return "havent poll with this id")
      if (poll.creator != userHandler.User) {
        "You cant stop poll, you are not creator"
      }
      else {
        PollsStore.update(poll.copy(active = false))
        "stoped"
      }
    }
  }


  case class GetResults(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return "no id")
      poll.toString()
    }
  }
}