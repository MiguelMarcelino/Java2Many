import org.eclipse.jdt.core.dom.*

/** Applies transformations to the Java AST
  */
class ASTTransformer extends ASTNodeVisitor {

  override def visitSwitchCase(node: SwitchCase): SwitchCase = {
    // Nothing yet
  }

}
