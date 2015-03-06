package nlp

package object cmudict {

  import java.io.File
  import java.util.Scanner
  import nlp.PronunciationDictionary
  import scala.collection.immutable.HashMap
  import scala.util.parsing.combinator._


  lazy val pronunciations: PronunciationDictionary = {
    val parser = PronunciationDictionaryParser // typing convenience
    var dictionary = new HashMap[Word, Set[Pronunciation]]

    val scanner = new Scanner(new File("cmudict"))
    while (scanner.hasNextLine) {
      val line = scanner.nextLine
      if (!line.startsWith("##")) {
        parser.parse(parser.entry, line) match {
          case parser.Success((word, pronunciation), _) =>
            dictionary.get(word) match {
              case Some(prons) =>
                dictionary = dictionary + (word -> (prons + pronunciation))
              case None =>
                dictionary = dictionary + (word -> Set(pronunciation))
            }
          case parser.Error(message, _) => println(message)
          case parser.Failure(message, _) => println (message)
        }
      }
    }

    dictionary
  }
}
