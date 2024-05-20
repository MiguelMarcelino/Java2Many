package helpers

import org.eclipse.jdt.core.dom.{AST, ASTNode, ASTParser, CompilationUnit}
import org.eclipse.jface.text.Document

object AstHelpers {

  def getAST(parser: ASTParser, document: Document): AST = {
    parser.setSource(document.get.toCharArray)
    val compilationUnit = parser.createAST(null).asInstanceOf[CompilationUnit]
    compilationUnit.getAST
  }

  def getRoot(parser: ASTParser, document: Document): ASTNode = {
    parser.setSource(document.get.toCharArray)
    val compilationUnit = parser.createAST(null).asInstanceOf[CompilationUnit]
    compilationUnit.getRoot
  }

}
