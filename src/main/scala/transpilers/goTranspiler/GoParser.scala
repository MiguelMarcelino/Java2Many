package transpilers.goTranspiler

import base.ASTParser
import org.eclipse.jdt.core.dom.*

class GoParser extends ASTParser {

  val typesMap = Map(
    "int" -> "int",
    "boolean" -> "bool",
    "char" -> "rune",
    "byte" -> "byte",
    "short" -> "int16",
    "long" -> "int64",
    "float" -> "float32",
    "double" -> "float64",
    "String" -> "string",
    "void" -> "void"
  )

  override def visit(node: AnnotationTypeDeclaration): String = { "" }

  override def visit(node: AnnotationTypeMemberDeclaration): String = { "" }

  override def visit(node: AnonymousClassDeclaration): String = { "" }

  override def visit(node: ArrayAccess): String = { "" }

  override def visit(node: ArrayCreation): String = { "" }

  override def visit(node: ArrayInitializer): String = { "" }

  override def visit(node: ArrayType): String = { "" }

  override def visit(node: AssertStatement): String = { "" }

  override def visit(node: Assignment): String = { "" }

  override def visit(node: Block): String = {
    node
      .statements()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
      .mkString("\n")
  }

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
    val imports = node
      .imports()
      .toArray()
      .map {
        case n: ImportDeclaration =>
          visit(n)
        case _ => ""
      }
      .mkString("\n")

    // As per the documentation, CompilationUnit is either an OrdinaryCompilationUnit or a ModularCompilationUnit
    //
    // OrdinaryCompilationUnit:
    //      [ PackageDeclaration ]
    //          { ImportDeclaration }
    //          { TypeDeclaration | EnumDeclaration | AnnotationTypeDeclaration | <b>;</b> }
    // ModularCompilationUnit:
    //      {ImportDeclaration}
    //           ModuleDeclaration

    val isOrdinaryCompilationUnit = node.getModule == null

    isOrdinaryCompilationUnit match {
      case true => // It is an OrdinaryCompilationUnit
        val pkg = node.getPackage == null match {
          case false => visit(node.getPackage)
          case true  => ""
        }

        val body = node
          .types()
          .toArray()
          .map {
            case n: ASTNode =>
              visit(n)
            case _ => ""
          }
          .mkString("\n")

        s"""$pkg
           |$imports
           |
           |$body""".stripMargin
      case _ => // Otherwise, it is a ModularCompilationUnit
        val moduleDeclaration = visit(node.getModule)
        s"""$imports
           |
           |$moduleDeclaration""".stripMargin
    }
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

  override def visit(node: ExpressionStatement): String = {
    visit(node.getExpression)
  }

  override def visit(node: FieldAccess): String = { "" }

  override def visit(node: FieldDeclaration): String = {
    val declarationType = visit(node.getType)
    val declarationNames = node
      .fragments()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
    val declarationValue = declarationNames.headOption.getOrElse("")

    s"$declarationValue $declarationType"
  }

  override def visit(node: ForStatement): String = {
    val nodeBody = visit(node.getBody)
    s"""for () {
       |  $nodeBody
       |}""".stripMargin
  }

  override def visit(node: GuardedPattern): String = { "" }

  override def visit(node: IfStatement): String = { "" }

  override def visit(node: ImportDeclaration): String = {
    s"""import \"${node.getName}\""""
  }

  override def visit(node: InfixExpression): String = { "" }

  override def visit(node: Initializer): String = {
    visit(node.getBody)
  }

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
    val parameters = node
      .parameters()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
      .mkString(", ")

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

