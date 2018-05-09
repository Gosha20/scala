trait Command {
  def perform(userHandler: UserHandler) : String
}
