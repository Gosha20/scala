import DateTime._
import PollsMap._
object Commands {
  case class CreatePoll (name : String,
                         anon : Boolean,
                         resShow : Boolean,
                         startTime: DateTime,
                         endTime: DateTime) extends Command {
    override def execute(): String = {
      val id = PollsMap.polls.size
      polls +=  id -> new Poll(name,anon,resShow,startTime,endTime,id)
      id.toString
    }
  }


  case class List() extends Command {
    override def execute(): String = {
      polls.toString()
    }
  }

  case class DeletePoll(id : Int)extends Command{
    override def execute(): String = {
      if (PollsMap.polls.contains(id)){
        PollsMap.polls -= id
        s"delete id:$id"
      }
      "id is not exist"
    }
  }

  case class StartPoll(id : Int)extends Command{
    override def execute(): String = "start poll " + id
  }

  case class StopPoll(id : Int)extends Command{
    override def execute(): String = "stop poll " + id
  }

  case class Result(id : Int)extends Command{
    override def execute(): String = "result " + id
  }

}
