import exceptions.InvalidTypeException
import org.eclipse.jdt.core.dom.{ASTNode, SwitchCase, BinOp}

import java.lang.reflect.InvocationTargetException

trait AstPrinter {

  private def visitNode(astNode: ASTNode): String = {
    astNode match {
      case SwitchCase =>
        visitSwitchCase(astNode)
        for (child <- astNode.asInstanceOf[SwitchCase].expressions()) {
          visitNode(child)
        }
      case _ =>
        throw InvalidTypeException(astNode.getClass.getName())
    }
  }

  def visitSwitchCase(node: SwitchCase): String = {
    return ""
  }

}
