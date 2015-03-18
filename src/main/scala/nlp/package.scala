package object nlp {

  import java.io.FileInputStream
  import opennlp.tools.tokenize.Tokenizer
  import opennlp.tools.tokenize.TokenizerME
  import opennlp.tools.tokenize.TokenizerModel


  lazy val englishTokenizer =
    new TokenizerME(
      new TokenizerModel(
        new FileInputStream("en-token.bin")))


  type Phoneme = Symbol
  type Syllable = Seq[Phoneme]
  type Pronunciation = Seq[Syllable]
  type Word = String
  type PronunciationDictionary = Map[Word, Set[Pronunciation]]

  private def vowelPhonemes = Set('AA, 'AH, 'AW, 'EH, 'ER, 'EY, 'IH, 'OW , 'UH, 'AE, 'AO, 'AY, 'IY, 'OY, 'UW)

  /** A phoneme can be either a vowel sound or a consonant sound. */
  def isVowel(phoneme: Phoneme): Boolean =
    vowelPhonemes.contains(phoneme)

  /** Two syllables rhyme if they are the same from the first vowel phoneme
    * onward. */
  def isRhyme(syll1: Syllable, syll2: Syllable): Boolean =
    syll2.endsWith(rhymeSyllable(syll1))

  def rhymeSyllable(syllable: Syllable): Syllable =
    syllable.slice(syllable.indexWhere(isVowel), syllable.length)

  def appendToken(tokens: Seq[String], token: String): Seq[String] =
    if (token.startsWith("'")) {
        tokens.lastOption match {
          case Some(previousToken) =>
            tokens.dropRight(1) :+ (previousToken + token)
          case None => Seq(token)
        }
      } else {
        tokens :+ token
      }

  def unsplitContractions(tokens: Seq[String]): Seq[String] =
    tokens.foldLeft(Seq[String]())(appendToken)

  def syllabify(tokenizer: Tokenizer)(text: String):
      Seq[Option[Pronunciation]] =
    unsplitContractions(tokenizer.tokenize(text))
      .map { token =>
        cmudict.pronunciations.getOrElse(token.toUpperCase(), Set())
      }.map(_.headOption)
}
