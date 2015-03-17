package songs

import java.io.File
import java.util.Scanner
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import scala.util.parsing.combinator.RegexParsers
import scala.collection.immutable.HashMap
import scala.util.matching.Regex


object SongParser {

  def title: Regex = """(?m)(?i)title: (.+)""".r

  def chorusLabel: Regex = """(?m)(?i)\[chorus:?\]""".r

  def verseLabel: Regex = """(?m)(?i)\[.*\]""".r

  def loadSongs(): Map[Seq[Line], String] = {

    var rhymes: RhymeLookup = new HashMap[Seq[Line], String]

    val scanner = new Scanner(new File("scrape/lyrics"))
    scanner.useDelimiter("%%%")

    while (scanner.hasNext()) {
      val song = scanner.next()
      title.findFirstIn(song) match {
        case Some(songTitle) => {
          val startInfo: SongParts = new HashMap[Symbol, Seq[String]]
          val populatedInfo = song.split("""\n(\s*\n)+""")
            .foldLeft(startInfo)((info, chunk) => parseChunk(info, chunk))
          rhymes = addSegmentFromSongParts(rhymes, 'verse, populatedInfo,
            songTitle)
          rhymes = addSegmentFromSongParts(rhymes, 'chorus, populatedInfo,
            songTitle)
        }
        case _ => {}
      }
    }

    rhymes
  }

  private def addSegmentFromSongParts(
    rhymes: RhymeLookup,
    key: Symbol,
    songParts: SongParts,
    title: String): RhymeLookup =
    Try(
      songParts.get(key) match {
        case Some(segment) =>
          addSegment(title, rhymes, segment)
        case None => rhymes
      }) match {
      case Success(newRhymes) => newRhymes
      case Failure(_) => rhymes
    }

  type SongParts = Map[Symbol, Seq[String]]

  private def parseChunk(songInfo: SongParts, chunk: String):
      SongParts = {

    if (!title.findFirstIn(chunk).isEmpty) {
      songInfo
    } else {
      chunk match {
        case chorusLabel.unanchored() => {
          addIfNotExists(songInfo, ('chorus -> chunk.split('\n').drop(1)))
        }
        case verseLabel.unanchored() => {
          addIfNotExists(songInfo, ('verse -> chunk.split('\n').drop(1)))
        }
        case _ => {
          addIfNotExists(songInfo, ('verse -> chunk.split('\n')))
        }
      }
    }
  }

  private def addIfNotExists[K, V](map: Map[K, V], mapping: Tuple2[K, V]):
      Map[K, V] = {
    if (!map.contains(mapping._1))
      map + mapping
    else
      map
  }

  private def addSegment(
    title:String,
    rhymes: RhymeLookup,
    lines: Seq[String]): RhymeLookup = {

    rhymes + (lines.take(2)
      .map(nlp.syllabify(nlp.englishTokenizer)_)
      .map(prons => {
        Line(
          prons.flatten.flatten.length,
          nlp.rhymeSyllable(prons.flatten.last.last))
      })
      -> title)
  }
}
