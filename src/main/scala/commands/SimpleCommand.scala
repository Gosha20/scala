package commands

import poll_store.Poll, poll_store.PollsStore
import java.time.LocalDateTime
import my_parser.Command
import user_handler.UserHandler

object SimpleCommand {
  case class CreatePoll (pollTitle : String,
                       isAnon : Boolean,
                       resShown : String,
                       startTime: LocalDateTime,
                       endTime: LocalDateTime) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val pollId = PollsStore.getMinId(PollsStore.polls)
      val poll = Poll(pollTitle, isAnon, resShown, startTime, endTime, pollId, userHandler.user)
      PollsStore.addPoll(poll, pollId)
      s"Your poll ID: $pollId"
    }
  }

  case class ToList() extends Command {
    override def perform(userHandler: UserHandler): String =
      {
        PollsStore.checkTime()
        "Polls:\n" + PollsStore.getPollsList
      }
  }


  case class DeletePoll(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return s"There is no poll with that ID")
      if (poll.creator != userHandler.user) {
        "You can't delete that poll, you are not poll's creator"
      }
      else{
        PollsStore.deletePoll(id, userHandler.user)
      }
    }
  }

  case class StartPoll(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return "There is no poll with that ID")
      if (poll.creator == userHandler.user && poll.startTime == null) {
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
      if (poll.creator == userHandler.user && poll.endTime == null) {
        PollsStore.update(poll.copy(active = false))
        s"poll with id $id stopped"
      }
      else {
        "You cant stop poll, you are not creator or poll will stop avto"
      }
    }
  }

  case class GetResults(id : Int)extends Command{
    override def perform(userHandler: UserHandler): String = {
      PollsStore.checkTime()
      val poll = PollsStore.polls.getOrElse(id, return s"There is no poll with that ID")

      if (poll.resShown == "continuous")
        return poll.getResult

      else{
        if (!poll.active){
          if (poll.startTime == null)
            poll.getResult
          else{
            if (LocalDateTime.now().isAfter(poll.startTime))
              poll.getResult
          }
        }

      }
        "Results will be shown after poll's finish"
    }
  }
}