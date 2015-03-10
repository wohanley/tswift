package object songs {

  import java.io.File
  import java.util.Scanner
  import nlp._
  import scala.collection.immutable.HashMap
  import scala.util.parsing.combinator.RegexParsers


  case class Line(syllableCount: Int, finalSyllable: Syllable)

  type RhymeLookup = Map[Seq[Line], String]

  lazy val rhymeLookups: Map[Seq[Line], String] = {

    var rhymes: RhymeLookup = new HashMap[Seq[Line], String]

    val scanner = new Scanner(new File("scrape/lyrics"))
    scanner.useDelimiter("%%%")

    while (scanner.hasNext()) {
      val song = scanner.next()
      val title = SongParser.title.findFirstIn(song)

      println(title)

      title match {
        case Some(title) => {
          SongParser.verse.findFirstMatchIn(song).map(verse => {
            rhymes = addSegment(title, rhymes, verse.group(2))
          })
          SongParser.chorus.findFirstMatchIn(song).map(chorus =>
            rhymes = addSegment(title, rhymes, chorus.group(1)))
        }
        // if the title didn't parse, there's no point adding anything
        case None => {}
      }
    }

    rhymes
  }

  private def addSegment(
    title:String,
    rhymes: RhymeLookup,
    segment: String): RhymeLookup = {
    println(segment)
    segment.lines.take(2).map(println)
    rhymes + (segment.lines.take(2)
      .map(syllabify(nlp.englishTokenizer)_)
      .map(prons => Line(prons.flatten.length, prons.last.last))
      .toSeq
      -> title)
  }
}
