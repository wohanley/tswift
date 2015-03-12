package object songs {

  import nlp._


  case class Line(syllableCount: Int, finalRhymeSyllable: Syllable)

  type RhymeLookup = Map[Seq[Line], String]

  lazy val rhymeLookups: RhymeLookup = SongParser.loadSongs()

  /** Returns the title of a song that matches this text, if one exists. */
  def songMatch(text: String): Option[String] = {
    val words = syllabify(englishTokenizer)(text).toSeq
    println(words)
    if (words.length > 2) {
      (for {
        endIndex1 <- 0 to words.length - 2
        endIndex2 <- endIndex1 + 1 to words.length - 1
      } yield {
        println(endIndex1 + " and " + endIndex2)
        rhymeLookups.get(Seq(
          line(0, endIndex1, words),
          line(endIndex1 + 1, endIndex2, words)))
      }).find(!_.isEmpty).map(_.get)
    }
    else
      None
  }

  private def line(
    startIndex: Int,
    endIndex: Int,
    words: Seq[Pronunciation]):
      Line = {
    val lineWords = words.slice(startIndex, endIndex + 1)
    Line(
      lineWords.map(_.length).sum,
      rhymeSyllable(lineWords.last.last))
  }
}
