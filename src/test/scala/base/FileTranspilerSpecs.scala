package base

import org.scalatest.wordspec.AnyWordSpec

import java.io.File
import java.nio.file.Files

class FileTranspilerSpecs extends AnyWordSpec {

  val commandLineOptions: Map[String, String] =
    Map("language" -> "go", "file" -> "temp.java", "targetDir" -> "temp")

  "transpile" should {
    "correctly transpile a method declaration" in {
      // Arrange
      val javaMethodDeclaration =
        """public static void someMethod(String value) {
          |}""".stripMargin
      // Create a temporary file to write the contents to
      val file: File = Files.createTempFile("temp", ".java").toFile
      Files.write(file.toPath, javaMethodDeclaration.getBytes())

      val fileTranspiler: FileTranspiler = FileTranspiler()
      val targetDir: File = Files.createTempDirectory("temp").toFile

      // Act
      val result =
        fileTranspiler.transpileFile(file, Some(targetDir), commandLineOptions)

      val fileContents = Files.readAllLines(file.toPath).toArray()
      val fileAsString = fileContents.mkString("\n")

      assert(
        fileAsString ==
          """func someMethod(value string) {
            |}""".stripMargin
      )
    }
  }
}
