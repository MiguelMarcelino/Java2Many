package base

import org.eclipse.jdt.core.dom.{AST, ASTNode, ASTParser, CompilationUnit}
import org.eclipse.jface.text.Document

object AstHelpers {

  def getCompilationUnit(
      parser: ASTParser,
      document: Document
  ): CompilationUnit = {
    parser.setSource(document.get.toCharArray)
    parser.createAST(null).asInstanceOf[CompilationUnit]
  }

  def getAST(parser: ASTParser, document: Document): AST = {
    val compilationUnit = getCompilationUnit(parser, document)
    compilationUnit.getAST
  }

  def getRoot(parser: ASTParser, document: Document): ASTNode = {
    val compilationUnit = getCompilationUnit(parser, document)
    compilationUnit.getRoot
  }

  def printAST(parser: ASTParser, document: Document): String = {
    val compilationUnit = getCompilationUnit(parser, document)
    val printer = ASTPrinter()

    compilationUnit.accept(printer)
    printer.buffer.toString
  }

}
