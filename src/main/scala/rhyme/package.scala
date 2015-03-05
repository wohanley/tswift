package object rhyme {

  case class Line(syllableCount: Int, finalSyllable: String)
  case class Song(name: String, lines: Seq[Line])

  def isSongMatch(tweet: String, song: Song): Boolean = false
}
