package poll_store

import user_handler.User
import java.time.LocalDateTime

case class Poll(pollTitle : String,
                isAnon : Boolean,
                resShown: Boolean,
                startTime: LocalDateTime,
                endTime: LocalDateTime,
                pollId : Int,
                creator : User,
                active: Boolean = false) {

  override def toString: String = {
    s"""
|Poll Title: $pollTitle
|Is Anonymous: $isAnon
|Results visibility: $resShown
|Poll Start Time: $startTime
|Poll End Time: $endTime
|Poll Id: $pollId
|Is poll active now?: $active
|Creator: $creator""".stripMargin
  }
}
