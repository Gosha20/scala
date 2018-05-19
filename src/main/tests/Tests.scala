import org.scalatest.WordSpec
import org.scalatest.BeforeAndAfter
import atto._
import Atto._
import my_parser.ParserCommands
import poll_store.{DateTime, Poll, PollsStore}
import java.time.LocalDateTime
import user_handler._

import commands._

import scala.StringContext.InvalidEscapeException
import scala.collection.immutable.HashMap

class Tests extends WordSpec with BeforeAndAfter {

  private val user = User("Name")
  private val executor = UserHandler(user)

  private def getId(string: String): Int = getIdInString(executor.performCommand(s"/create_poll $string"))

  private def getIdInString(string: String): Int =
    string.filter(_.isDigit).toInt

  private val unknownCommand: String = "Wrong command"
  private val noPollWithID: String = "There is no poll with that ID"

  before {
    PollsStore.polls = new HashMap[Int, Poll]
  }

  "user_handler.User" can {
    "input incorrect command" in {
      assert(executor.performCommand("/crtpoll qwe") == unknownCommand)
      assert(executor.performCommand("/lst") == unknownCommand)
      assert(executor.performCommand("/create poll lmao") == unknownCommand)
    }
  }
  "input correct command" in {
    assert(executor.performCommand("/create_poll MyPool").toString != unknownCommand)
    assert(executor.performCommand("/list") != unknownCommand)
  }


  "Polls" must {
    "be empty" when {
      "starts" in {
        assert(PollsStore.polls.isEmpty)
        ParserCommands.getCommand("/list").get
        assert(PollsStore.polls.isEmpty)
      }
      "deletes last pool" in {
        val id = getId("OnePoll")
        executor.performCommand(s"/delete_poll $id")
        assert(PollsStore.polls.isEmpty)
      }
    }
    "have positive IDs" in {
      val id = getId("OnePoll")
      assert(id >= 0)
      val id2 = getId("SecondPoll12341241")
      assert(id2.toInt >= 0)
    }
    "simply create" in {
      val repr = ParserCommands.getCommand("/create_poll Simply").get
      assert(repr == SimpleCommand.CreatePoll(pollTitle = "Simply", isAnon = true, resShown = false,
        startTime = null, endTime = null))
    }
    "create with non-anon flag" in {
      val id = getId("Simply no")
      val p = PollsStore.polls(id)
      assert(p == poll_store.Poll(pollTitle = "Simply", isAnon = false, resShown = false,
        startTime = null, endTime = null, pollId = id.toInt, creator = user))
    }
    "create with non-anon flag and afterstop results visibility flag" in {
      val id = getId("Simply no afterstop")
      val p = PollsStore.polls(id)
      assert(p == poll_store.Poll(pollTitle = "Simply", isAnon = false, resShown = true,
        startTime = null, endTime = null, pollId = id, creator = user))
    }
    "create with anon and continuous flags" in {
      val id = getId("Simply yes continuous")
      val p = PollsStore.polls(id)
      assert(p == poll_store.Poll(pollTitle = "Simply", isAnon = true, resShown = false,
        startTime = null, endTime = null, pollId = id, creator = user))
    }
    "create with start time" in {
      val id = getId("Simply yes continuous 2018-04-21 10:21:31")
      val p = PollsStore.polls(id)
      assert(p == poll_store.Poll(pollTitle = "Simply", isAnon = true, resShown = false,
        startTime = LocalDateTime.of(2018, 4, 21, 10, 21, 31), endTime = null, pollId = id, creator = user))
    }
    "create with end time" in {
      val id = getId("Simply yes continuous 2018-04-21 10:21:31 2019-04-21 10:21:31")
      val p = PollsStore.polls(id)
      assert(p == poll_store.Poll(pollTitle = "Simply", isAnon = true, resShown = false,
        startTime = LocalDateTime.of(2018, 4, 21, 10, 21, 31), endTime = LocalDateTime.of(2019, 4, 21, 10, 21, 31),
        pollId = id, creator = user))
    }
    "start correctly" in {
      val id = getId("first")
      executor.performCommand(s"/start_poll $id")
      assert(PollsStore.polls(id).active)
    }
    "stop correctly" in {
      val id = getId("first")
      executor.performCommand(s"/start_poll $id")
      assert(PollsStore.polls(id).active)
      executor.performCommand(s"/stop_poll $id")
      assert(!PollsStore.polls(id).active)
    }
    "delete correctly 1" in {
      val id1 = getId("first")
      val id2 = getId("second")
      val id3 = getId("third")
      executor.performCommand(s"/start_poll $id1")
      executor.performCommand(s"/start_poll $id2")
      executor.performCommand(s"/start_poll $id3")
      executor.performCommand(s"/delete_poll $id2")
      assert(!PollsStore.polls.contains(id2))
      assert(PollsStore.polls.contains(id1) & PollsStore.polls.contains(id3))
      executor.performCommand(s"/delete_poll $id3")
      assert(!PollsStore.polls.contains(id3) & !PollsStore.polls.contains(id2))
      assert(PollsStore.polls.contains(id1))
    }
    "delete correctly 2" in {
      assert(executor.performCommand("/delete_poll 1") == noPollWithID)
    }
    "not to start unexisting poll" in {
      assert(executor.performCommand("/start_poll 1") == noPollWithID)
    }
    "have nice list with several polls" in {
      executor.performCommand("/create_poll f")
      executor.performCommand("/create_poll g")
      assert(executor.performCommand("/list") == "Polls:\n" +
        """
           |Poll Title: f
           |Is Anonymous: true
           |Results visibility: false
           |Poll Start Time: null
           |Poll End Time: null
           |Poll Id: 0
           |Is poll active now?: false
           |Creator: Name""".stripMargin + "\n\n----------\n" +
        """
           |Poll Title: g
           |Is Anonymous: true
           |Results visibility: false
           |Poll Start Time: null
           |Poll End Time: null
           |Poll Id: 1
           |Is poll active now?: false
           |Creator: Name""".stripMargin)
    }
    "have nice list with zero polls" in {
      assert(executor.performCommand("/list") == "Polls:\n")
    }
  }
}

