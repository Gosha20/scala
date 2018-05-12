package parser
import user_handler._
trait Command {
  def perform(userHandler: UserHandler) : String
}
