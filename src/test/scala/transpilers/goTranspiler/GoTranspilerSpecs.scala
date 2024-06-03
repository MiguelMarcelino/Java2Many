package transpilers.goTranspiler

import org.eclipse.jdt.core.dom.{ASTNode, MethodDeclaration}
import org.eclipse.jface.text.Document
import org.scalatest.wordspec.AnyWordSpec

class GoTranspilerSpecs extends AnyWordSpec {

  def transpileCode(codeAsString: String): String = {
    val document = Document(codeAsString.stripMargin)
    val transpiler = new GoTranspiler(document)
    transpiler.transpileCode()
  }

  "goTranspiler" should {
    "return the correct method declaration" in {
      // Arrange
      val javaMethodDeclaration =
        """public static void someMethod(String value) {
          |}""".stripMargin

      // Act
      val result = transpileCode(javaMethodDeclaration)

      assert(
        result ==
          """func someMethod(value string) {
          |}""".stripMargin
      )
    }

    "successfully parse empty return statement" in {
      // Arrange
      val javaMethodDeclaration =
        """public static String someMethod(String value) {
          |  return;
          |}""".stripMargin

      // Act
      val result = transpileCode(javaMethodDeclaration)

      assert(
        result ==
          """func someMethod(value string) string {
          |  return
          |}""".stripMargin
      )
    }

    "successfully parse return statement" in {
      // Arrange
      val javaMethodDeclaration =
        """public static String someMethod(String value) {
          |  return value;
          |}""".stripMargin

      // Act
      val result = transpileCode(javaMethodDeclaration)

      assert(
        result ==
          """func someMethod(value string) string {
          |  return value
          |}""".stripMargin
      )
    }

    // Class tests
    "successfully create a struct without fields" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          |}""".stripMargin

      // Act
      val result = transpileCode(javaClassDeclaration)

      assert(
        result ==
          """type SomeClass struct {
          |}""".stripMargin
      )
    }

    "successfully parse a class with type parameters" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          |  private String value;
          |}""".stripMargin

      // Act
      val result = transpileCode(javaClassDeclaration)

      // TODO: Review test
      assert(
        result ==
          """type SomeClass struct {
            |}""".stripMargin
      )
    }

    "successfully parse a class with methods" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          |  private String value;
          |
          |  public void someMethod(String value) {
          |  }
          |}""".stripMargin

      // Act
      val result = transpileCode(javaClassDeclaration)

      assert(
        result ==
          """type SomeClass struct {
            |  value string
            |}
            |
            |func (s *SomeClass) someMethod(value string) {
            |}""".stripMargin
      )
    }

    "successfully parse a class with constructor" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          |  private String value;
          |
          |  public SomeClass(String value) {
          |    this.value = value;
          |  }
          |}""".stripMargin

      // Act
      val result = transpileCode(javaClassDeclaration)

      assert(
        result ==
          """type SomeClass struct {
            |  value string
            |}
            |
            |func NewSomeClass(value string) *SomeClass {
            |  return &SomeClass{value: value}
            |}""".stripMargin
      )
    }
  }
}
