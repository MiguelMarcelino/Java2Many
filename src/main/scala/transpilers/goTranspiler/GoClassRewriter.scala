package transpilers.goTranspiler

import base.ASTTransformer
import org.eclipse.jdt.core.dom.*
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite
import org.eclipse.jface.text.Document

import scala.collection.mutable

class GoClassRewriter(document: Document) extends ASTTransformer(document) {

  // A map of all the methods that have been modified within each class.
  // Mapping: class name -> method name
  private val modifiedMethods = mutable.Map[String, String]()

  /** Rewrites method declarations to allow for dispatching based on the classes type.
    *
    * @param node The method declaration node.
    * @return true to visit the children of this node, false to skip the children.
    */
  override def visit(node: MethodDeclaration): Boolean = {
    // Add a new argument to each method to allow dispatching on the class.
    val parent = node.getParent
    val newNode = parent match {
      case typeDeclaration: TypeDeclaration =>
        val className = typeDeclaration.getName.getIdentifier
        val parameterList = node.parameters()

        val ast = node.getAST

        val variableDeclaration = ast.newSingleVariableDeclaration()
        variableDeclaration.setName(
          ast.newSimpleName(s"${className.head.toLower}")
        )
        variableDeclaration.setType(
          ast.newSimpleType(ast.newSimpleName(className))
        )

        addParameters(node, List(variableDeclaration))

        modifiedMethods.addOne(
          (typeDeclaration.getName.getIdentifier, node.getName.getIdentifier)
        )
    }

    true
  }

  override def visit(node: MethodInvocation): Boolean = {
    // There are two types of invocations
    // 1. Invocations within the current class
    // 2. Invocations from another scope

    // Check if the method invocation corresponds to a modified method
    if (modifiedMethods.contains(node.getName.getIdentifier)) {
      val ast = node.getAST
      val rewriter = ASTRewrite.create(ast)

      // Create the new argument
      val newArg = ast.newSimpleName("s")

      // Use ListRewrite to add the new argument to the method invocation
      val listRewrite =
        rewriter.getListRewrite(node, MethodInvocation.ARGUMENTS_PROPERTY)
      listRewrite.insertFirst(newArg, null)
    }

    true
  }

  private def addParameters(
      node: MethodDeclaration,
      parameters: List[SingleVariableDeclaration]
  ): Unit = {
    val ast = node.getAST
    val rewriter = ASTRewrite.create(ast)

    // Add the new parameters to the method declaration.
    val listRewrite =
      rewriter.getListRewrite(node, MethodDeclaration.PARAMETERS_PROPERTY)
    parameters.foreach(p => listRewrite.insertFirst(p, null))
    val edits = rewriter.rewriteAST(document, null)

    // Apply the changes to the document.
    applyTransformation(edits)
  }
}
