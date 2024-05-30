package transpilers.goTranspiler

import base.ASTTransformer
import base.helpers.AstHelpers
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite
import org.eclipse.jdt.core.dom.{
  ASTNode,
  MethodDeclaration,
  SingleVariableDeclaration,
  TypeDeclaration
}
import org.eclipse.jface.text.Document

import scala.collection.mutable.ListBuffer

class GoClassRewriter(document: Document) extends ASTTransformer(document) {

  val functionsToChange = ListBuffer[String]()

  /** Rewrites method declarations to allow for dispatching based on the classes type.
    *
    * @param node The method declaration node.
    * @return true to visit the children of this node, false to skip the children.
    */
  override def visit(node: MethodDeclaration): Boolean = {
    // Add a new argument to each method to allow dispatching on the class.
    val parent = node.getParent
    val newNode = parent match {
      case t: TypeDeclaration =>
        val className = t.getName.getIdentifier
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

  // TODO: We also have to rewrite method calls to add a new class argument.
}
