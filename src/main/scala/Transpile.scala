import org.eclipse.jdt.core.dom.*

/** Transpiles the Java AST representation into a string representation in Scala
  */
class Transpile extends ASTVisitor {

  def visit(node: MethodInvocation): Boolean = {
    true
  }

}
