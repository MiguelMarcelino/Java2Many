package base

import org.eclipse.jdt.core.dom.*

import scala.collection.mutable.ArrayBuffer

/** Defines the methods that must be implemented by any parser that
  * wants to convert a Java AST to a specific target language.
  *
  * It extends the ASTNodeVisitor trait, which defines the visit method for all
  * the AST nodes. The ASTParser trait defines the visit method for the type-specific
  * AST nodes.
  */
trait ASTParser {

  // A dumb visit method
  def visit(node: ASTNode): String = {
    node match {
      case n: Annotation                      => visit(n)
      case n: AnnotationTypeDeclaration       => visit(n)
      case n: AnnotationTypeMemberDeclaration => visit(n)
      case n: AnonymousClassDeclaration       => visit(n)
      case n: ArrayAccess                     => visit(n)
      case n: ArrayCreation                   => visit(n)
      case n: ArrayInitializer                => visit(n)
      case n: ArrayType                       => visit(n)
      case n: AssertStatement                 => visit(n)
      case n: Assignment                      => visit(n)
      case n: Block                           => visit(n)
      case n: BlockComment                    => visit(n)
      case n: BooleanLiteral                  => visit(n)
      case n: BreakStatement                  => visit(n)
      case n: CaseDefaultExpression           => visit(n)
      case n: CastExpression                  => visit(n)
      case n: CatchClause                     => visit(n)
      case n: CharacterLiteral                => visit(n)
      case n: ClassInstanceCreation           => visit(n)
      case n: CompilationUnit                 => visit(n)
      case n: ConditionalExpression           => visit(n)
      case n: ConstructorInvocation           => visit(n)
      case n: ContinueStatement               => visit(n)
      case n: CreationReference               => visit(n)
      case n: Dimension                       => visit(n)
      case n: DoStatement                     => visit(n)
      case n: EmptyStatement                  => visit(n)
      case n: EnhancedForStatement            => visit(n)
      case n: EnumConstantDeclaration         => visit(n)
      case n: EnumDeclaration                 => visit(n)
      case n: ExportsDirective                => visit(n)
      case n: ExpressionMethodReference       => visit(n)
      case n: ExpressionStatement             => visit(n)
      case n: FieldAccess                     => visit(n)
      case n: FieldDeclaration                => visit(n)
      case n: ForStatement                    => visit(n)
      case n: GuardedPattern                  => visit(n)
      case n: IfStatement                     => visit(n)
      case n: ImportDeclaration               => visit(n)
      case n: InfixExpression                 => visit(n)
      case n: Initializer                     => visit(n)
      case n: InstanceofExpression            => visit(n)
      case n: IntersectionType                => visit(n)
      case n: Javadoc                         => visit(n)
      case n: JavaDocRegion                   => visit(n)
      case n: JavaDocTextElement              => visit(n)
      case n: LabeledStatement                => visit(n)
      case n: LambdaExpression                => visit(n)
      case n: LineComment                     => visit(n)
      case n: MarkerAnnotation                => visit(n)
      case n: MemberRef                       => visit(n)
      case n: MemberValuePair                 => visit(n)
      case n: MethodRef                       => visit(n)
      case n: MethodRefParameter              => visit(n)
      case n: MethodDeclaration               => visit(n)
      case n: MethodInvocation                => visit(n)
      case n: Modifier                        => visit(n)
      case n: ModuleDeclaration               => visit(n)
      case n: ModuleModifier                  => visit(n)
      case n: NameQualifiedType               => visit(n)
      case n: NormalAnnotation                => visit(n)
      case n: NullLiteral                     => visit(n)
      case n: NullPattern                     => visit(n)
      case n: NumberLiteral                   => visit(n)
      case n: OpensDirective                  => visit(n)
      case n: PackageDeclaration              => visit(n)
      case n: ParameterizedType               => visit(n)
      case n: ParenthesizedExpression         => visit(n)
      case n: PatternInstanceofExpression     => visit(n)
      case n: PostfixExpression               => visit(n)
      case n: PrefixExpression                => visit(n)
      case n: ProvidesDirective               => visit(n)
      case n: PrimitiveType                   => visit(n)
      case n: QualifiedName                   => visit(n)
      case n: QualifiedType                   => visit(n)
      case n: ModuleQualifiedName             => visit(n)
      case n: RequiresDirective               => visit(n)
      case n: RecordDeclaration               => visit(n)
      case n: RecordPattern                   => visit(n)
      case n: ReturnStatement                 => visit(n)
      case n: SimpleName                      => visit(n)
      case n: SimpleType                      => visit(n)
      case n: SingleMemberAnnotation          => visit(n)
      case n: SingleVariableDeclaration       => visit(n)
      case n: StringLiteral                   => visit(n)
      case n: SuperConstructorInvocation      => visit(n)
      case n: SuperFieldAccess                => visit(n)
      case n: SuperMethodInvocation           => visit(n)
      case n: SuperMethodReference            => visit(n)
      case n: SwitchCase                      => visit(n)
      case n: SwitchExpression                => visit(n)
      case n: SwitchStatement                 => visit(n)
      case n: SynchronizedStatement           => visit(n)
      case n: TagElement                      => visit(n)
      case n: TagProperty                     => visit(n)
      case n: TextBlock                       => visit(n)
      case n: TextElement                     => visit(n)
      case n: ThisExpression                  => visit(n)
      case n: ThrowStatement                  => visit(n)
      case n: TryStatement                    => visit(n)
      case n: TypeDeclaration                 => visit(n)
      case n: TypeDeclarationStatement        => visit(n)
      case n: TypeLiteral                     => visit(n)
      case n: TypeMethodReference             => visit(n)
      case n: TypeParameter                   => visit(n)
      case n: TypePattern                     => visit(n)
      case n: UnionType                       => visit(n)
      case n: UsesDirective                   => visit(n)
      case n: VariableDeclarationExpression   => visit(n)
      case n: VariableDeclarationStatement    => visit(n)
      case n: VariableDeclarationFragment     => visit(n)
      case n: WhileStatement                  => visit(n)
      case n: WildcardType                    => visit(n)
      case n: YieldStatement                  => visit(n)
      case n: StringTemplateExpression        => visit(n)
      case n: StringFragment                  => visit(n)
      case n: StringTemplateComponent         => visit(n)
      case _ =>
        throw new IllegalArgumentException(
          s"Unknown AST node type: ${node.getClass.getSimpleName}"
        )
    }
  }

  /** The map of types to their specific types in the target language.
    * Every parser must define their own map.
    */
  protected val typesMap: Map[String, String]

  /** The map of calls to their specific calls in the target language.
    * Every parser must define their own map.
    */
  protected val callsMap: Map[String, String]

  /** A list of imports to be added to the target language code.
    */
  protected val importsList: ArrayBuffer[String] = ArrayBuffer()

  protected def getImports(node: CompilationUnit) = {
    val imports = node
      .imports()
      .toArray()
      .map {
        case n: ImportDeclaration =>
          visit(n)
        case _ => ""
      }
    val totalImports = importsList ++ imports

    totalImports.mkString("\n")

  }

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
  def visit(node: PrimitiveType): String = {
    node.getPrimitiveTypeCode.toString match {
      case t if typesMap.contains(t) => typesMap(t)
      case t                         => t
    }
  }

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
    * This method maps the Scala types to their specific types in the target language
    *
    * @param node the node to visit
    * @return the resulting code as a string
    */
  def visit(node: Type): String = {
    node.toString match {
      case n if typesMap.contains(n) => typesMap(n)
      case n                         => n
    }
  }

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
