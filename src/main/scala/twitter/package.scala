package object twitter {

  import scala.util.Failure
  import scala.util.Success
  import scala.util.Try
  import util.Properties
  import twitter4j._

  def tweet(update: StatusUpdate) {

    val twitterConfig = new twitter4j.conf.ConfigurationBuilder()
      .setOAuthConsumerKey(Properties.envOrElse("API_KEY", ""))
      .setOAuthConsumerSecret(Properties.envOrElse("API_SECRET", ""))
      .setOAuthAccessToken(Properties.envOrElse("ACCESS_TOKEN", ""))
      .setOAuthAccessTokenSecret(Properties.envOrElse("ACCESS_TOKEN_SECRET", ""))
      .build()

    val twitter = new TwitterFactory(twitterConfig).getInstance()
    Try(twitter.updateStatus(update)) match {
      case Success(_) => Unit
      case Failure(error) => {
        println("Failed to tweet: " + update)
        println("Tried with config: " + twitterConfig)
        println("Error reported: " + error)
      }
    }
  }

  def tweetUrl(status: Status): String =
    "https://twitter.com/" + status.getUser().getScreenName() + "/status/" +
      status.getId()
}
