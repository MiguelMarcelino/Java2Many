package transpilers.goTranspiler

import base.{Transpiler, TranspilerOptions}
import org.eclipse.jface.text.Document

class GoTranspiler(document: Document) extends Transpiler(document) {

  override val transpilerOptions: TranspilerOptions = TranspilerOptions(
    transformers = Seq(GoClassRewriter(document)),
    optimizers = Seq(),
    parser = GoParser(),
    formatter = Option.empty, // Some(GoFormatter()),
    extension = "go"
  )
}
