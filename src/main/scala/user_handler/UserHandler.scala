package user_handler

import parser.{Command, ParserCommands}

case class UserHandler(User : User) {

  def performCommand(stringCmd : String) : String = {
    ParserCommands.getCommand(stringCmd) match {
      case Some(cmd) => perform(cmd)
      case None => "Wrong command"
    }
  }

  def perform(cmd : Command) : String = cmd.perform(this)
}
