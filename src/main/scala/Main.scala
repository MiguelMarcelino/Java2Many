import base.helpers.AstHelpers
import org.eclipse.jdt.core.dom.{AST, ASTParser}
import org.eclipse.jface.text.Document
import transpilers.goTranspiler.GoParser

import java.io.File

object Main {

  final def main(args: Array[String]) = {
    println("Welcome to Scala2Many, a Scala to many transpiler (obviously...)")

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

  def transpile(document: Document): String = {
    val transformers = List(
      new ASTTransformerImpl(document)
    )

    transformers.foreach(_.transform())

    // TODO: Move parser and JavaVersion to a separate class
    val parser = ASTParser.newParser(AST.JLS9)
    val astNode = AstHelpers.getRoot(parser, document)

    val ast = AstHelpers.getCompilationUnit(parser, document)

    // Helper code to print AST
    // println(AstHelpers.printAST(parser, document))
    println("--------------")

    // TODO: Parse the code using the parser that is linked to a specific command line argument (allow multiple languages)
    val languageParser = new GoParser
    languageParser.visit(astNode)
  }

  /** Testing code
    * @return The result of the transpilation
    */
  private def testTranspile(): Unit = {
    // TODO: This is just test code. It should be removed when we start parsing code from files.
    val document = Document(
      "package transpiled;" +
        "import java.util.List;" + "\n" +
        " class X(String animal) {" + "\n" +
        "  public void deleteme() { }" + "\n" +
        "}".stripMargin
    )

    val transpiledCode = transpile(document)

    println(transpiledCode)
  }

}
