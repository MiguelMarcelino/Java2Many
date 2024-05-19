import org.eclipse.jface.text.Document
import transformers.ASTTransformerImpl

import java.io.File

object Main {

  final def main(args: Array[String]) = {
    println("Welcome to Scala2Go, a Scala to Go transpiler (obviously...)")

    val usage = """
      Usage: java2scala [--file file] [--projectDir dir]
    """

//    if (args.length == 0) println(usage)
//
//    val options = parseArgs(Map(), args.toList)
//    println(options)

    testTranspile()
  }

  private def parseArgs(
      map: Map[String, Any],
      list: List[String]
  ): Map[String, Any] = {
    list match {
      case Nil => map
      case "--file" :: value :: tail =>
        parseArgs(map ++ Map("file" -> value), tail)
      case "--dir" :: value :: tail =>
        parseArgs(map ++ Map("dir" -> value), tail)
      case string :: Nil =>
        parseArgs(map ++ Map("filename" -> string), list.tail)
      case unknown :: _ =>
        throw new IllegalArgumentException(
          s"Failed to parse arguments. Unknown option $unknown"
        )
    }
  }

  /** Transpiles a single file from Java to Scala
    *
    * @param fileLocation - the location of the file to transpile
    */
  def transpileFile(fileLocation: File): Option[Result.Value] = {
    Option.empty[Result.Value]
  }

  /** Transpiles a project directory from Java to Scala
    *
    * @param dirLocation - The location of the projectDirectory to transpile
    */
  def transpileDir(projectDirLocation: File): Option[Result.Value] = {
    Option.empty[Result.Value]
  }

  /** Testing code
    * @return The result of the transpilation
    */
  def testTranspile(): Unit = {
    // TODO: This is just test code
    val document = Document(
      "import java.util.List;\n\nclass X\n{\n\n\tpublic void deleteme()\n\t{\n\t}\n\n}\n"
    )
    val astTransformer = new ASTTransformerImpl(document)

    println(document.get)

//    assert(
//      "import java.util.List;\n\nclass X\n{\n\n\tpublic void deleteme()\n\t{\n\t}\n\n}" == document.get
//    )

  }

}
