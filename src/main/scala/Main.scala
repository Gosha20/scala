import scala.collection.immutable.HashMap
import scala.io.Source
object Main {
  def main(args: Array[String]) : Unit = {
    val user = User("gosha")
    val user2 = User("pepte")
    val userHandler2 = UserHandler(user2)
    val userHandler = UserHandler(user)

    println(userHandler.performCommand("/create_poll test1"))
//    println(userHandler.performCommand("/create_poll test2"))
//    println(userHandler.performCommand("/list"))
//    println(PollsStore.userWorkWithPoll.toString())
//    println(userHandler.performCommand("/view"))
//    println(userHandler.performCommand("/begin 110251487"))
//    println(userHandler.performCommand("/view"))
//    println(userHandler.performCommand("/stop_poll 110251487"))
//    println(PollsStore.userWorkWithPoll.toString())
//    println(userHandler.performCommand("/end"))
//    println(userHandler.performCommand("/view"))
    println(userHandler.performCommand("/begin 110251487"))
    println(userHandler.performCommand("/delete_question 0"))
    println(userHandler.performCommand("/add_question <da> open"))
    println(PollsStore.pollQuestion.toString())
    println(userHandler.performCommand("/add_question <Go wolk1?> choice \na\nb\nc"))
    println(PollsStore.pollQuestion.toString())
    println(userHandler.performCommand("/delete_question 0"))
    println(userHandler.performCommand("/add_question <Go wolk2?> multi \n" +
      "da\n" +
      "net\n" +
      "none"))


    println(PollsStore.pollQuestion.toString())
    println(userHandler.performCommand("/answer 5 <answer>"))
  }
}
