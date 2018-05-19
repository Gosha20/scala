package user_handler

import my_parser.{Command, ParserCommands}

case class UserHandler(user : User) {

  def performCommand(stringCmd : String) : String = {
    ParserCommands.getCommand(stringCmd) match {
      case Some(cmd) => perform(cmd)
      case None => "Wrong command"
    }
  }

  def perform(cmd : Command) : String = cmd.perform(this)
}
