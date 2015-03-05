package object nlp {

  type Phoneme = Symbol
  type Syllable = Seq[Phoneme]
  type Pronunciation = Seq[Syllable]
  type Word = String
  type PronunciationDictionary = Map[Word, Set[Pronunciation]]

  private def vowelPhonemes = Set('AA, 'AH, 'AW, 'EH, 'ER, 'EY, 'IH, 'OW , 'UH, 'AE, 'AO, 'AY, 'IY, 'OY, 'UW)

  /** A phoneme can be either a vowel sound or a consonant sound. */
  def isVowel(phoneme: Phoneme): Boolean =
    vowelPhonemes.contains(phoneme)

  def isRhyme(syll1: Syllable, syll2: Syllable): Boolean =
    syll2.endsWith(syll1.slice(syll1.indexWhere(isVowel), syll1.length))
}
