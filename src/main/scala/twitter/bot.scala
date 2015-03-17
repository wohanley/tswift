package twitter

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

    println(Properties.envOrElse("API_KEY", ""))

    stream.addListener(HoseListener)
    stream.sample()
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