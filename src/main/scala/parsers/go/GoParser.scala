package parsers.go

import org.eclipse.jdt.core.dom.*
import parsers.ASTParser

class GoParser extends ASTParser {

  def visit(node: AnnotationTypeDeclaration): String = { "" }

  def visit(node: AnnotationTypeMemberDeclaration): String = { "" }

  def visit(node: AnonymousClassDeclaration): String = { "" }

  def visit(node: ArrayAccess): String = { "" }

  def visit(node: ArrayCreation): String = { "" }

  def visit(node: ArrayInitializer): String = { "" }

  def visit(node: ArrayType): String = { "" }

  def visit(node: AssertStatement): String = { "" }

  def visit(node: Assignment): String = { "" }

  def visit(node: Block): String = { "" }

  def visit(node: BlockComment): String = {
    s"/* ${node.toString} */"
  }

  def visit(node: BooleanLiteral): String = { "" }

  def visit(node: BreakStatement): String = { "" }

  def visit(node: CaseDefaultExpression): String = { "" }

  def visit(node: CastExpression): String = { "" }

  def visit(node: CatchClause): String = { "" }

  def visit(node: CharacterLiteral): String = { "" }

  def visit(node: ClassInstanceCreation): String = { "" }

  def visit(node: CompilationUnit): String = {
    visit(node.getModule)
  }

  def visit(node: ConditionalExpression): String = { "" }

  def visit(node: ConstructorInvocation): String = { "" }

  def visit(node: ContinueStatement): String = { "" }

  def visit(node: CreationReference): String = { "" }

  def visit(node: Dimension): String = { "" }

  def visit(node: DoStatement): String = { "" }

  def visit(node: EmptyStatement): String = { "" }

  def visit(node: EnhancedForStatement): String = { "" }

  def visit(node: EnumConstantDeclaration): String = { "" }

  def visit(node: EnumDeclaration): String = { "" }

  def visit(node: ExportsDirective): String = { "" }

  def visit(node: ExpressionMethodReference): String = { "" }

  def visit(node: ExpressionStatement): String = { "" }

  def visit(node: FieldAccess): String = { "" }

  def visit(node: FieldDeclaration): String = { "" }

  def visit(node: ForStatement): String = { "" }

  def visit(node: GuardedPattern): String = { "" }

  def visit(node: IfStatement): String = { "" }

  def visit(node: ImportDeclaration): String = { "" }

  def visit(node: InfixExpression): String = { "" }

  def visit(node: Initializer): String = { "" }

  def visit(node: InstanceofExpression): String = { "" }

  def visit(node: IntersectionType): String = { "" }

  /** Visits the given AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: Javadoc): String = { "" }

  /** Visits the given AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: JavaDocRegion): String = { "" }

  def visit(node: JavaDocTextElement): String = { "" }

  def visit(node: LabeledStatement): String = { "" }

  def visit(node: LambdaExpression): String = { "" }

  def visit(node: LineComment): String = {
    s"// ${node.toString}"
  }

  def visit(node: MarkerAnnotation): String = { "" }

  def visit(node: MemberRef): String = { "" }

  def visit(node: MemberValuePair): String = { "" }

  def visit(node: MethodRef): String = { "" }

  def visit(node: MethodRefParameter): String = { "" }

  def visit(node: MethodDeclaration): String = {
    val body = visit(node.getBody)
    val arguments = node.getJavadoc

    s"""func ${node.getName} () {
       |  $body
       |}""".stripMargin
  }

  def visit(node: MethodInvocation): String = { "" }

  def visit(node: Modifier): String = { "" }

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    *         4
    */
  def visit(node: ModuleDeclaration): String = { "" }

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    *         4
    */
  def visit(node: ModuleModifier): String = { "" }

  def visit(node: NameQualifiedType): String = { "" }

  def visit(node: NormalAnnotation): String = { "" }

  def visit(node: NullLiteral): String = { "" }

  def visit(node: NullPattern): String = { "" }

  def visit(node: NumberLiteral): String = { "" }

  def visit(node: OpensDirective): String = { "" }

  def visit(node: PackageDeclaration): String = { "" }

  def visit(node: ParameterizedType): String = { "" }

  def visit(node: ParenthesizedExpression): String = { "" }

  def visit(node: PatternInstanceofExpression): String = { "" }

  def visit(node: PostfixExpression): String = { "" }

  def visit(node: PrefixExpression): String = { "" }

  /** Visits the given type-specific AST node.
    * <p>
    * The default implementation does nothing and return true.
    * Subclasses may re-implement.
    * </p>
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ProvidesDirective): String = { "" }

  def visit(node: PrimitiveType): String = { "" }

  def visit(node: QualifiedName): String = { "" }

  def visit(node: QualifiedType): String = { "" }

  def visit(node: ModuleQualifiedName): String = { "" }

  def visit(node: RequiresDirective): String = { "" }

  def visit(node: RecordDeclaration): String = { "" }

  def visit(node: RecordPattern): String = { "" }

  def visit(node: ReturnStatement): String = { "" }

  def visit(node: SimpleName): String = { "" }

  def visit(node: SimpleType): String = { "" }

  def visit(node: SingleMemberAnnotation): String = { "" }

  def visit(node: SingleVariableDeclaration): String = { "" }

  def visit(node: StringLiteral): String = { "" }

  def visit(node: SuperConstructorInvocation): String = { "" }

  def visit(node: SuperFieldAccess): String = { "" }

  def visit(node: SuperMethodInvocation): String = { "" }

  def visit(node: SuperMethodReference): String = { "" }

  def visit(node: SwitchCase): String = { "" }

  def visit(node: SwitchExpression): String = { "" }

  def visit(node: SwitchStatement): String = { "" }

  def visit(node: SynchronizedStatement): String = { "" }

  def visit(node: TagElement): String = { "" }

  def visit(node: TagProperty): String = { "" }

  def visit(node: TextBlock): String = { "" }

  def visit(node: TextElement): String = { "" }

  def visit(node: ThisExpression): String = { "" }

  def visit(node: ThrowStatement): String = { "" }

  def visit(node: TryStatement): String = { "" }

  def visit(node: TypeDeclaration): String = { "" }

  def visit(node: TypeDeclarationStatement): String = { "" }

  def visit(node: TypeLiteral): String = { "" }

  def visit(node: TypeMethodReference): String = { "" }

  def visit(node: TypeParameter): String = { "" }

  def visit(node: TypePattern): String = { "" }

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    * @since 3.7.1
    */
  def visit(node: UnionType): String = { "" }

  def visit(node: UsesDirective): String = { "" }

  def visit(node: VariableDeclarationExpression): String = { "" }

  def visit(node: VariableDeclarationStatement): String = { "" }

  def visit(node: VariableDeclarationFragment): String = { "" }

  def visit(node: WhileStatement): String = { "" }

  def visit(node: WildcardType): String = { "" }

  def visit(node: YieldStatement): String = { "" }

  def visit(node: StringTemplateExpression): String = { "" }

  def visit(node: StringFragment): String = { "" }

  def visit(node: StringTemplateComponent): String = { "" }
}
