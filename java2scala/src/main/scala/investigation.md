# Investigating the best AST parsers

## Eclipse JDT library 
The eclipse JDT library includes an AST parsing tool.It uses the visitor pattern to visit nodes.
We tried using it to create our transpiler, but it appears to have some limitations. To test it, we used the code snippet below.

```scala
import org.eclipse.jdt.core.dom.*

// Your Java code
val javaCode: String =
  """
class HelloWorld {
    public static void main(String[] args) {
        if(true) {
            System.out.println('Hello, World!';
        }
    }

    public static void aMethod(String arg) {
        System.out.println('Hello, World!';
    }
}"""

// Create ASTParser instance
val parser: ASTParser =
  ASTParser.newParser(AST.JLS3) // Use the appropriate Java version

// Set source code and options
parser.setSource(javaCode.toCharArray())
parser.setKind(ASTParser.K_COMPILATION_UNIT)

// Parse the code and get the AST
val astNode: ASTNode = parser.createAST(null)

val astVisitor = new ASTVisitor() {
  override def visit(node: MethodInvocation): Boolean = {
    val name = node.getName()
    name.setIdentifier("hello")
    node.setName(name)
    super.visit(node)
  }

  override def visit(node: IfStatement): Boolean = {
    println(node.toString()) // Does not occur
    super.visit(node)
  }

  override def visit(node: AnonymousClassDeclaration): Boolean = {
    println(node.toString()) // Does not occur
    super.visit(node)
  }
}

// Traverse the AST
astNode.accept(astVisitor)
println(astNode)
```

Eclipse's AST parser does not seem to allow us to search for all the information about nodes. 
The output from the code above is as follows:

```java
class HelloWorld {
  public static void main(  String[] args){
  }
  public static void aMethod(  String arg){
  }
}
```

As you can see, it does not give us the if nodes. We still need to explore more about Eclipse's AST parsing capabilities.
The line `parser.setKind(ASTParser.K_COMPILATION_UNIT)` seems to decide the depth of the tree search.

### Relevant doc links
* [ASTParser](https://www.ibm.com/docs/api/v1/content/SS8PJ7_9.5.0/org.eclipse.jdt.doc.isv/reference/api/org/eclipse/jdt/core/dom/ASTParser.html#newParser-int-)
* [ASTVisitor](https://www.ibm.com/docs/en/rational-soft-arch/9.5?topic=SS8PJ7_9.5.0/org.eclipse.jdt.doc.isv/reference/api/org/eclipse/jdt/core/dom/ASTVisitor.htm)

## Using `javaparser`
The `javaparser` tool can build an AST from java source code and also provides some analysis functionalities.
The source code below was used for testing the functionalities.

```scala
import java.nio.charset.StandardCharsets
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.utils.CodeGenerationUtils

val javaCode =
  """
      |public class HelloWorld {
      |    public static void main(String[] args) {
      |        System.out.println("Hello, world!");
      |    }
      |}
    """.stripMargin

// Parse the Java source code from the string and create an AST
val compilationUnit: CompilationUnit = StaticJavaParser.parse(javaCode)

// Now you can work with the AST
// For example, you can traverse the AST using visitors, modify it, analyze it, etc.

// Print the AST as a string
println(compilationUnit.toString)
```


TODO: Continue from here