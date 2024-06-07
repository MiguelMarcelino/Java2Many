package base

import exceptions.FileCreationException
import org.eclipse.jface.text.Document
import transpilers.goTranspiler.GoTranspiler

import java.io.File
import java.nio.charset.Charset
import java.nio.file.{Files, Path}

class FileTranspiler {

  /** Transpiles a file or a directory from Java to Scala
    *
    * @param options the options to use when transpiling
    *                - file: the location of the file to transpile
    *                - dir: the location of the directory to transpile
    *                - targetDir: the location of the directory to put the transpiled files
    *                - language: the language of the source code
    * @return the transpiled file or directory
    */
  def transpile(options: Map[String, String]): File = {
    val fileLocation = options.get("file").map(File(_))
    val dirLocation = options.get("dir").map(File(_))
    val targetDir = options.get("targetDir").map(File(_))

    (fileLocation, dirLocation) match {
      case (Some(_), Some(_)) =>
        throw new IllegalArgumentException(
          "Cannot transpile both a file and a directory at the same time"
        )
      case (Some(file), None) =>
        transpileFile(file, targetDir, options)
      case (None, Some(dir)) =>
        transpileDir(
          projectDirLocation = dir,
          targetDir = targetDir,
          commandLineOptions = options,
          isChildDir = false
        )
      case _ =>
        throw new IllegalArgumentException(
          "Please provide either a file or a directory to transpile"
        )
    }
  }

  /** Transpiles a single file from Java to Scala
    *
    * @param fileLocation - the location of the file to transpile
    * @return the transpiled file
    */
  def transpileFile(
      fileLocation: File,
      targetDir: Option[File],
      commandLineOptions: Map[String, String]
  ): File = {
    // Parse command line options
    val languageName = commandLineOptions.get("language")

    // Read source code from file
    val sourceCode =
      Files.readString(fileLocation.toPath, Charset.forName("UTF-8"))

    // Create a document from the source code
    val document = new Document(sourceCode)

    val transpiler = getTranspiler(languageName, document)
    val transpiledCode = transpiler.transpileCode()

    // Get the name for the new file
    val languageExtension = transpiler.transpilerOptions.extension
    val existingFileName = fileLocation.getName
      .split('.')
      .headOption
    val newFileName = existingFileName.headOption match {
      case Some(name) => s"$name.$languageExtension"
      case None       => s"transpiled.$languageExtension"
    }

    // Write transpiled code to file
    val transpiledFile = targetDir match {
      case Some(target) =>
        val targetFile = new File(target, newFileName)
        if (!target.exists()) {
          target.mkdirs()
        }

        println("=============")
        println(targetFile)
        println(targetFile.toPath)
        println("=============")

        writeToFile(targetFile, transpiledCode)
      case None =>
        val parent = fileLocation.getParentFile.getPath
        val targetFile = new File(parent, newFileName)
        writeToFile(targetFile, transpiledCode)
    }

    // Format code
    transpiler.transpilerOptions.formatter.formatCode(transpiledFile)

    transpiledFile.toFile
  }

  private def writeToFile(
      targetFile: File,
      content: String
  ): Path = {
    targetFile.exists() match {
      case true =>
        // If the file already exists, we want to overwrite its current content
        Files.writeString(targetFile.toPath, content)
      case false =>
        // If the file does not exist, we want to create a new file and write the content to it
        targetFile.createNewFile()
        Files.writeString(targetFile.toPath, content)
    }
  }

  /** Transpiles a project directory from Java to Scala
    *
    * @param projectDirLocation - the location of the project directory to transpile
    * @param targetDir - the location of the directory to put the transpiled files
    * @param commandLineOptions - the options to use when transpiling
    * @param isChildDir - whether the directory is a child directory
    */
  def transpileDir(
      projectDirLocation: File,
      targetDir: Option[File],
      commandLineOptions: Map[String, String],
      isChildDir: Boolean
  ): File = {
    val files = projectDirLocation.listFiles().toSeq

    val target = (targetDir, isChildDir) match {
      case (Some(t), _)  => t
      case (None, true)  => projectDirLocation
      case (None, false) =>
        // We want to put all the files in a new directory with the suffix '_transpiled'
        // to not have all files in the same folder
        new File(
          projectDirLocation.getParentFile,
          s"${projectDirLocation.getName}_transpiled"
        )
    }

    files.map {
      case file if file.isDirectory =>
        transpileDir(file, Some(target), commandLineOptions, isChildDir = true)
      case file if file.isFile =>
        transpileFile(
          file,
          Some(target),
          commandLineOptions
        )
      case _ => None
    }

    target
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
