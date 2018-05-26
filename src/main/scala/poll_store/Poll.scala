package poll_store
import java.time._

import user_handler.User

import scala.collection.mutable

final case class Poll(pollTitle : String,
                isAnon : Boolean,
                resShown: String,
                startTime: LocalDateTime,
                endTime: LocalDateTime,
                pollId : Int,
                creator : User,
                active: Boolean = false) {

  def checkTime() : Unit = {
    val nowtime = LocalDateTime.now()
    if (startTime != null){
      if(nowtime.isAfter(startTime)) {
        if (endTime == null)
          PollsStore.update(this.copy(active = true))
        else {
          PollsStore.update(this.copy(active = nowtime.isBefore(endTime)))
        }
      }
    }
  }

  override def toString: String = {
    s"""
|Poll Title: $pollTitle
|Is Anonymous: $isAnon
|Results visibility: $resShown
|Poll Start Time: $startTime
|Poll End Time: $endTime
|Poll Id: $pollId
|Is poll active now?: $active
|Creator: $creator""".stripMargin  }

  def getResult : String = {
        val questions = PollsStore.pollQuestion.getOrElse(this, mutable.HashMap())
        this.toString + "\nQuestions:" + questions.values.mkString("~~~~~~~~~~")
  }
}

