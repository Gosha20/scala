import scala.collection.immutable.HashMap

import QuestionTypes.QuestionTypes

case class Question(name:String,
                    questionType : QuestionTypes,
                    answers:Set[String],
                    anonymous : Boolean,
                    votes: HashMap[String,Int] = HashMap.empty ,
                    userVote : HashMap[String, Set[User]] = HashMap.empty ) {
  def addVote(user: User, answer:String): Question ={
    val newvotes = votes + (answer -> (votes.getOrElse(answer, 0)+1))
    val newuservote = if (anonymous) {
      userVote
    } else {
      userVote + (answer -> (userVote.getOrElse(answer, Set[User]()) + user))
    }
    this.copy(votes = newvotes, userVote = newuservote)
  }
}
