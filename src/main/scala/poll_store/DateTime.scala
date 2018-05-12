package poll_store

case class Date(year: Int, month: Int, day: Int)
case class Time(hour: Int, minutes: Int, seconds: Int)
case class DateTime(date: Date, time: Time)

object DateTime {
//  val emptyTime = new DateTime(Date(0,0,0),Time(0,0,0))
  val emptyTime = null
}