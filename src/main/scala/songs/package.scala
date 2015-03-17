package object songs {

  import nlp._


  case class Line(syllableCount: Int, finalRhymeSyllable: Syllable)

  type RhymeLookup = Map[Seq[Line], String]

  lazy val rhymeLookups: RhymeLookup = SongParser.loadSongs()

  case class SongMatch(title: String, lines: Seq[Line])

  /** Returns a matching song for this text, if one exists. */
  def songMatch(text: String): Option[SongMatch] = {
    val words = syllabify(englishTokenizer)(text).flatten.toSeq
    if (words.length > 2) {
      (for {
        endIndex1 <- 0 to words.length - 2
        endIndex2 <- endIndex1 + 1 to words.length - 1
      } yield {
        val lines = Seq(
          line(0, endIndex1, words),
          line(endIndex1 + 1, endIndex2, words))
        println(endIndex1 + " and " + endIndex2 + ": " + lines)
        rhymeLookups.get(lines).map(SongMatch(_, lines))
      }).flatten.headOption
    }
    else
      None
  }

  def splitToMatchLines(text: String, lines: Seq[Line]): String = {
    val prons = englishTokenizer.tokenize(text)
      .zip(syllabify(englishTokenizer)(text))

    var output = ""
    var currentToken = 0
    var currentLineSyllables = 0
    (for (line <- lines) {
      while (currentLineSyllables < line.syllableCount) {
        prons.lift(currentToken).map { case (token, pron) =>
          output = output + token + " "
          currentToken = currentToken + 1
          currentLineSyllables =
            currentLineSyllables + pron.map(_.length).getOrElse(0)
        }
      }
      output = output + '\n'
      currentLineSyllables = 0
    })

    output
  }

  private def line(
    startIndex: Int,
    endIndex: Int,
    words: Seq[Pronunciation]):
      Line = {
    val lineWords = words.slice(startIndex, endIndex + 1)
    //println(words + " from " + startIndex + " to " + endIndex + ":\n" + lineWords)
    Line(
      lineWords.map(_.length).sum,
      rhymeSyllable(lineWords.last.last))
  }

//  private def lastWhere[T](predicate: T => Boolean): Option[T] = {
//    
//  }
}
