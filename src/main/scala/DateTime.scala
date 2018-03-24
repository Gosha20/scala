object DateTime {
  val emptyTime = new DateTime(Date(0,0,0),Time(0,0,0))
}
  case class Date(year: Int, month: Int, day: Int)
  case class Time(hour: Int, minutes: Int, seconds: Int)
  case class DateTime(date: Date, time: Time)
