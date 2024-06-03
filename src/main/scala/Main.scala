import base.Transpiler
import org.eclipse.jface.text.Document
import transpilers.goTranspiler.GoTranspiler

import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import scala.collection.mutable

object Main {

  final def main(args: Array[String]) = {
    println("Welcome to Java2Many, a Java to many transpiler (obviously...)")

    val usage = """
      Usage: java2scala [--file file] [--projectDir dir]
    """

    if (args.length == 0) println(usage)

    val options = parseArgs(mutable.Map(), args.toList)

    transpile(options)
  }

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

  private def parseArgs(
      map: mutable.Map[String, String],
      list: List[String]
  ): Map[String, String] = {
    list match {
      case Nil => map.toMap
      case "--file" :: value :: tail =>
        parseArgs(map ++ Map("file" -> value), tail)
      case "--dir" :: value :: tail =>
        parseArgs(map ++ Map("dir" -> value), tail)
      case "--language" :: value :: tail =>
        parseArgs(map ++ Map("language" -> value), tail)
      case value :: Nil =>
        parseArgs(map ++ Map("filename" -> value), list.tail)
      case unknown :: _ =>
        throw new IllegalArgumentException(
          s"Failed to parse arguments. Unknown option $unknown"
        )
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

    val transpiledCode = transpiler.transpile()

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
