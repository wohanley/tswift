package twitter

import songs._
import twitter4j._


class Bot {

  def main(args: Array[String]): Unit = {
    val text = "You're on the phone with your girlfriend, she's upset\nShe's going off about something that you said"
    println(songMatch(text).map { case SongMatch(title, lines) =>
      splitToMatchLines(text, lines)
    })
  }
}


object HoseListener extends StatusListener {

  def onStatus(status: Status): Unit = {
    songMatch(status.getText()).map { case SongMatch(title, lines) =>
      tweet("tune of " + title + ":\n" +
        splitToMatchLines(status.getText(), lines))
      }
  }

  // fuck all these
  def onDeletionNotice(x$1: twitter4j.StatusDeletionNotice): Unit = {}
  def onScrubGeo(x$1: Long,x$2: Long): Unit = {}
  def onStallWarning(x$1: twitter4j.StallWarning): Unit = {}
  def onTrackLimitationNotice(x$1: Int): Unit = {}
  def onException(x$1: Exception): Unit = {}
}
