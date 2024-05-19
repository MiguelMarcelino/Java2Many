package transformers

import org.eclipse.jdt.core.dom.rewrite.{ASTRewrite, ListRewrite}
import org.eclipse.jdt.core.dom.{AST, CompilationUnit}

case class RewriteObjects(
    rewriter: ASTRewrite,
    listRewrite: ListRewrite
)
