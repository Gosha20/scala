import QuestionTypes.QuestionTypes

import scala.collection.immutable.HashMap

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
  case class AddQuestion(name:String, questionType: QuestionTypes, answers:Set[String]) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.userWorkWithPoll.getOrElse(userHandler.User, return "vi ne rabotaete s polom")
      val question = Question(name,questionType,answers, poll.isAnon)
      val pollQuestion = PollsStore.pollQuestion.getOrElse(poll,HashMap())
      val questionId = (0 to pollQuestion.size).filter((i:Int) => !pollQuestion.contains(i))(0)
      PollsStore.pollQuestion += (poll -> (pollQuestion + (questionId -> question)))
      s"done, question id - $questionId"
    }
  }

  case class DeleteQuestion(idQuestion:Int) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.userWorkWithPoll.getOrElse(userHandler.User, return "vi ne rabotaete s polom")
      val pollQuestion = PollsStore.pollQuestion.getOrElse(poll,HashMap())
      if (pollQuestion.contains(idQuestion)) {
        PollsStore.pollQuestion += (poll -> (pollQuestion - idQuestion))
        "question is deleted"
      }else{
        s"net voprosa s id $idQuestion"
      }

    }

  }

  case class AnswerQuestion(id:Int, answer: String) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.userWorkWithPoll.getOrElse(userHandler.User, return "vi ne vibrali vopros")
      val question = PollsStore.pollQuestion(poll).getOrElse(id, return "net takoko question")
      question.questionType match {
        case QuestionTypes.Choice => {
          ???
        }
        case QuestionTypes.Multi => {
          ???
        }
        case QuestionTypes.Open => {
          ???
        }
      }
      ""
    }

  }
}


