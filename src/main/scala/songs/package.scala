package object songs {

  import nlp._


  case class Line(syllableCount: Int, finalSyllable: Syllable)

  type RhymeLookup = Map[Seq[Line], String]

  lazy val rhymeLookups: RhymeLookup = SongParser.loadSongs()
}
