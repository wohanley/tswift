package songs

import scala.util.matching.Regex


object SongParser {

  def title: Regex = """(?m)^title:\s*(.+)$""".r

  def chorus: Regex = """(?s)(?i)\n\n\[chorus:?\]\s*\n+(.+)\n\n""".r

  def verse: Regex = chunkSurround("""(?s)(?i)(\[[^(chorus:?)]\]\s*\n+)?(.+)""").r

  private def chunkDelimiter: String = """\n\n+"""

  private def chunkSurround(middle: String): String =
    chunkDelimiter + middle + chunkDelimiter
}
