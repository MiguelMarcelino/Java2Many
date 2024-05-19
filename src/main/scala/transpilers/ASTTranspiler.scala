package transpilers

import org.eclipse.jdt.core.dom.ASTVisitor

import scala.collection.mutable.ListBuffer

abstract class ASTTranspiler extends ASTVisitor {
  // TODO: Check how the AST is traversed to confirm if having a list is enough
  private def transpiledCode: ListBuffer[String] = ListBuffer.empty[String]

  /** Add the transpiled code to the list of converted code
    * @param code
    */
  def addTranspiledCode(code: String): Unit = {
    transpiledCode.append(code)
  }

}
