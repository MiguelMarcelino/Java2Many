package base

case class TranspilerOptions(
    transformers: Seq[ASTTransformer],
    optimizers: Seq[ASTTransformer],
    parser: ASTParser,
    formatter: Option[Formatter],
    extension: String
)
