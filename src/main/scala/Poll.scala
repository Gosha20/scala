class Poll( name : String,
            anon : Boolean,
            visibility: Boolean,
            startTime: DateTime,
            endTime: DateTime,
            id : Int,
            active: Boolean) {

  def start() : Unit = ???
  def stop() : Unit = ???
  
  override def toString: String = {
    s"""
        |Name: $name
        |Anon: $anon
        |Visa: $visibility
        |start: $startTime
        |end: $endTime
        |id: $id
    """.stripMargin
  }
}
