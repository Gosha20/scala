package my_parser
import user_handler._
trait Command {
  def perform(userHandler: UserHandler) : String
}
