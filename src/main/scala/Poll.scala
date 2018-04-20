final case class Poll(pollTitle : String,
           isAnon : Boolean,
           resultsVisibility: Boolean,
           startTime: DateTime,
           endTime: DateTime,
           pollId : Int) {
  var active = true
  def start() : Unit = active = true
  def stop() : Unit = active = false

  override def toString: String = {
    s"""
        |Poll Title: $pollTitle
        |Is Anonymous: $isAnon
        |Results visibility: $resultsVisibility
        |Poll Start Time: $startTime
        |Poll End Time: $endTime
        |Poll Id: $pollId
       |Is poll active now?: $active
    """.stripMargin
  }
}
