package twitter

import scala.None
import scala.Some
import songs._
import twitter4j._
import scala.util.Properties


object Bot {

  def main(args: Array[String]): Unit = {
    val stream = new TwitterStreamFactory(new conf.ConfigurationBuilder()
      .setOAuthConsumerKey(Properties.envOrElse("API_KEY", ""))
      .setOAuthConsumerSecret(Properties.envOrElse("API_SECRET", ""))
      .setOAuthAccessToken(Properties.envOrElse("ACCESS_TOKEN", ""))
      .setOAuthAccessTokenSecret(Properties.envOrElse("ACCESS_TOKEN_SECRET", ""))
      .build()).getInstance()

    stream.addListener(HoseListener)
    val filter = new FilterQuery()
    filter.language(Array("en"))
    filter.locations(Array(Array(-180, -90), Array(180, 90)))
    stream.filter(filter)
  }
}


object HoseListener extends StatusListener {

  def onStatus(status: Status): Unit = {
    songMatch(status.getText()).map {
      case SongMatch(title, lines) =>
        println("tune of " + title.toString() + ":\n" +
          splitToMatchLines(status.getText(), lines).take(140))
    }
  }

  def onStallWarning(warning: twitter4j.StallWarning): Unit = {
    println("stall warning: " + warning.getMessage())
    println(warning.getPercentFull() + "% full")
  }

  // fuck all these
  def onDeletionNotice(x$1: twitter4j.StatusDeletionNotice): Unit = {}
  def onScrubGeo(x$1: Long,x$2: Long): Unit = {}
  def onTrackLimitationNotice(x$1: Int): Unit = {}
  def onException(x$1: Exception): Unit = {}
}
