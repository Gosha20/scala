import org.scalatest.WordSpec
import org.scalatest.BeforeAndAfter
import atto._
import Atto._
import PollsStore.polls

import scala.StringContext.InvalidEscapeException
import scala.collection.immutable.HashMap

class Tests extends WordSpec with BeforeAndAfter {

  private val errString: String = "Wrong command"

  // need to remake our time, DateTime lacks helpful methods and there is no integrity verification

  before{
    PollsStore.polls = new HashMap[Int, Poll]
  }

  "User" can{
    "input incorrect command" in{

      }
    }
    "input correct command" in{
      assert(ParserCommands.parseLine("/create_poll MyPool") != errString)
    }
  }
//
//  "parser" should{
//    "parse time correctly" in{
//      assert(ParserCommands.time.parse("22:22:22").option.contains(Time(22, 22, 22)))
////      assert(ParseCommands.time.parse("63:63:63").option.contains(Time(63, 63, 63))) //wtf fix this
//    }
//    "parse word correctly" in{
//      assert(ParserCommands.word.parse("Crown  ").option == Some("Crown"))
////      print(ParseCommands.word.parse("Vik123 ").option.contains(Some("vik123")))//"Vik123" is a problem(without whitespaces)
//    }
//
//  }
//
//  "Polls" must{
//    "be empty" when{
//      "starts" in {
//        assert(PollsStore.polls.isEmpty)
//        ParserCommands.parseLine("/list")
//        assert(PollsStore.polls.isEmpty)
//      }
//      "deletes last pool" in{
//        val id = ParserCommands.parseLine("/create_poll OnePoll")
//        ParserCommands.parseLine(s"/delete_poll $id")
//        assert(PollsStore.polls.isEmpty)
//      }
//    }
//    "have positive IDs" in {
//      val id = ParserCommands.parseLine("/create_poll OnePoll")
//      assert(id.toInt > 0)
//      val id2 = ParserCommands.parseLine("/create_poll SecondPoll12341241")
//      assert(id2.toInt > 0)
//    }
//    "simply create" in {
//      val id = ParserCommands.parseLine("/create_poll Simply")
//      val p = PollsStore.polls(id.toInt)
//      assert(p == Poll(pollTitle = "Simply", isAnon = true, resultsVisibility = false,
//                       startTime = null, endTime = null, pollId = id.toInt))
//    }
//    "create with non-anon flag" in {
//      val id = ParserCommands.parseLine("/create_poll Simply no")
//      val p = PollsStore.polls(id.toInt)
//      assert(p == Poll(pollTitle = "Simply", isAnon = false, resultsVisibility = false,
//        startTime = null, endTime = null, pollId = id.toInt))
//    }
//    "create with non-anon flag and afterstop results visibility flag" in {
//      val id = ParserCommands.parseLine("/create_poll Simply no afterstop")
//      val p = PollsStore.polls(id.toInt)
//      assert(p == Poll(pollTitle = "Simply", isAnon = false, resultsVisibility = true,
//        startTime = null, endTime = null, pollId = id.toInt))
//      //then fix resultsVisibility logic
//    }
//    "create with anon and continuous flags" in {
//      val id = ParserCommands.parseLine("/create_poll Simply yes continuous")
//      val p = PollsStore.polls(id.toInt)
//      assert(p == Poll(pollTitle = "Simply", isAnon = true, resultsVisibility = false,
//        startTime = null, endTime = null, pollId = id.toInt))
//    }
//    "create with start time" in {
//      val id = ParserCommands.parseLine("/create_poll Simply yes continuous 2018-04-21 10:21:31")
//      val p = PollsStore.polls(id.toInt)
//      assert(p == Poll(pollTitle = "Simply", isAnon = true, resultsVisibility = false,
//        startTime = DateTime(Date(2018, 4,21),Time(10,21,31)), endTime = null, pollId = id.toInt))
//    }
//    "create with end time" in {
//      val id = ParserCommands.parseLine("/create_poll Simply yes continuous 2018-04-21 10:21:31 2019-04-21 10:21:31")
//      val p = PollsStore.polls(id.toInt)
//      assert(p == Poll(pollTitle = "Simply", isAnon = true, resultsVisibility = false,
//        startTime = DateTime(Date(2018, 4, 21), Time(10, 21, 31)), endTime = DateTime(Date(2019, 4, 21), Time(10, 21, 31)),
//        pollId = id.toInt))
//    }
//    "start correctly" in {
//      val id = ParserCommands.parseLine("/create_poll first")
//      ParserCommands.parseLine(s"/start_poll $id")
//      assert(PollsStore.polls(id.toInt).active)
//    }
//    "stop correctly" in {
//      val id = ParserCommands.parseLine("/create_poll first")
//      ParserCommands.parseLine(s"/start_poll $id")
//      assert(PollsStore.polls(id.toInt).active)
//      ParserCommands.parseLine(s"/stop_poll $id")
//      assert(!PollsStore.polls(id.toInt).active)
//    }
//    "delete correctly 1" in {
//      val id1 = ParserCommands.parseLine("/create_poll first")
//      val id2 = ParserCommands.parseLine("/create_poll second")
//      val id3 = ParserCommands.parseLine("/create_poll third")
//      ParserCommands.parseLine(s"/start_poll $id1")
//      ParserCommands.parseLine(s"/start_poll $id2")
//      ParserCommands.parseLine(s"/start_poll $id3")
//      ParserCommands.parseLine(s"/delete_poll $id2")
//      assert(!PollsStore.polls.contains(id2.toInt))
//      assert(PollsStore.polls.contains(id1.toInt) & PollsStore.polls.contains(id3.toInt))
//      ParserCommands.parseLine(s"/delete_poll $id3")
//      assert(!PollsStore.polls.contains(id3.toInt) & !PollsStore.polls.contains(id2.toInt))
//      assert(PollsStore.polls.contains(id1.toInt))
//    }
//    "delete correctly 2" in {
//       assert(ParserCommands.parseLine("/delete_poll 1") == "no id")
//    }
//    "not to start unexisting poll" in{
//      assert(ParserCommands.parseLine("/start_poll 1") == "no id")
//    }
  }
