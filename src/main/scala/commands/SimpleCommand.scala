package commands

import java.lang.Math._
import poll_store._
import parser.Command
import user_handler.UserHandler
object SimpleCommand {
case class CreatePoll (pollTitle : String,
                       isAnon : Boolean,
                       resShown : Boolean,
                       startTime: DateTime,
                       endTime: DateTime) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val pollId = PollsStore.getMinId(PollsStore.polls)
      val poll = Poll(pollTitle, isAnon, resShown, startTime, endTime, pollId, userHandler.User)
      PollsStore.addPoll(poll, pollId)
    }
  }

//TODO (красивый вывод)
  case class ToList() extends Command {
    override def perform(userHandler: UserHandler): String = PollsStore.getPollsList
  }


  case class DeletePoll(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return s"havent poll with this id $id")
      if (poll.creator != userHandler.User) {
        "You cant delete poll, you are not creator"
      }
      else{
        PollsStore.deletePoll(id, userHandler.User)
      }
    }
  }


  case class StartPoll(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return "havent poll with this id")
      if (poll.creator == userHandler.User && poll.startTime == null) {
        PollsStore.update(poll.copy(active = true))
        s"poll with id $id started"
      }
      else {
        "You cant start poll, you are not creator"
      }
    }
  }


  case class StopPoll(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return "havent poll with this id")
      if (poll.creator == userHandler.User && poll.endTime == null ) {
        PollsStore.update(poll.copy(active = false))
        s"poll with id $id stopped"
      }
      else {
        "You cant stop poll, you are not creator"
      }
    }
  }

//TODO красиво результат голосования
  case class GetResults(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return s"havent poll with this id $id")
      if (!poll.resultsVisibility)
        PollsStore.pollQuestion(poll).toString()
      else
        "only afterstop"
    }
  }
}