import org.eclipse.jdt.core.dom.rewrite.{ASTRewrite, ListRewrite}
import org.eclipse.jdt.core.dom.{AST, ASTNode, ASTParser, CompilationUnit}
import org.eclipse.jface.text.Document
import org.eclipse.text.edits.{TextEdit}

object Main extends App {

  def main(args: Array[String]) = {
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

    transpile()
  }

  /** Transpiles a single file from Java to Scala
    *
    * @param fileLocation - the location of the file to transpile
    */
  def transpileFile(fileLocation: File): Option[Result.Value] = {
    Option.empty[Result]
  }

  /** Transpiles a project directory from Java to Scala
    *
    * @param dirLocation - The location of the projectDirectory to transpile
    */
  def transpileDir(projectDirLocation: File): Option[Result.Value] = {
    Option.empty[Result]
  }

  /** Testing code
    * @return The result of the transpilation
    */
  def transpile(): Unit = {
    // TODO: This is just sample code
    val document = new Nothing(
      "import java.util.List;\n\nclass X\n{\n\n\tpublic void deleteme()\n\t{\n\t}\n\n}\n"
    )

    // Create the ASTParser
    val parser = ASTParser.newParser(AST.getJLSLatest())
    // Set the source to be the document
    parser.setSource(document.get.toCharArray)

    val node = parser.createAST(null)

    val ast = cu.getAST

    val astTransformer = new ASTTransformer()
    val astTranspiler = new ASTTranspiler()

    val transformedAst = astTransformer.visit(node)
    val transpiledAst = astTranspiler.visit(transformedAst)

    println(transpiledAst)
  }

  def rewriteAST(): Unit = {
    // New example

    // Prepare document (contains the code)
    val document = new Document("import java.util.List;\nclass X {}\n")
    val parser = ASTParser.newParser(AST.JLS8)
    parser.setSource(document.get.toCharArray)

    // Create the AST
    val cu = parser.createAST(null).asInstanceOf[CompilationUnit]
    val ast = cu.getAST
    val id = ast.newImportDeclaration
    id.setName(ast.newName(Array[String]("java", "util", "Set")))

    // Create new rewriter
    val rewriter = ASTRewrite.create(ast)
    val lrw = rewriter.getListRewrite(cu, CompilationUnit.IMPORTS_PROPERTY)

    // Insert into AST
    lrw.insertLast(id, null)
    val edits = rewriter.rewriteAST(document, null)
    edits.apply(document)

    // Result assertions
    assert(
      "import java.util.List;\nimport java.util.Set;\nclass X {}\n" == document.get
    )
  }

}
