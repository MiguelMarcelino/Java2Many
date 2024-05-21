package transpilers.goTranspiler

import base.ASTParser
import org.eclipse.jdt.core.dom.*

class GoParser extends ASTParser {

  override def visit(node: AnnotationTypeDeclaration): String = { "" }

  override def visit(node: AnnotationTypeMemberDeclaration): String = { "" }

  override def visit(node: AnonymousClassDeclaration): String = { "" }

  override def visit(node: ArrayAccess): String = { "" }

  override def visit(node: ArrayCreation): String = { "" }

  override def visit(node: ArrayInitializer): String = { "" }

  override def visit(node: ArrayType): String = { "" }

  override def visit(node: AssertStatement): String = { "" }

  override def visit(node: Assignment): String = { "" }

  override def visit(node: Block): String = { "" }

  override def visit(node: BlockComment): String = {
    s"/* ${node.toString} */"
  }

  override def visit(node: BooleanLiteral): String = { "" }

  override def visit(node: BreakStatement): String = { "" }

  override def visit(node: CaseDefaultExpression): String = { "" }

  override def visit(node: CastExpression): String = { "" }

  override def visit(node: CatchClause): String = { "" }

  override def visit(node: CharacterLiteral): String = { "" }

  override def visit(node: ClassInstanceCreation): String = { "" }

  override def visit(node: CompilationUnit): String = {
    visit(node.getModule)
  }

  override def visit(node: ConditionalExpression): String = { "" }

  override def visit(node: ConstructorInvocation): String = { "" }

  override def visit(node: ContinueStatement): String = { "" }

  override def visit(node: CreationReference): String = { "" }

  override def visit(node: Dimension): String = { "" }

  override def visit(node: DoStatement): String = { "" }

  override def visit(node: EmptyStatement): String = { "" }

  override def visit(node: EnhancedForStatement): String = { "" }

  override def visit(node: EnumConstantDeclaration): String = { "" }

  override def visit(node: EnumDeclaration): String = { "" }

  override def visit(node: ExportsDirective): String = { "" }

  override def visit(node: ExpressionMethodReference): String = { "" }

  override def visit(node: ExpressionStatement): String = { "" }

  override def visit(node: FieldAccess): String = { "" }

  override def visit(node: FieldDeclaration): String = {
    node.getType.toString
  }

  override def visit(node: ForStatement): String = {
    val nodeBody = visit(node.getBody)
    s"""for () {
       |$nodeBody
       |}""".stripMargin
  }

  override def visit(node: GuardedPattern): String = { "" }

  override def visit(node: IfStatement): String = { "" }

  override def visit(node: ImportDeclaration): String = {
    s"""import \"${node.getName}\""""
  }

  override def visit(node: InfixExpression): String = { "" }

  override def visit(node: Initializer): String = { "" }

  override def visit(node: InstanceofExpression): String = { "" }

  override def visit(node: IntersectionType): String = { "" }

  override def visit(node: Javadoc): String = { "" }

  override def visit(node: JavaDocRegion): String = { "" }

  override def visit(node: JavaDocTextElement): String = { "" }

  override def visit(node: LabeledStatement): String = { "" }

  override def visit(node: LambdaExpression): String = { "" }

  override def visit(node: LineComment): String = {
    s"// ${node.toString}"
  }

  override def visit(node: MarkerAnnotation): String = { "" }

  override def visit(node: MemberRef): String = { "" }

  override def visit(node: MemberValuePair): String = { "" }

  override def visit(node: MethodRef): String = { "" }

  override def visit(node: MethodRefParameter): String = { "" }

  override def visit(node: MethodDeclaration): String = {
    val parameters = node.parameters()

    val isPrivate = node.modifiers().toArray().exists {
      case modifier: Modifier =>
        modifier.getKeyword == Modifier.ModifierKeyword.PRIVATE_KEYWORD
      case _ => false
    }

    val funcName = node.getName.getIdentifier

    val parsedFuncName = isPrivate match {
      case true  => funcName.capitalize
      case false => funcName
    }

    val nodeBody = visit(node.getBody)

    s"""func $parsedFuncName () {
       |  $nodeBody
       |}""".stripMargin
  }

