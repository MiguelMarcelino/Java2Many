package transpilers.goTranspiler

import base.ASTParser
import base.helpers.ASTNodeHelpers
import org.eclipse.jdt.core.dom.*

import scala.collection.mutable.ArrayBuffer

class GoParser extends ASTParser {

  override protected val typesMap = Map(
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

  override protected val callsMap = Map(
    "System.out.println" -> "fmt.Println",
    "System.out.print" -> "fmt.Print",
    "System.out.printf" -> "fmt.Printf",
    "System.in" -> "os.Stdin",
    "System.out" -> "os.Stdout",
    "System.err" -> "os.Stderr"
  )

  override protected val importsList = ArrayBuffer[String]()

  private def addImport(importName: String): Unit = {
    importsList.append(importName)
  }

  override def visit(node: AnnotationTypeDeclaration): String = {
    // TODO: Check if this is what is expected
    val body = node
      .bodyDeclarations()
      .toArray()
      .map {
        case n: ASTNode =>
          visit(n)
        case _ => ""
      }
      .mkString("\n")

    s"""type ${node.getName} struct {
       |  $body
       |}""".stripMargin
  }

  override def visit(node: AnnotationTypeMemberDeclaration): String = {
    val typeName = visit(node.getType)
    val name = visit(node.getName)
    node.getDefault match {
      case null => s"$name $typeName"
      case default =>
        val defaultValue = visit(default)
        s"$name $typeName = $defaultValue"
    }
  }

  override def visit(node: AnonymousClassDeclaration): String = { "" }

  override def visit(node: ArrayAccess): String = {
    val array = visit(node.getArray)
    val index = visit(node.getIndex)
    s"$array[$index]"
  }

  override def visit(node: ArrayCreation): String = {
    val typeName = visit(node.getType)
    val dimensions = node
      .dimensions()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
      .mkString(", ")

    val initializer = node.getInitializer match {
      case null => ""
      case _    => visit(node.getInitializer)
    }

    // TODO: Check initializer
    s"[$dimensions]$typeName $initializer"
  }

  override def visit(node: ArrayInitializer): String = {
    val expressions = node
      .expressions()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
      .mkString(", ")

    s"{$expressions}"
  }

  override def visit(node: ArrayType): String = {
    val typeName = visit(node.getElementType)
    val dimensions = node.getDimensions
    s"[$dimensions]$typeName"
  }

  override def visit(node: AssertStatement): String = {
    val expression = visit(node.getExpression)
    val message = node.getMessage match {
      case null => ""
      case _    => visit(node.getMessage)
    }

    // Since go does not have an assert statement, we use the panic statement
    // Unit tests are translated separately
    s"""if !($expression) {
       |  panic("$message")
       |}"""
  }

  override def visit(node: Assignment): String = {
    val leftHandSide = visit(node.getLeftHandSide)
    val rightHandSide = visit(node.getRightHandSide)
    s"$leftHandSide := $rightHandSide"
  }

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

  override def visit(node: BooleanLiteral): String = {
    s"${node.booleanValue()}"
  }

  override def visit(node: BreakStatement): String = {
    node.getLabel match {
      case null => "break"
      case _    => s"break ${visit(node.getLabel)}"
    }
  }

  override def visit(node: CaseDefaultExpression): String = {
    "default"
  }

  override def visit(node: CastExpression): String = { "" }

  override def visit(node: CatchClause): String = {
    val exception = visit(node.getException)
    val body = visit(node.getBody)
    s"""catch $exception {
       |  $body
       |}"""
  }

  override def visit(node: CharacterLiteral): String = { "" }

  override def visit(node: ClassInstanceCreation): String = {
    val typeName = visit(node.getType)
    val arguments = node
      .arguments()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
      .mkString(", ")

    s"""$typeName {
       |  $arguments
       |}""".stripMargin
  }

  override def visit(node: CompilationUnit): String = {
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

        // We must get the imports here, as there are nodes in the body that may add imports
        val imports = getImports(node)

        s"""$pkg
           |$imports
           |
           |$body""".stripMargin
      case _ => // Otherwise, it is a ModularCompilationUnit
        val moduleDeclaration = visit(node.getModule)

        // We must get the imports here, as there are nodes in the body that may add imports
        val imports = getImports(node)

        s"""$imports
           |
           |$moduleDeclaration""".stripMargin
    }
  }

