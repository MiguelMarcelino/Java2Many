package transformers

import org.eclipse.jdt.core.dom.*
import org.eclipse.jdt.internal.compiler.lookup.{BlockScope, ClassScope}
import org.eclipse.jface.text.Document

/** Applies transformations to the Java AST
  */
class ASTTransformerImpl(document: Document) extends ASTTransformer(document) {

  override def visit(node: SwitchStatement): Boolean = {
    println("Hello")

    true
  }

  override def visit(node: MethodDeclaration): Boolean = {
    // Example code to add new expression to method invocations
    val ast = getAST()

    // Add a new method invocation: System.out.println("Hello, World")
    val methodInvocation = ast.newMethodInvocation
    val qName = ast.newQualifiedName(
      ast.newSimpleName("System"),
      ast.newSimpleName("out")
    )
    methodInvocation.setExpression(qName)
    methodInvocation.setName(ast.newSimpleName("println"))

    appendElement(methodInvocation)

    true
  }

}
