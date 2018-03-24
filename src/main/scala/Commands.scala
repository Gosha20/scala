import DateTime._

object Commands {
  case class CreatePoll (name : String,
                         anon : Boolean,
                         resShow : Boolean,
                         startTime: DateTime,
                         endTime: DateTime) extends Command {
    override def execute(): String = {
      "create poll with params " + name + anon + resShow + startTime + endTime

    }
  }
  case class List() extends Command {
    override def execute(): String = "some list polls"
  }
  case class DeletePoll(id : Int)extends Command{
    override def execute(): String = "delete poll " + id
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
