package base

import org.eclipse.jdt.core.dom.rewrite.{ASTRewrite, ListRewrite}

case class RewriteObjects(
    rewriter: ASTRewrite,
    listRewrite: ListRewrite
)
