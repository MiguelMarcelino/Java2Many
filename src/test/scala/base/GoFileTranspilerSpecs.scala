package base

import org.scalatest.wordspec.AnyWordSpec

import java.io.File
import java.nio.file.Files

class GoFileTranspilerSpecs extends AnyWordSpec {

  private val targetDir: File = Files.createTempDirectory("temp").toFile

  private val commandLineOptions: Map[String, String] =
    Map("language" -> "go", "file" -> "temp.java", "targetDir" -> "temp")

  "transpile" should {
    "successfully transpile an empty class" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          |}""".stripMargin

      val expectedResult = """
          |type SomeClass struct {
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully transpile a method declaration" in {
      // Arrange
      val javaMethodDeclaration =
        """
          |class SomeClass {
          |  public static void someMethod(String value) {
          |  }
          |}""".stripMargin

      val expectedResult = """
          |type SomeClass struct {
          |}
          |func someMethod(someClass SomeClass, value string) {
          |}""".stripMargin

      val file = writeFileContents(javaMethodDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully transpile a method declaration with a return type" in {
      // Arrange
      val javaMethodDeclaration =
        """
          |class SomeClass {
          |  public static void someMethod(String value) {
          |    return value;
          |  }
          |}""".stripMargin

      val expectedResult = """
          |type SomeClass struct {
          |}
          |func someMethod(someClass SomeClass, value string) {
          |    return value
          |}""".stripMargin

      val file = writeFileContents(javaMethodDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully transpile an empty return statement" in {
      // Arrange
      val javaMethodDeclaration =
        """
          |class SomeClass {
          |  public static void someMethod(String value) {
          |    return;
          |  }
          |}""".stripMargin

      val expectedResult = """
          |type SomeClass struct {
          |}
          |func someMethod(someClass SomeClass, value string) {
          |    return
          |}""".stripMargin

      val file = writeFileContents(javaMethodDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully parse a class with private variables" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass<T> {
          | private String value;
          |}""".stripMargin

      val expectedResult =
        """type SomeClass struct {
          |    value string
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully parse a class with methods" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          | private String value;
          |
          | public void setValue(String value) {
          |     this.value = value;
          | }
          |
          | public String getValue() {
          |     return this.value;
          | }
          |}""".stripMargin

      val expectedResult = """
          |type SomeClass struct {
          |    value string
          |}
          |
          |func setValue(someClass SomeClass, value string) {
          |    someClass.value := value
          |}
          |
          |func getValue(someClass SomeClass) {
          |    return someClass.value
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
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

      val expectedResult = """
          |type SomeClass struct {
          |    value string
          |}
          |
          |func SomeClass(value string) {
          |    return SomeClass(value = value)
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
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

  private def normalizeCode(code: String): String = code.replaceAll("\\s", "")
}
