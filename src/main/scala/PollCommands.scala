import DateTime._
import PollsMap._
object PollCommands {
  case class CreatePoll (pollTitle : String,
                         isAnon : Boolean,
                         resShown : Boolean,
                         startTime: DateTime,
                         endTime: DateTime) extends Command {
    override def execute(): String = {
      val pollId = PollsMap.polls.size
      polls +=  pollId -> new Poll(pollTitle,isAnon,resShown,startTime,endTime,pollId)
      pollId.toString
    }
  }


  case class ToList() extends Command {
    override def execute(): String = {
      polls.toString()
    }
  }


  case class DeletePoll(id : Int)extends Command{
    override def execute(): String = {
        PollsMap.polls.getOrElse(id, return "no id")
        PollsMap.polls -= id
        s"delete id:$id"
    }
  }


  case class StartPoll(id : Int)extends Command{
    override def execute(): String = {
      val poll = PollsMap.polls.getOrElse(id, return "no id")
      poll.start()
      "started"
    }
  }


  case class StopPoll(id : Int)extends Command{
    override def execute(): String = {
      PollsMap.polls(id).start()
      "stoped"
    }
  }


  case class GetResults(id : Int)extends Command{
    override def execute(): String = {
      val poll = PollsMap.polls.getOrElse(id, return "no id")
      poll.toString()
    }
  }
}
