import org.eclipse.jdt.core.dom.*

/** Applies transformations to the Java AST
  */
class ASTTransformer extends ASTVisitor {

  override def visit(node: SwitchCase): Boolean = {
    println("Hello")
    super.visit(node)
  }

  override def visit(node: MethodDeclaration): Boolean = {
    // Example code to add new expression to method invocations
    val ast = node.getAST
    val methodInvocation = ast.newMethodInvocation

    // Add a new method invocation: System.out.println("Hello, World")
    val qName = ast.newQualifiedName(
      ast.newSimpleName("System"),
      ast.newSimpleName("out")
    )
    methodInvocation.setExpression(qName)
    methodInvocation.setName(ast.newSimpleName("println"))
    val literal = ast.newStringLiteral
    literal.setLiteralValue("Hello, World")
    methodInvocation.arguments.add(literal)

    // Append the statement
    node.getBody.statements.add(ast.newExpressionStatement(methodInvocation))
    super.visit(node)
  }

}
