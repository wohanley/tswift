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
          val verse = SongParser.verse.findFirstIn(song)
          val chorus = SongParser.chorus.findFirstIn(song)

          rhymes = addSegment(title, rhymes, verse)
          rhymes = addSegment(title, rhymes, chorus)
        }
        // if the title didn't parse, there's no point adding anything
        case None => {}
      }
    }

    rhymes
  }

  private def addSegment(title:String,
    rhymes: RhymeLookup,
    segment: Option[String]): RhymeLookup = {
    println(segment)
    segment match {
      case Some(seg) => {
        seg.lines.take(2).map(println)
        rhymes + (seg.lines.take(2)
          .map(syllabify(nlp.englishTokenizer)_)
          .map(prons => Line(prons.flatten.length, prons.last.last))
          .toSeq
          -> title)
      }
      // ignore failures, no big deal
      case None => rhymes
    }
  }
}
