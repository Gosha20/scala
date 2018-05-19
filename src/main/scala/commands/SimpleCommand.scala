package commands

import poll_store.Poll, poll_store.PollsStore
import java.time.LocalDateTime
import my_parser.Command
import user_handler.UserHandler

object SimpleCommand {
  case class CreatePoll (pollTitle : String,
                       isAnon : Boolean,
                       resShown : Boolean,
                       startTime: LocalDateTime,
                       endTime: LocalDateTime) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val pollId = PollsStore.getMinId(PollsStore.polls)
//      val poll = Poll(pollTitle, isAnon, resShown, startTime, endTime, pollId, userHandler.User)
      val poll = Poll(pollTitle, isAnon, resShown, startTime, endTime, pollId, userHandler.User)
      PollsStore.addPoll(poll, pollId)
      s"Your poll ID: $pollId"
    }
  }

  case class ToList() extends Command {
    override def perform(userHandler: UserHandler): String = "Polls:\n" + PollsStore.getPollsList
  }


  case class DeletePoll(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return s"There is no poll with that ID")
      if (poll.creator != userHandler.User) {
        "You can't delete that poll, you are not poll's creator"
      }
      else{
        PollsStore.deletePoll(id, userHandler.User)
      }
    }
  }


  case class StartPoll(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return "There is no poll with that ID")
      if (poll.creator == userHandler.User && poll.startTime == null) {
        PollsStore.update(poll.copy(active = true))
        s"Poll with ID $id has started"
      }
      else {
        "You can't start poll, you are not poll's creator"
      }
    }
  }


  case class StopPoll(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return "There is no poll with that ID")
      if (poll.creator == userHandler.User && poll.endTime == null ) {
        PollsStore.update(poll.copy(active = false))
        s"poll with id $id stopped"
      }
      else {
        "You cant stop poll, you are not creator"
      }
    }
  }

  case class GetResults(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return s"There is no poll with that ID")
      if (!poll.resShown)
        PollsStore.pollQuestion(poll).toString()
      else
        "Results will be shown after poll's finish"
    }
  }
}