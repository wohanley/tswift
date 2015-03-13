scalaVersion := "2.11.5"

// specs2
libraryDependencies += "org.specs2" %% "specs2-core" % "3.0" % "test"
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
scalacOptions in Test += "-Yrangepos"

// parser combinators
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3"

libraryDependencies += "org.apache.opennlp" % "opennlp-tools" % "1.5.3"

libraryDependencies +=  "org.twitter4j" % "twitter4j-stream" % "3.0.5"
