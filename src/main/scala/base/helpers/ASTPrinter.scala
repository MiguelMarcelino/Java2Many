package base.helpers

import org.eclipse.jdt.core.dom.{ASTNode, ASTVisitor}

/** Print the AST
  *
  * Code courtesy of eclipse wiki: https://wiki.eclipse.org/FAQ_How_do_I_create_and_examine_an_AST%3F
  */
class ASTPrinter extends ASTVisitor {
  val buffer = new StringBuffer

  override def preVisit(node: ASTNode): Unit = {
    // write the name of the node being visited
    printDepth(node)
    var name = node.getClass.getName
    name = name.substring(name.lastIndexOf('.') + 1)
    buffer.append(name)
    buffer.append(" {\r\n")
  }

  override def postVisit(node: ASTNode): Unit = {
    // write a closing brace to indicate end of the node
    printDepth(node)
    buffer.append("}\r\n")
  }

  private def printDepth(node: ASTNode): Unit = {
    // indent the current line to an appropriate depth
    var nodeDepth = node
    while (nodeDepth != null) {
      nodeDepth = nodeDepth.getParent
      buffer.append("  ")
    }
  }
}
