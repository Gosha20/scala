package commands

import parser.Command
import poll_store._
import question.QuestionTypes.QuestionTypes
import user_handler._
import scala.collection.immutable.HashMap
import question._

object ContextCommands {
  case class Begin(id: Int) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return s"havent poll with this id $id")
      PollsStore.setBeginWork(userHandler.User, poll)
      s"You worked with poll id $id"
      }
    }

  case class End() extends Command {
    override def perform(userHandler: UserHandler): String =
      if (PollsStore.userWorkWithPoll.contains(userHandler.User)){
        PollsStore.setEndWork(userHandler.User)
        s"You stoped work with poll"
      }
    else{
        "you i ne nachinal boy"
      }
  }

//To DO
  case class View() extends Command {
    override def perform(userHandler: UserHandler): String = userHandler.User.getPollToView

    }

  case class AddQuestion(name:String, questionType: QuestionTypes, answers:Set[String]) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.userWorkWithPoll.getOrElse(userHandler.User, return "vi ne rabotaete s polom")
      if (poll.creator == userHandler.User && !poll.active){
      val question = Question(name, questionType, answers, poll.isAnon)
      val pollQuestion = PollsStore.pollQuestion.getOrElse(poll,HashMap())
      val questionId = PollsStore.getMinId(pollQuestion)
      PollsStore.pollQuestion += (poll -> (pollQuestion + (questionId -> question)))
      s"done, question id - $questionId"
      }else
        "you cant change, you are not creator"
    }
  }

  case class DeleteQuestion(idQuestion:Int) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.userWorkWithPoll.getOrElse(userHandler.User, return "vi ne rabotaete s polom")
      if (poll.creator == userHandler.User && !poll.active){
      val pollQuestion = PollsStore.pollQuestion.getOrElse(poll,HashMap())
      if (pollQuestion.contains(idQuestion)) {
        PollsStore.pollQuestion += (poll -> (pollQuestion - idQuestion))
        "question is deleted"
      }else{
        s"net voprosa s id $idQuestion"}
      }else
        "you cant change, you are not creator"

    }

  }
//TODO
  case class AnswerQuestion(questionId:Int, answer: String) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.userWorkWithPoll.getOrElse(userHandler.User, return "vi ne vibrali vopros")
      if (poll.active){
          val question : Question = PollsStore.pollQuestion(poll).getOrElse(questionId, return "net takoko question")
          if (question.users.contains(userHandler.User)){
            return "vi uje golosovali"
          }else {
            question.questionType match {

              case QuestionTypes.Choice => {
                addAnswer(poll,userHandler.User,answer)
              }

              case QuestionTypes.Multi => {
                val answerParsed = answer.split(' ')
                answerParsed.foreach(answer => addAnswer(poll,userHandler.User,answer))
                return "added "
              }

              case QuestionTypes.Open => {
                addAnswer(poll, userHandler.User, answer)
              }
            }
          }
          "something wrong"
      }else
        "poll not active now, you are so late"
    }

  def addAnswer(poll:Poll, user : User, answ : String) : String = {
    val newQuestion = PollsStore.pollQuestion(poll)(questionId).addVote(user, answ)
    PollsStore.pollQuestion += (poll -> (PollsStore.pollQuestion(poll) + (questionId -> newQuestion)))
    "added"
  }

  }
}


