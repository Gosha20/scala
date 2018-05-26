package commands

import my_parser.Command
import poll_store._
import question.QuestionTypes.QuestionTypes
import user_handler._

import scala.collection.immutable.HashMap
import question._

import scala.util.Try

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

  case class View() extends Command {
    override def perform(userHandler: UserHandler): String = {
      val poll = PollsStore.userWorkWithPoll.getOrElse(userHandler.user, return "need be in contex mode")
      val questions = PollsStore.pollQuestion(poll).values

      poll.toString + "\n" + questions.mkString("\n")
    }
  }

  case class AddQuestion(name:String, questionType: QuestionTypes, answers:Array[String]) extends Command {
    override def perform(userHandler: UserHandler): String = {
      PollsStore.checkTime()
      val poll = PollsStore.userWorkWithPoll.getOrElse(userHandler.user,
        return "This command works only in context mode. You should enter it first")
      if (poll.creator == userHandler.user && !poll.active){
            val pollQuestion = PollsStore.pollQuestion.getOrElse(poll,HashMap())
            val questionId = PollsStore.getMinId(pollQuestion)
            val question = Question(questionId, name, questionType, answers, poll.isAnon)
            PollsStore.pollQuestion += (poll -> (pollQuestion + (questionId -> question)))
            s"Question added to Poll. Question Id - $questionId"
      }else
        "You can't add question to this poll, you are not poll's creator or poll is active"
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
            s"There is no question with id $idQuestion"}
        }else
            "You can't delete the question, you are not poll's creator or poll is active"
    }

  }

  case class AnswerQuestion(questionId:Int, answer: String) extends Command {
    override def perform(userHandler: UserHandler): String = {
      PollsStore.checkTime()
      val poll = PollsStore.userWorkWithPoll.getOrElse(userHandler.user,
        return "This command works only in context mode. You should enter it first")

      if (poll.active){
        val question = PollsStore.pollQuestion(poll).getOrElse(questionId, return "No question with that number")
        if (question.users.contains(userHandler.user)){
          "You've already voted or answered that question"
        }else {
          question.questionType match {
            case QuestionTypes.Choice => {
              val answerById = getAnswerById(question, answer)
              if (answerById != "")
                addAnswer(poll, userHandler.user, answerById)
              else
                "wrong answer id"
            }
            case QuestionTypes.Multi => {
              val answerParsed = answer.split(' ')
              if (answerParsed.distinct.length == answerParsed.length) {
                answerParsed.foreach(answerId =>
                  {
                    val answer = getAnswerById(question, answerId)
                    if (answer == "")
                      return "wrong answer id"
                  })
                answerParsed.foreach(answer => addAnswer(poll, userHandler.user, getAnswerById(question, answer)))
                "full ok"
              }
              else
                "you cant choice duplicate answer id"
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
    def getAnswerById(question : Question, idInstr: String) : String = {
      val id = Try(idInstr.toInt).toOption
      if (id.isDefined && question.answers.length >= id.get && 1 <= id.get )
        return question.answers(id.get - 1)
      ""
    }
  }
}


