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

      // Assert
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

      // Assert
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

      // Assert
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

      // Assert
      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully transpile a class with private variables" in {
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

      // Assert
      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully transpile a class with methods" in {
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

      // Assert
      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully transpile a class with constructor" in {
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

      // Assert
      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully transpile assignment operations" in {
      // Test all assignments specified in the Assignment class:
      // = ASSIGN
      // += PLUS_ASSIGN
      // -= MINUS_ASSIGN
      // *= TIMES_ASSIGN
      // /= DIVIDE_ASSIGN
      // &= BIT_AND_ASSIGN
      // |= BIT_OR_ASSIGN
      // ^= BIT_XOR_ASSIGN
      // %= REMAINDER_ASSIGN
      // <<= LEFT_SHIFT_ASSIGN
      // >>= RIGHT_SHIFT_SIGNED_ASSIGN
      // >>>= RIGHT_SHIFT_UNSIGNED_ASSIGN

      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
            | public void someMethod() {
            |     int a = 1;
            |     a += 1;
            |     a -= 1;
            |     a *= 1;
            |     a /= 1;
            |     a &= 1;
            |     a |= 1;
            |     a ^= 1;
            |     a %= 1;
            |     a <<= 1;
            |     a >>= 1;
            |     a >>>= 1;
            | }
            |}""".stripMargin

      val expectedResult = """
          |type SomeClass struct {
          |}
          |
          |func someMethod(someClass SomeClass) {
          |    a int := 1
          |    a += 1
          |    a -= 1
          |    a *= 1
          |    a /= 1
          |    a &= 1
          |    a |= 1
          |    a ^= 1
          |    a %= 1
          |    a <<= 1
          |    a >>= 1
          |    a >>>= 1
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      // Assert
      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))

    }

    "successfully transpile arithmetic expressions" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          | public void someMethod() {
          |     int a = 1 + 2;
          |     int b = 1 - 2;
          |     int c = 1 * 2;
          |     int d = 1 / 2;
          | }
          |}""".stripMargin

      val expectedResult =
        """
          |type SomeClass struct {
          |}
          |
          |func someMethod(someClass SomeClass) {
          |    a int := 1 + 2
          |    b int := 1 - 2
          |    c int := 1 * 2
          |    d int := 1 / 2
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      // Assert
      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))

    }

    "successfully transpile a simple if-statement" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          | public void someMethod() {
          |     if (true) {
          |         return;
          |     }
          | }
          |}""".stripMargin

      val expectedResult =
        """
          |type SomeClass struct {
          |}
          |
          |func someMethod(someClass SomeClass) {
          |    if true {
          |        return
          |    }
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      // Assert
      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully transpile an if-else statement" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          | public void someMethod() {
          |     if (true) {
          |         return;
          |     } else {
          |         return;
          |     }
          | }
          |}""".stripMargin

      val expectedResult =
        """
          |type SomeClass struct {
          |}
          |
          |func someMethod(someClass SomeClass) {
          |    if true {
          |        return
          |    } else {
          |        return
          |    }
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      // Assert
      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully parse an if-elseif-else statement" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          |  public void someMethod(int value) {
          |     if (value == 1) {
          |         return 1;
          |     } else if (value == 2) {
          |         return 2;
          |     } else if (value == 3) {
          |         return 3;
          |     } else {
          |         return;
          |     }
          |  }
          |}""".stripMargin

      val expectedResult = """
          |type SomeClass struct {
          |}
          |
          |func someMethod(someClass SomeClass, value int) {
          |    if value == 1 {
          |        return 1
          |    } else if value == 2 {
          |        return 2
          |    } else if value == 3 {
          |        return 3
          |    } else {
          |        return
          |    }
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      // Assert
      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully transpile a for loop" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          |  public void someMethod() {
          |     for (int i = 0; i < 10; i++) {
          |         System.out.println(i);
          |     }
          |  }
          |}""".stripMargin

      val expectedResult = """
          |type SomeClass struct {
          |}
          |
          |func someMethod(someClass SomeClass) {
          |    for i int := 0; i < 10; i++ {
          |        fmt.Println(i)
          |    }
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      // Assert
      assert(normalizeCode(fileAsString) == normalizeCode(expectedResult))
    }

    "successfully transpile a while loop" in {
      // Arrange
      val javaClassDeclaration =
        """public class SomeClass {
          |  public void someMethod() {
          |     while (true) {
          |         break;
          |     }
          |  }
          |}""".stripMargin

      val expectedResult = """
          |type SomeClass struct {
          |}
          |
          |func someMethod(someClass SomeClass) {
          |    for true {
          |        break
          |    }
          |}""".stripMargin

      val file = writeFileContents(javaClassDeclaration)
      val fileTranspiler: FileTranspiler = FileTranspiler()

      // Act
      val transpiledFile =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileAsString = readFileContents(transpiledFile)

      // Assert
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
