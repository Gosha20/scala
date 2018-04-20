import org.scalatest.WordSpec
import org.scalatest.BeforeAndAfter
import atto._
import Atto._
import PollsMap.polls

import scala.StringContext.InvalidEscapeException
import scala.collection.immutable.HashMap

class TestsSpec extends WordSpec with BeforeAndAfter {

  private val errString: String = "Wrong command"

  // need to remake our time, DateTime lacks helpful methods and there is no integrity verification

  before{
    PollsMap.polls = new HashMap[Int, Poll]
  }

  "User" can{
    "input incorrect command" in{
      assertResult(errString) {
        ParseCommands.parseLine("")
        ParseCommands.parseLine("/LMAO")
        ParseCommands.parseLine("/create_pool Test")
        ParseCommands.parseLine("/delete 1")
        ParseCommands.parseLine("/start 1")
        ParseCommands.parseLine("/stop 2")
        //        ParseCommands.parseLine("/list 23") //fix this
        //        ParseCommands.parseLine(null)//do we need to fix this?
      }
    }
    "input correct command" in{
      assert(ParseCommands.parseLine("/create_poll MyPool") != errString)
    }
  }

  "Parser" should{
    "parse time correctly" in{
      assert(ParseCommands.time.parse("22:22:22").option.contains(Time(22, 22, 22)))
//      assert(ParseCommands.time.parse("63:63:63").option.contains(Time(63, 63, 63))) //wtf fix this
    }
    "parse word correctly" in{
      assert(ParseCommands.word.parse("Crown  ").option == Some("Crown"))
//      print(ParseCommands.word.parse("Vik123 ").option.contains(Some("vik123")))//"Vik123" is a problem(without whitespaces)
    }

  }

  "Polls" must{
    "be empty" when{
      "starts" in {
        assert(PollsMap.polls.isEmpty)
        ParseCommands.parseLine("/list")
        assert(PollsMap.polls.isEmpty)
      }
      "deletes last pool" in{
        val id = ParseCommands.parseLine("/create_poll OnePoll")
        ParseCommands.parseLine(s"/delete_poll $id")
        assert(PollsMap.polls.isEmpty)
      }
    }
    "have positive IDs" in {
      val id = ParseCommands.parseLine("/create_poll OnePoll")
      assert(id.toInt > 0)
      val id2 = ParseCommands.parseLine("/create_poll SecondPoll12341241")
      assert(id2.toInt > 0)
    }
    "simply create" in {
      val id = ParseCommands.parseLine("/create_poll Simply")
      val p = PollsMap.polls(id.toInt)
      assert(p == Poll(pollTitle = "Simply", isAnon = true, resultsVisibility = false,
                       startTime = null, endTime = null, pollId = id.toInt))
    }
    "create with non-anon flag" in {
      val id = ParseCommands.parseLine("/create_poll Simply no")
      val p = PollsMap.polls(id.toInt)
      assert(p == Poll(pollTitle = "Simply", isAnon = false, resultsVisibility = false,
        startTime = null, endTime = null, pollId = id.toInt))
    }
    "create with non-anon flag and afterstop results visibility flag" in {
      val id = ParseCommands.parseLine("/create_poll Simply no afterstop")
      val p = PollsMap.polls(id.toInt)
      assert(p == Poll(pollTitle = "Simply", isAnon = false, resultsVisibility = true,
        startTime = null, endTime = null, pollId = id.toInt))
      //then fix resultsVisibility logic
    }
    "create with anon and continuous flags" in {
      val id = ParseCommands.parseLine("/create_poll Simply yes continuous")
      val p = PollsMap.polls(id.toInt)
      assert(p == Poll(pollTitle = "Simply", isAnon = true, resultsVisibility = false,
        startTime = null, endTime = null, pollId = id.toInt))
    }
    "create with start time" in {
      val id = ParseCommands.parseLine("/create_poll Simply yes continuous 2018-04-21 10:21:31")
      val p = PollsMap.polls(id.toInt)
      assert(p == Poll(pollTitle = "Simply", isAnon = true, resultsVisibility = false,
        startTime = DateTime(Date(2018, 4,21),Time(10,21,31)), endTime = null, pollId = id.toInt))
    }
    "create with end time" in {
      val id = ParseCommands.parseLine("/create_poll Simply yes continuous 2018-04-21 10:21:31 2019-04-21 10:21:31")
      val p = PollsMap.polls(id.toInt)
      assert(p == Poll(pollTitle = "Simply", isAnon = true, resultsVisibility = false,
        startTime = DateTime(Date(2018, 4, 21), Time(10, 21, 31)), endTime = DateTime(Date(2019, 4, 21), Time(10, 21, 31)),
        pollId = id.toInt))
    }
    "start correctly" in {
      val id = ParseCommands.parseLine("/create_poll first")
      ParseCommands.parseLine(s"/start_poll $id")
      assert(PollsMap.polls(id.toInt).active)
    }
    "stop correctly" in {
      val id = ParseCommands.parseLine("/create_poll first")
      ParseCommands.parseLine(s"/start_poll $id")
      assert(PollsMap.polls(id.toInt).active)
      ParseCommands.parseLine(s"/stop_poll $id")
      assert(!PollsMap.polls(id.toInt).active)
    }
    "delete correctly 1" in {
      val id1 = ParseCommands.parseLine("/create_poll first")
      val id2 = ParseCommands.parseLine("/create_poll second")
      val id3 = ParseCommands.parseLine("/create_poll third")
      ParseCommands.parseLine(s"/start_poll $id1")
      ParseCommands.parseLine(s"/start_poll $id2")
      ParseCommands.parseLine(s"/start_poll $id3")
      ParseCommands.parseLine(s"/delete_poll $id2")
      assert(!PollsMap.polls.contains(id2.toInt))
      assert(PollsMap.polls.contains(id1.toInt) & PollsMap.polls.contains(id3.toInt))
      ParseCommands.parseLine(s"/delete_poll $id3")
      assert(!PollsMap.polls.contains(id3.toInt) & !PollsMap.polls.contains(id2.toInt))
      assert(PollsMap.polls.contains(id1.toInt))
    }
    "delete correctly 2" in {
       assert(ParseCommands.parseLine("/delete_poll 1") == "no id")
    }
    "not to start unexisting poll" in{
      assert(ParseCommands.parseLine("/start_poll 1") == "no id")
    }
  }
}
