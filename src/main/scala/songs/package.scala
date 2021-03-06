package object songs {

  import nlp._


  case class Line(syllableCount: Int, finalRhymeSyllable: Syllable)

  type RhymeLookup = Map[Seq[Line], String]

  lazy val rhymeLookups: RhymeLookup = SongParser.loadSongs()

  case class SongMatch(title: String, lines: Seq[Line])

  /** Returns a matching song for this text, if one exists. */
  def songMatch(text: String): Option[SongMatch] = {
    val withProns = withPronunciations(englishTokenizer.tokenize(text))
    /* See if there are any words that we couldn't find pronunciations for.
     * If there are, we can't match this text to a song. */
    val wordsMatch = """[a-zA-Z]""".r.unanchored
    if (!withProns.find {
      case (token, None) => token match {
        case wordsMatch(_*) => true
        case _ => false
      }
      case _ => false
    }.isEmpty) return None

    val words = syllabify(englishTokenizer)(text).flatten
    if (words.length > 2) {
      (for { splitIndex <- 1 to words.length - 1 } yield {
        val lines = Seq(
          line(0, splitIndex, words),
          line(splitIndex, words.length, words))
        rhymeLookups.get(lines).map(SongMatch(_, lines))
      }).flatten.headOption
    }
    else
      None
  }

  def splitToMatchLines(text: String, lines: Seq[Line]): String = {
    val prons = mergeTokens(englishTokenizer.tokenize(text))
      .zip(syllabify(englishTokenizer)(text))

    var output = Seq[String]()
    var currentToken = 0
    var currentLineSyllables = 0
    (for (line <- lines) {
      while (currentLineSyllables < line.syllableCount) {
        prons.lift(currentToken).map { case (token, pron) =>
          output = appendToken(output, token)
          currentToken = currentToken + 1
          currentLineSyllables =
            currentLineSyllables + pron.map(_.length).getOrElse(0)
        }
      }
      output = appendToken(output, "\n")
      currentLineSyllables = 0
    })

    output.mkString(" ")
  }

  private def line(
    startIndex: Int,
    endIndex: Int,
    words: Seq[Pronunciation]):
      Line = {
    val lineWords = words.slice(startIndex, endIndex)
    Line(
      lineWords.map(_.length).sum,
      rhymeSyllable(lineWords.last.last))
  }
}
