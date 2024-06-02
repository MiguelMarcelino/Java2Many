package transpilers.goTranspiler

import base.ASTTransformer
import org.eclipse.jdt.core.dom.*
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite
import org.eclipse.jface.text.Document

class GoClassRewriter(document: Document) extends ASTTransformer(document) {

  /** Rewrites method declarations to allow for dispatching based on the classes type.
    *
    * @param node The method declaration node.
    * @return true to visit the children of this node, false to skip the children.
    */
  override def visit(node: MethodDeclaration): Boolean = {
    // Add a new argument to each method to allow dispatching on the class.
    node.getParent match {
      case typeDeclaration: TypeDeclaration if !node.isConstructor =>
        // If the node is not a constructor, we need to add the class name as a parameter
        val ast = node.getAST
        val rewriter = ASTRewrite.create(ast)

        val className = typeDeclaration.getName.getIdentifier

        // The parameter name is the class name in lowercase
        val parameterName = className.toLowerCase()
        val variableDeclaration = ast.newSingleVariableDeclaration()
        variableDeclaration.setName(
          ast.newSimpleName(s"$parameterName")
        )
        variableDeclaration.setType(
          ast.newSimpleType(ast.newSimpleName(className))
        )

        // Add the new parameters to the method declaration.
        val listRewrite =
          rewriter.getListRewrite(node, MethodDeclaration.PARAMETERS_PROPERTY)
        listRewrite.insertFirst(variableDeclaration, null)

        // Apply the changes to the document.
        val edit = rewriter.rewriteAST(document, null)
        applyTransformation(edit)

      case _ => // Do nothing if it's not a TypeDeclaration
    }

    true
  }

  override def visit(node: MethodInvocation): Boolean = {
    // There are two types of invocations
    // 1. Invocations within the current class
    //    - We must use the class instance provided in the method arguments to call the method
    //    to the method invocation
    // 2. Invocations from other classes
    //    - GoParser takes care of these cases, by adding the expression as the first argument to the method invocation
    node.getParent match {
      case parent: TypeDeclaration =>
        // 1. Invocations within the current class
        val parentClassName = parent.getName.getIdentifier

        val ast = node.getAST
        val rewriter = ASTRewrite.create(ast)

        // For the variable name, we always use the parent class's name in lowercase
        // See the visit method for MethodDeclaration for more information
        val variableName = parentClassName.toLowerCase()

        // Create a new variable that creates the type of the class
        val newArg = ast.newSimpleName(s"$variableName")

        // Use ListRewrite to add the new argument to the method invocation
        val listRewrite =
          rewriter.getListRewrite(node, MethodInvocation.ARGUMENTS_PROPERTY)
        listRewrite.insertFirst(newArg, null)

        // Apply the changes to the document
        val edit = rewriter.rewriteAST(document, null)
        applyTransformation(edit)
      case _ => // Do nothing if it's not a TypeDeclaration
    }

    true
  }
}
