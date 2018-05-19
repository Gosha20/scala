package question

import question.QuestionTypes.QuestionTypes
import user_handler.User
import scala.collection.immutable.HashMap

case class Question(name:String,
                    questionType : QuestionTypes,
                    answers : Set[String],
                    anonymous : Boolean,
                    votes : HashMap[String,Int] = HashMap.empty ,
                    users : Set[User] = Set[User](),
                    userVote : HashMap[String, Set[User]] = HashMap.empty ) {

  def addVote(user: User, answer: String): Question = {
      val newvotes = votes + (answer -> (votes.getOrElse(answer, 0) + 1))
      val newUsers = users + user
      val newuservote = if (anonymous) {
        userVote
      } else {
        userVote + (answer -> (userVote.getOrElse(answer, Set[User]()) + user))
      }
      this.copy(votes = newvotes, userVote = newuservote, users = newUsers)
  }
  override def toString: String = {
    val variants = answers.mkString(";")
    val votesCount = users.size
    val variantVotesSet =
      for(variant <- votes.keys) yield s"'$variant' variant votes: "+
        votes(variant).toString()
    val variantVotes = variantVotesSet.mkString("\n")
    val participants = if (anonymous){
      "\nParticipants: Anon"
    } else {
      "\nParticipants:" + users.mkString(",")
    }
    s"""
       |Question: $name
       |Variants: $variants
       |Question Type: $questionType
       |Anon: $anonymous
       |Votes Amount: $votesCount $participants
       |$variantVotes
     """.stripMargin
  }
}
