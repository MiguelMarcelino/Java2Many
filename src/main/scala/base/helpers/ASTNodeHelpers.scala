package base.helpers

import org.eclipse.jdt.core.dom.ASTNode

import scala.reflect.ClassTag

object ASTNodeHelpers {

  /** Find the first parent node of a given type. The ClassTag is used to handle type erasure at runtime,
    * allowing us to check the type of parentType.
    *
    * @param node The node to start the search from.
    * @param parentType The type of the parent node to find.
    * @tparam T The type of the parent node to find.
    * @return The first parent node of the given type, if it exists.
    */
  def findParentNode[T <: ASTNode: ClassTag](
      node: ASTNode,
      parentType: Class[T]
  ): Option[T] = {
    node.getParent match {
      case p: T if parentType.isInstance(p) =>
        Some(p)
      case p if p != null =>
        findParentNode(p, parentType)
      case _ =>
        None
    }
  }
}
