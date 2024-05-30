package base

import base.helpers.AstHelpers
import org.eclipse.jdt.core.dom.{AST, ASTParser}
import org.eclipse.jface.text.Document

trait Transpiler(document: Document) {

  val transpilerOptions: TranspilerOptions

  // TODO: Read from conf
  val parser = ASTParser.newParser(AST.JLS9)

  def transpile(): String = {
    // Apply language specific transformers
    val transformers = transpilerOptions.transformers
    transformers.foreach(_.transform())

    // Apply language specific optimizations
    val optimizers = transpilerOptions.optimizers
    optimizers.foreach(_.transform())

    // Helper code to print AST
    // println(AstHelpers.printAST(parser, document))
    // println("--------------")

    // Parse the AST with a language-specific parser
    val astNode = AstHelpers.getRoot(parser, document)
    val languageParser = transpilerOptions.parser
    languageParser.visit(astNode)
  }
}
