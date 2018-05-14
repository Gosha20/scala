package poll_store

import user_handler.User
import question._
import scala.collection.immutable.HashMap

object PollsStore {
  var polls = new HashMap[Int, Poll]
  var userWorkWithPoll = new HashMap[User, Poll]
  var pollQuestion = new HashMap[Poll, HashMap[Int, Question]]

  def getMinId(map:HashMap[Int, Any]) : Int = {
    (0 to map.size).filter((i:Int) => !map.contains(i))(0)
  }

  def addPoll(poll : Poll, pollId : Int): String ={
      polls += pollId -> poll
      s"poll is created with id $pollId"
  }
//TODO красивый вывод
  def getPollsList: String = polls.toString()

  def deletePoll(id : Int, user : User) : String = {
    val poll = polls(id)
    polls -= id
    pollQuestion -= poll
    s"poll is deleted id $id"
  }

  def setBeginWork(user: User, poll: Poll): Unit =userWorkWithPoll += (user -> poll)
  def setEndWork(user: User) : Unit = userWorkWithPoll -= user
  def update(poll : Poll): Unit = polls += (poll.pollId -> poll)

}
