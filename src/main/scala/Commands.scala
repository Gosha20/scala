import DateTime._
import PollsMap._
object Commands {
  case class CreatePoll (name : String,
                         anon : Boolean,
                         resShow : Boolean,
                         startTime: DateTime,
                         endTime: DateTime) extends Command {
    override def perform(): String = {
      val id = PollsMap.polls.size
      polls +=  id -> new Poll(name,anon,resShow,startTime,endTime,id)
      id.toString
    }
  }


  case class List() extends Command {
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

  case class Result(id : Int)extends Command{
    override def perform(): String = {
      val poll = PollsMap.polls.getOrElse(id, return "no id")
      poll.toString()
    }
  }

}