    node.isConstructor match {
      case true =>
        // TODO: Investigate bug. For some reason, doing this on the GoClassRewriter inserts a
        //  TypeDeclaration on the class level
        val parentName = node.getParent match {
          case p: TypeDeclaration => p.getName.getIdentifier
          case _                  => "EMPTY_PARENT"
        }
        val parameterNames = node.parameters().toArray.map {
          case parameter: SingleVariableDeclaration =>
            parameter.getName.getIdentifier
        }
        s"""func $parsedFuncName ($parameters) {
           |  return $parentName(${parameterNames.mkString(", ")})
           |}""".stripMargin
      case _ =>
        s"""func $parsedFuncName ($parameters) {
           |  $nodeBody
           |}""".stripMargin
    }

  }

  override def visit(node: MethodInvocation): String = {
    val name = visit(node.getName)
    val arguments = node
      .arguments()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
      .mkString(", ")

    // Expressions must be passed as an argument, as Go does not have classes.
    // We therefore rely on dispatching to find the correct function.
    node.getExpression match {
      case n: Expression =>
        val expression = visit(node.getExpression)
        s"$name($expression, $arguments)"
      case null => s"$name($arguments)"
    }
  }

  override def visit(node: Modifier): String = {
    // TODO: Revisit this method
    node.getKeyword.toString
  }

  override def visit(node: ModuleDeclaration): String = {
    val statements = node
      .moduleStatements()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
      .mkString("\n")

    s"""module ${node.getName.getFullyQualifiedName} {
       |  $statements
       |}""".stripMargin
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

  override def visit(node: PackageDeclaration): String = {
    s"package ${node.getName.getFullyQualifiedName}"
  }

  override def visit(node: ParameterizedType): String = { "" }

  override def visit(node: ParenthesizedExpression): String = { "" }

  override def visit(node: PatternInstanceofExpression): String = { "" }

  override def visit(node: PostfixExpression): String = { "" }

  override def visit(node: PrefixExpression): String = { "" }

  override def visit(node: ProvidesDirective): String = { "" }

  override def visit(node: QualifiedName): String = { "" }

  override def visit(node: QualifiedType): String = { "" }

  override def visit(node: ModuleQualifiedName): String = { "" }

  override def visit(node: RequiresDirective): String = { "" }

  override def visit(node: RecordDeclaration): String = { "" }

  override def visit(node: RecordPattern): String = { "" }

  override def visit(node: ReturnStatement): String = {
    s"return ${visit(node.getExpression)}"
  }

  override def visit(node: SimpleName): String = {
    s"${node.getIdentifier}"
  }

  override def visit(node: SimpleType): String = {
    s"${node.getName.getFullyQualifiedName}"
  }

  override def visit(node: SingleMemberAnnotation): String = { "" }

  override def visit(node: SingleVariableDeclaration): String = {
    val typeName = visit(node.getType)
    val name = visit(node.getName)
    s"$name $typeName"
  }

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

  override def visit(node: TypeDeclaration): String = {
    val fields = node.getFields.map(node => visit(node)).mkString("\n")
    val methods = node.getMethods.map(node => visit(node)).mkString("\n")

    s"""type ${node.getName} struct {
       |  $fields
       |}
       |
       |$methods""".stripMargin
  }

  override def visit(node: TypeDeclarationStatement): String = { "" }

  override def visit(node: TypeLiteral): String = {
    s"${node.getType.toString}"
  }

  override def visit(node: TypeMethodReference): String = {
    s"${node.getType.toString}"
  }

  override def visit(node: TypeParameter): String = {
    val name = node.getName.getIdentifier
    val bounds = node
      .typeBounds()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
      .mkString(", ")

    s"$name $bounds"
  }

  override def visit(node: TypePattern): String = { "" }

  override def visit(node: UnionType): String = { "" }

  override def visit(node: UsesDirective): String = { "" }

  override def visit(node: VariableDeclarationExpression): String = {
    val typeName = visit(node.getType)
    val fragments = node
      .fragments()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
      .mkString(", ")

    s"$fragments: $typeName"
  }

  override def visit(node: VariableDeclarationStatement): String = {
    val typeName = visit(node.getType)
    val fragments = node
      .fragments()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
      .mkString(", ")

    s"$fragments: $typeName"
  }

  override def visit(node: VariableDeclarationFragment): String = {
    s"${node.getName.getIdentifier}"
  }

  override def visit(node: WhileStatement): String = { "" }

  override def visit(node: WildcardType): String = { "" }

  override def visit(node: YieldStatement): String = { "" }

  override def visit(node: StringTemplateExpression): String = { "" }

  override def visit(node: StringFragment): String = {
    node.getEscapedValue
  }

  override def visit(node: StringTemplateComponent): String = { "" }
}