  override def visit(node: MethodInvocation): String = { "" }

  override def visit(node: Modifier): String = { "" }

  override def visit(node: ModuleDeclaration): String = {
//    node
//      .moduleStatements()
//      .toArray()
//      .map {
//        case methodDeclaration: MethodDeclaration =>
//          visit(methodDeclaration)
//        case _ => ""
//      }
//      .mkString("\n")

    ""
  }

  override def visit(node: ModuleModifier): String = { "" }

  override def visit(node: NameQualifiedType): String = { "" }

  override def visit(node: NormalAnnotation): String = { "" }

  override def visit(node: NullLiteral): String = { "" }

  override def visit(node: NullPattern): String = { "" }

  override def visit(node: NumberLiteral): String = {
    s"${node.getToken}"
  }

  override def visit(node: OpensDirective): String = { "" }

  override def visit(node: PackageDeclaration): String = { "" }

  override def visit(node: ParameterizedType): String = { "" }

  override def visit(node: ParenthesizedExpression): String = { "" }

  override def visit(node: PatternInstanceofExpression): String = { "" }

  override def visit(node: PostfixExpression): String = { "" }

  override def visit(node: PrefixExpression): String = { "" }

  override def visit(node: ProvidesDirective): String = { "" }

  override def visit(node: PrimitiveType): String = { "" }

  override def visit(node: QualifiedName): String = { "" }

  override def visit(node: QualifiedType): String = { "" }

  override def visit(node: ModuleQualifiedName): String = { "" }

  override def visit(node: RequiresDirective): String = { "" }

  override def visit(node: RecordDeclaration): String = { "" }

  override def visit(node: RecordPattern): String = { "" }

  override def visit(node: ReturnStatement): String = { "" }

  override def visit(node: SimpleName): String = { "" }

  override def visit(node: SimpleType): String = { "" }

  override def visit(node: SingleMemberAnnotation): String = { "" }

  override def visit(node: SingleVariableDeclaration): String = { "" }

  override def visit(node: StringLiteral): String = { "" }

  override def visit(node: SuperConstructorInvocation): String = { "" }

  override def visit(node: SuperFieldAccess): String = { "" }

  override def visit(node: SuperMethodInvocation): String = { "" }

  override def visit(node: SuperMethodReference): String = { "" }

  override def visit(node: SwitchCase): String = { "" }

  override def visit(node: SwitchExpression): String = { "" }

  override def visit(node: SwitchStatement): String = { "" }

  override def visit(node: SynchronizedStatement): String = { "" }

  override def visit(node: TagElement): String = { "" }

  override def visit(node: TagProperty): String = { "" }

  override def visit(node: TextBlock): String = { "" }

  override def visit(node: TextElement): String = { "" }

  override def visit(node: ThisExpression): String = { "" }

  override def visit(node: ThrowStatement): String = { "" }

  override def visit(node: TryStatement): String = { "" }

  override def visit(node: TypeDeclaration): String = { "" }

  override def visit(node: TypeDeclarationStatement): String = { "" }

  override def visit(node: TypeLiteral): String = { "" }

  override def visit(node: TypeMethodReference): String = { "" }

  override def visit(node: TypeParameter): String = { "" }

  override def visit(node: TypePattern): String = { "" }

  override def visit(node: UnionType): String = { "" }

  override def visit(node: UsesDirective): String = { "" }

  override def visit(node: VariableDeclarationExpression): String = { "" }

  override def visit(node: VariableDeclarationStatement): String = { "" }

  override def visit(node: VariableDeclarationFragment): String = {
    s"${node.getName.getIdentifier}"
  }

  override def visit(node: WhileStatement): String = { "" }

  override def visit(node: WildcardType): String = { "" }

  override def visit(node: YieldStatement): String = { "" }

  override def visit(node: StringTemplateExpression): String = { "" }

  override def visit(node: StringFragment): String = { "" }

  override def visit(node: StringTemplateComponent): String = { "" }
}
