package user_handler
import poll_store._

import scala.collection.mutable
case class User(name: String) {

  override def toString: String = name.toString
}
