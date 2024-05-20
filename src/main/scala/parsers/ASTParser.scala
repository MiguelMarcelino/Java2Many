package parsers

import org.eclipse.jdt.core.dom.*

import scala.collection.mutable.ListBuffer

trait ASTParser {

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: AnnotationTypeDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: AnnotationTypeMemberDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: AnonymousClassDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ArrayAccess): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ArrayCreation): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ArrayInitializer): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ArrayType): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: AssertStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: Assignment): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: Block): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: BlockComment): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: BooleanLiteral): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: BreakStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: CaseDefaultExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: CastExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: CatchClause): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: CharacterLiteral): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ClassInstanceCreation): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: CompilationUnit): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ConditionalExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ConstructorInvocation): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ContinueStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: CreationReference): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: Dimension): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: DoStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: EmptyStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: EnhancedForStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: EnumConstantDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: EnumDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ExportsDirective): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ExpressionMethodReference): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ExpressionStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: FieldAccess): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: FieldDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ForStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: GuardedPattern): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: IfStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ImportDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: InfixExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: Initializer): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: InstanceofExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: IntersectionType): String

  /** Visits the given AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: Javadoc): String

  /** Visits the given AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: JavaDocRegion): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: JavaDocTextElement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: LabeledStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: LambdaExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: LineComment): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: MarkerAnnotation): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: MemberRef): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: MemberValuePair): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: MethodRef): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: MethodRefParameter): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: MethodDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: MethodInvocation): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: Modifier): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    * 4
    */
  def visit(node: ModuleDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    * 4
    */
  def visit(node: ModuleModifier): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: NameQualifiedType): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: NormalAnnotation): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: NullLiteral): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: NullPattern): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: NumberLiteral): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: OpensDirective): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: PackageDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ParameterizedType): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ParenthesizedExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: PatternInstanceofExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: PostfixExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: PrefixExpression): String

  /** Visits the given type-specific AST node.
    * <p>
    * The default implementation does nothing and return true.
    * Subclasses may re-implement.
    * </p>
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ProvidesDirective): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: PrimitiveType): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: QualifiedName): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: QualifiedType): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ModuleQualifiedName): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: RequiresDirective): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: RecordDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: RecordPattern): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ReturnStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: SimpleName): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: SimpleType): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: SingleMemberAnnotation): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: SingleVariableDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: StringLiteral): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: SuperConstructorInvocation): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: SuperFieldAccess): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: SuperMethodInvocation): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: SuperMethodReference): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: SwitchCase): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: SwitchExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: SwitchStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: SynchronizedStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: TagElement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: TagProperty): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: TextBlock): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: TextElement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ThisExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: ThrowStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: TryStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: TypeDeclaration): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: TypeDeclarationStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: TypeLiteral): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: TypeMethodReference): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: TypeParameter): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: TypePattern): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    * @since 3.7.1
    */
  def visit(node: UnionType): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: UsesDirective): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: VariableDeclarationExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: VariableDeclarationStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: VariableDeclarationFragment): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: WhileStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: WildcardType): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: YieldStatement): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: StringTemplateExpression): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: StringFragment): String

  /** Visits the given type-specific AST node.
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: StringTemplateComponent): String

}
