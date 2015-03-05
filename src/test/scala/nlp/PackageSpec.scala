package tests.nlp

import org.specs2.mutable._
import nlp._

class PackageSpec extends Specification {

  "isVowel" should {
    "return true for vowel phonemes" in {
      !(Set('AH, 'EY, 'ER, 'IY)
        .map(phoneme => isVowel(phoneme))
        .contains(false))
    }
    "return false for consonant phonemes" in {
      !(Set('B, 'D, 'L, 'N, 'TH)
        .map(phoneme => isVowel(phoneme))
        .contains(true))
    }
    "return false for unknown phonemes" in {
      !isVowel('KITTIES)
    }
  }

  "isRhyme" should {
    "return true when syllables rhyme" in {
      false 
    }

    "return false when syllables don't rhyme" in {
      false
    }
  }
}
