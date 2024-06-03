package base

import org.eclipse.jface.text.Document
import transpilers.goTranspiler.GoTranspiler

import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files

class FileTranspiler {

  def transpile(options: Map[String, String]): Unit = {
    val fileLocation = options.get("file")
    val dirLocation = options.get("dir")

    val result = (fileLocation, dirLocation) match {
      case (Some(_), Some(_)) =>
        throw new IllegalArgumentException(
          "Cannot transpile both a file and a directory at the same time"
        )
      case (Some(filePath), None) =>
        val file = new File(filePath)
        transpileFile(file, options)
      case (None, Some(dirPath)) =>
        val dir = new File(dirPath)
        transpileDir(dir, options)
    }
  }

  /** Transpiles a single file from Java to Scala
    *
    * @param fileLocation - the location of the file to transpile
    */
  def transpileFile(
      fileLocation: File,
      commandLineOptions: Map[String, String]
  ): Unit = {
    // Parse command line options
    val languageName = commandLineOptions.get("language")

    // Read source code from file
    val sourceCode =
      Files.readString(fileLocation.toPath, Charset.forName("UTF-8"))

    // Create a document from the source code
    val document = new Document(sourceCode)

    val transpiler = getTranspiler(languageName, document)

    val transpiledCode = transpiler.transpileCode()

    // Write transpiled code to file
    val transpiledFile =
      Files.writeString(fileLocation.toPath, transpiledCode)

    // Format code
    transpiler.transpilerOptions.formatter.formatCode(transpiledFile)
  }

  /** Transpiles a project directory from Java to Scala
    *
    * @param dirLocation - The location of the projectDirectory to transpile
    */
  def transpileDir(
      projectDirLocation: File,
      commandLineOptions: Map[String, String]
  ): Unit = {
    val files = projectDirLocation.listFiles().toSeq

    files.map {
      case file if file.isDirectory =>
        transpileDir(file, commandLineOptions)
      case file if file.isFile =>
        transpileFile(file, commandLineOptions)
      case _ => None
    }

    ()
  }

  private def getTranspiler(
      language: Option[String],
      document: Document
  ): Transpiler = {
    language match {
      case Some("go") => GoTranspiler(document)
      case _ =>
        throw new IllegalArgumentException(s"Unsupported language: $language")
    }
  }
}
