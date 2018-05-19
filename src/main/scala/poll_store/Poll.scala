package poll_store
import java.time._
import user_handler.User

case class Poll(pollTitle : String,
                isAnon : Boolean,
                resultsVisibility: Boolean,
                startTime: LocalDateTime,
                endTime: LocalDateTime,
                pollId : Int,
                creator : User,
                active: Boolean = false) {

  override def toString: String = {
    s"""
|Poll Title: $pollTitle
|Is Anonymous: $isAnon
|Results visibility: $resultsVisibility
|Poll Start Time: $startTime
|Poll End Time: $endTime
|Poll Id: $pollId
|Is poll active now?: $active
|Creator: $creator""".stripMargin
  }
}