  override def visit(node: ConditionalExpression): String = {
    val expression = visit(node.getExpression)
    val thenExpression = visit(node.getThenExpression)
    val elseExpression = visit(node.getElseExpression)
    // Go does not support the ternary operator, so we use the shortest way to build an if-else statement
    s"""if $elseExpression; $expression {
       |  $thenExpression
       |}""".stripMargin
  }

  override def visit(node: ConstructorInvocation): String = { "" }

  override def visit(node: ContinueStatement): String = { "" }

  override def visit(node: CreationReference): String = { "" }

  override def visit(node: Dimension): String = { "" }

  override def visit(node: DoStatement): String = {
    val body = visit(node.getBody)
    val expression = visit(node.getExpression)
    // TODO: Check if this is correct
    // There is no do while statement in Go, so we use a for loop
    s"""for $expression {
       |  $body
       |}""".stripMargin
  }

  override def visit(node: EmptyStatement): String = { "" }

  override def visit(node: EnhancedForStatement): String = { "" }

  override def visit(node: EnumConstantDeclaration): String = { "" }

  override def visit(node: EnumDeclaration): String = { "" }

  override def visit(node: ExportsDirective): String = { "" }

  override def visit(node: ExpressionMethodReference): String = { "" }

  override def visit(node: ExpressionStatement): String = {
    visit(node.getExpression)
  }

