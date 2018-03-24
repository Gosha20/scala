import DateTime._
import PollsMap._
object PollCommands {
  case class CreatePoll (pollTitle : String,
                         isAnon : Boolean,
                         resShown : Boolean,
                         startTime: DateTime,
                         endTime: DateTime) extends Command {
    override def perform(): String = {
      val pollId = pollTitle.hashCode
      polls +=  pollId -> new Poll(pollTitle,isAnon,resShown,startTime,endTime,pollId)
      pollId.toString
    }
  }


  case class ToList() extends Command {
    override def perform(): String = {
      polls.toString()
    }
  }


  case class DeletePoll(id : Int)extends Command{
    override def perform(): String = {
      PollsMap.polls.getOrElse(id, return "no id")
      PollsMap.polls -= id
      s"delete id:$id"
    }
  }


  case class StartPoll(id : Int)extends Command{
    override def perform(): String = {
      val poll = PollsMap.polls.getOrElse(id, return "no id")
      poll.start()
      "started"
    }
  }


  case class StopPoll(id : Int)extends Command{
    override def perform(): String = {
      PollsMap.polls(id).start()
      "stoped"
    }
  }


  case class GetResults(id : Int)extends Command{
    override def perform(): String = {
      val poll = PollsMap.polls.getOrElse(id, return "no id")
      poll.toString()
    }
  }
}