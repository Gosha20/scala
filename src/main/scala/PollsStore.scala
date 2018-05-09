import java.lang.Math.abs

import scala.collection.immutable.HashMap
object PollsStore {
  var polls = new HashMap[Int, Poll]
  var userWorkWithPoll = new HashMap[User, Poll]
  var pollQuestion = new HashMap[Poll, HashMap[Int, Question]]

  def addPoll(poll : Poll, user : User): Unit ={
    polls +=  poll.pollId -> poll
  }
  def getPollsList: String =polls.toString()

  def deletePoll(id : Int, user : User) :Unit = {
    val poll = polls(id)
    polls -= id
    pollQuestion -= poll
  }
  def setBeginWork(user: User, poll: Poll): Unit =userWorkWithPoll += (user -> poll)
  def setEndWork(user: User) : Unit = userWorkWithPoll -= user
  def update(poll : Poll): Unit = polls += (poll.pollId -> poll)

}
