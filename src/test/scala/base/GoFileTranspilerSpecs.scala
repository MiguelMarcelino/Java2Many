package base

import org.scalatest.wordspec.AnyWordSpec

import java.io.File
import java.nio.file.Files

class GoFileTranspilerSpecs extends AnyWordSpec {

  private val targetDir: File = Files.createTempDirectory("temp").toFile

  private val commandLineOptions: Map[String, String] =
    Map("language" -> "go", "file" -> "temp.java", "targetDir" -> "temp")

  "transpile" should {
    "correctly transpile a method declaration" in {
      // Arrange
      val javaMethodDeclaration =
        """
          |class SomeClass {
          |  public static void someMethod(String value) {
          |  }
          |}""".stripMargin

      val expectedResult =
        "type SomeClass struct {" + "\n" +
          "}" + "\n" +
          "func someMethod(value string) {" + "\n" +
          "}".stripMargin

      val file = writeFileContents(javaMethodDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(
        fileAsString.linesIterator
          .map(_.trim)
          .mkString("")
          .replaceAll("\\s", "") ==
          expectedResult.linesIterator
            .map(_.trim)
            .mkString("")
            .replaceAll("\\s", "")
      )
    }

    "correctly transpile a method declaration with a return type" in {
      // Arrange
      val javaMethodDeclaration =
        """public static String someMethod(String value) {
          |   return value;
          |}""".stripMargin

      val file = writeFileContents(javaMethodDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(
        fileAsString ==
          """func someMethod(value string) string {
            |    return value
            |}""".stripMargin
      )
    }

    "successfully transpile an empty return statement" in {
      // Arrange
      val javaMethodDeclaration =
        """public static String someMethod(String value) {
          |   return;
          |}""".stripMargin

      val file = writeFileContents(javaMethodDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(
        fileAsString ==
          """func someMethod(value string) string {
            |    return
            |}""".stripMargin
      )
    }

    "successfully transpile a struct without fields" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(
        fileAsString ==
          """type SomeClass struct {
            |}""".stripMargin
      )
    }

    "successfully parse a class with private variables" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass<T> {
          | private String value;
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(
        fileAsString ==
          """type SomeClass struct {
            |    value string
            |}""".stripMargin
      )
    }

    "successfully parse a class with methods" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass<T> {
          | private String value;
          |
          | public void setValue(String value) {
          |     this.value = value;
          | }
          |
          | public String getValue() {
          |     return value;
          | }
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(
        fileAsString ==
          """type SomeClass struct {
            |    value string
            |}
            |
            |func (s *SomeClass) setValue(value string) {
            |    s.value = value
            |}
            |
            |func (s *SomeClass) getValue() string {
            |    return s.value
            |}""".stripMargin
      )
    }

    "successfully parse a class with constructor" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass<T> {
          | private String value;
          |
          | public SomeClass(String value) {
          |     this.value = value;
          | }
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(
        fileAsString ==
          """type SomeClass struct {
            |    value string
            |}
            |
            |func NewSomeClass(value string) *SomeClass {
            |    return &SomeClass{value: value}
            |}""".stripMargin
      )
    }

  }

  private def readFileContents(file: File): String = {
    val fileContents = Files.readAllLines(file.toPath).toArray()
    fileContents.mkString("\n")
  }

  private def writeFileContents(contents: String): File = {
    val file: File = Files.createTempFile("temp", ".java").toFile
    Files.write(file.toPath, contents.getBytes()).toFile
  }
}
