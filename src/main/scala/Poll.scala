class Poll( name : String,
            anon : Boolean,
            visibility: Boolean,
            startTime: DateTime,
            endTime: DateTime,
            id : Int) {
  var active = false
  def start() : Unit = active = true
  def stop() : Unit = active = false

  override def toString: String = {
    s"""
        |Name: $name
        |Anon: $anon
        |Visa: $visibility
        |start: $startTime
        |end: $endTime
        |id: $id
       |started: $active
    """.stripMargin
  }
}