  override def visit(node: FieldAccess): String = {
    val expression = visit(node.getExpression)
    val name = visit(node.getName)
    s"$expression.$name"
  }

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
    val expression = visit(node.getExpression)
    s"""for ($expression) {
       |  $nodeBody
       |}""".stripMargin
  }

  override def visit(node: GuardedPattern): String = { "" }

  override def visit(node: IfStatement): String = {
    val expression = visit(node.getExpression)
    val thenStatement = visit(node.getThenStatement)
    val parent = node.getParent

    val elseStatement = node.getElseStatement match {
      case null => ""
      case _: IfStatement =>
        visit(node.getElseStatement)
      case _ =>
        s"""else {
           |  ${visit(node.getElseStatement)}
           |}"""
    }

    parent match {
      case _: IfStatement =>
        s"""else if $expression {
             |  $thenStatement
             |} $elseStatement"""
      case _ =>
        s"""if $expression {
             |  $thenStatement
             |} $elseStatement"""

    }
  }

  override def visit(node: ImportDeclaration): String = {
    s"""import \"${node.getName}\""""
  }

  override def visit(node: InfixExpression): String = { "" }

  override def visit(node: Initializer): String = {
    visit(node.getBody)
  }

  override def visit(node: InstanceofExpression): String = { "" }

  override def visit(node: IntersectionType): String = { "" }

  override def visit(node: Javadoc): String = {
    // convert to docs in golang
    val doc = node.toString
    val lines = doc.split("\n")
    lines.map(line => s"// $line").mkString("\n")
  }

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
        // Find all assignments to class variables and use them when creating an instance of the class
        // in the constructor
        val thisAssignments = node.getBody.statements().toArray().collect {
          case exprStmt: ExpressionStatement =>
            exprStmt.getExpression match {
              case assignment: Assignment =>
                assignment.getLeftHandSide match {
                  case fieldAccess: FieldAccess
                      if fieldAccess.getExpression
                        .isInstanceOf[ThisExpression] =>
                    val assignmentVal = assignment.getRightHandSide.toString
                    s"${fieldAccess.getName.getIdentifier} = $assignmentVal"
                }
            }
        }
        s"""func $parsedFuncName ($parameters) {
           |  return $parentName(${thisAssignments.mkString(", ")})
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
        callsMap.get(s"$expression.$name") match {
          case Some(value) => s"$value($arguments)"
          case None        => s"$name($expression, $arguments)"
        }
      case null => s"$name($arguments)"
    }
  }

  override def visit(node: Modifier): String = {
    // Modifiers are most likely not relevant in go, so I will just return an empty string for now
    ""
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

  override def visit(node: NullLiteral): String = {
    "nil"
  }

  override def visit(node: NullPattern): String = { "" }

  override def visit(node: NumberLiteral): String = {
    s"${node.getToken}"
  }

  override def visit(node: OpensDirective): String = { "" }

  override def visit(node: PackageDeclaration): String = {
    s"package ${node.getName.getFullyQualifiedName}"
  }

  override def visit(node: ParameterizedType): String = {
    val typeName = visit(node.getType)
    val typeArguments = node
      .typeArguments()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
      .mkString(", ")

    s"$typeName $typeArguments"
  }

  override def visit(node: ParenthesizedExpression): String = { "" }

  override def visit(node: PatternInstanceofExpression): String = { "" }

  override def visit(node: PostfixExpression): String = { "" }

  override def visit(node: PrefixExpression): String = { "" }

  override def visit(node: ProvidesDirective): String = { "" }

  override def visit(node: QualifiedName): String = {
    s"${node.getFullyQualifiedName}"
  }

  override def visit(node: QualifiedType): String = { "" }

  override def visit(node: ModuleQualifiedName): String = { "" }

  override def visit(node: RequiresDirective): String = { "" }

  override def visit(node: RecordDeclaration): String = { "" }

  override def visit(node: RecordPattern): String = { "" }

  override def visit(node: ReturnStatement): String = {
    node.getExpression match {
      case null => "return"
      case _    => s"return ${visit(node.getExpression)}"
    }
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

  override def visit(node: StringLiteral): String = {
    s"`${node.getEscapedValue}`"
  }

  override def visit(node: SuperConstructorInvocation): String = {
    // There is no super constructor call in go
    ""
  }

  override def visit(node: SuperFieldAccess): String = { "" }

  override def visit(node: SuperMethodInvocation): String = {
    // There is no super method call in go
    ""
  }

  override def visit(node: SuperMethodReference): String = {
    // There is no super method call in go
    ""
  }

  override def visit(node: SwitchCase): String = { "" }

  override def visit(node: SwitchExpression): String = { "" }

  override def visit(node: SwitchStatement): String = {
    val expression = visit(node.getExpression)
    val body = node
      .statements()
      .toArray()
      .map {
        case node: ASTNode =>
          visit(node)
        case _ => ""
      }
      .mkString("\n")

    s"""switch $expression {
       |  $body
       |}""".stripMargin
  }

  override def visit(node: SynchronizedStatement): String = { "" }

  override def visit(node: TagElement): String = { "" }

  override def visit(node: TagProperty): String = { "" }

  override def visit(node: TextBlock): String = { "" }

  override def visit(node: TextElement): String = { "" }

  override def visit(node: ThisExpression): String = {
    // Check the arguments of the method where it is being called and create an assignment
    // to the class variable
    val parentClassNode =
      ASTNodeHelpers.findParentNode(node, classOf[TypeDeclaration])
    parentClassNode match {
      case Some(parentClass) =>
        val className = parentClass.getName.getIdentifier
        className.head.toLower +: className.tail
      case None => "this"
    }
  }

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

  override def visit(node: WhileStatement): String = {
    val expression = visit(node.getExpression)
    val body = visit(node.getBody)
    // Go does not have a do while statement. For is go's while statement
    s"""for $expression {
       |  $body
       |}""".stripMargin
  }

  override def visit(node: WildcardType): String = { "" }

  override def visit(node: YieldStatement): String = { "" }

  override def visit(node: StringTemplateExpression): String = { "" }

  override def visit(node: StringFragment): String = {
    node.getEscapedValue
  }

  override def visit(node: StringTemplateComponent): String = { "" }
}
