package songs

import scala.util.matching.Regex


object SongParser {

  def title: Regex = """(?m)^title:\s*(.+)$""".r

  def chorus: Regex = """(?s)(?i)\n\n\[chorus:?\]\s*\n+(.+)\n\n""".r

  def verse: Regex = """(?s)(?i)\n\n+(\[[^(chorus:?)]\]\s*\n+)?(.+)\n\n+""".r
}
