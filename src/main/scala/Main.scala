import org.eclipse.jdt.core.dom.{AST, ASTNode, ASTParser}

object Main extends App {

  def main(args: Array[String]) {
    println("Welcome to Java2Scala, a Java to Scala transpiler (obviously...)")

    val usage = """
      Usage: java2scala [--file file] [--projectDir dir]
    """

    if (args.length == 0) println(usage)

    def parseArgs(
        map: Map[String, Any],
        list: List[String]
    ): Map[String, Any] = {
      list match {
        case Nil => map
        case "--file" :: value :: tail =>
          nextArg(map ++ Map("file" -> value), tail)
        case "--dir" :: value :: tail =>
          nextArg(map ++ Map("arg2" -> value), tail)
        case string :: Nil =>
          nextArg(map ++ Map("filename" -> string), list.tail)
        case unknown :: _ =>
          println("Unknown option " + unknown)
          exit(1)
      }
    }

    val options = nextArg(Map(), args.toList)
    println(options)
  }

  /** Transpiles a single file from Java to Scala
    *
    * @param fileLocation - the location of the file to transpile
    */
  def transpileFile(fileLocation: File): Option[Result] = {
    Option.empty[Result]
  }

  /** Transpiles a project directory from Java to Scala
    *
    * @param dirLocation - The location of the projectDirectory to transpile
    */
  def transpileDir(projectDirLocation: File): Option[Result] = {
    Option.empty[Result]
  }

  /** Testing code
    * @return The result of the transpilation
    */
  def transpile(): Option[Result] = {
    // TODO: This is just sample code
    val document = new Nothing(
      "import java.util.List;\n\nclass X\n{\n\n\tpublic void deleteme()\n\t{\n\t}\n\n}\n"
    )

    // Create the ASTParser
    val parser = ASTParser.newParser(AST.getJLSLatest())
    // Set the source to be the document
    parser.setSource(document.get.toCharArray)

    val cu = parser.createAST(null)

    val astTransformer = new ASTTransformer()
    val astTranspiler = new ASTTranspiler()

    val transformedAst = astTransformer.visitNode(cu)
    val transpiledAst = astTranspiler.visitNode(transformedAst)

    println(transpiledAst)
  }

}
