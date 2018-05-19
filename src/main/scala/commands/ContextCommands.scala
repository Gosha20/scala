package commands

import my_parser.Command
import poll_store._
import question.QuestionTypes.QuestionTypes
import user_handler._
import scala.collection.immutable.HashMap
import question._

object ContextCommands {
  case class Begin(id: Int) extends Command {
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.polls.getOrElse(id, return s"There is no poll with ID: $id")
      PollsStore.setBeginWork(userHandler.user, poll)
      s"Context mode is on. You are working with poll $id now"
      }
    }

  case class End() extends Command {
    override def perform(userHandler: UserHandler): String =
      if (PollsStore.userWorkWithPoll.contains(userHandler.user)){
        PollsStore.setEndWork(userHandler.user)
        s"Context mode is off"
      }
    else{
        "This command works only in context mode. You should enter it first"
      }
  }

//To DO
  case class View() extends Command {
    override def perform(userHandler: UserHandler): String = PollsStore.polls(PollsStore.userWorkWithPoll(userHandler.user).pollId).toString
    }

  case class AddQuestion(name:String, questionType: QuestionTypes, answers:Set[String]) extends Command {
    override def perform(userHandler: UserHandler): String = {
      PollsStore.checkTime()
      val poll = PollsStore.userWorkWithPoll.getOrElse(userHandler.user,
        return "This command works only in context mode. You should enter it first")
      if (poll.creator == userHandler.user && !poll.active){
            val question = Question(name, questionType, answers, poll.isAnon)
            val pollQuestion = PollsStore.pollQuestion.getOrElse(poll,HashMap())
            val questionId = PollsStore.getMinId(pollQuestion)
            PollsStore.pollQuestion += (poll -> (pollQuestion + (questionId -> question)))
            s"Question added to Poll. Question Id - $questionId"
      }else
        "You can't add question to this poll, you are not poll's creator"
    }
  }

  case class DeleteQuestion(idQuestion:Int) extends Command {
    override def perform(userHandler: UserHandler): String = {
      PollsStore.checkTime()
      val poll = PollsStore.userWorkWithPoll.getOrElse(userHandler.user,
        return "This command works only in context mode. You should enter it first")
      if (poll.creator == userHandler.user && !poll.active){
          val pollQuestion = PollsStore.pollQuestion.getOrElse(poll,HashMap())
          if (pollQuestion.contains(idQuestion)) {
            PollsStore.pollQuestion += (poll -> (pollQuestion - idQuestion))
            "Question was deleted"
        }else{
            s"There is no question with number $idQuestion"}
        }else
            "You can't delete the question, you are not poll's creator"
    }

  }
//TODO
  case class AnswerQuestion(questionId:Int, answer: String) extends Command {
    override def perform(userHandler: UserHandler): String = {
      PollsStore.checkTime()
      val poll = PollsStore.userWorkWithPoll.getOrElse(userHandler.user,
        return "This command works only in context mode. You should enter it first")

      if (poll.active){
          val question : Question = PollsStore.pollQuestion(poll).getOrElse(questionId, return "No question with that number")
          if (question.users.contains(userHandler.user)){
            "You've already voted or answered that question"
          }else {
            question.questionType match {
              case QuestionTypes.Choice => {
                addAnswer(poll,userHandler.user,answer)
              }
              case QuestionTypes.Multi => {
                val answerParsed = answer.split(' ')
                answerParsed.foreach(answer => addAnswer(poll,userHandler.user,answer))
                "Answers added"
              }
              case QuestionTypes.Open => {
                addAnswer(poll, userHandler.user, answer)

              }
            }
          }
      }else
        "You can't answer questions of not started poll"
    }

  def addAnswer(poll:Poll, user : User, answ : String) : String = {
    val newQuestion = PollsStore.pollQuestion(poll)(questionId).addVote(user, answ)
    PollsStore.pollQuestion += (poll -> (PollsStore.pollQuestion(poll) + (questionId -> newQuestion)))
    "Answer successfully added!"
  }
  }
}


