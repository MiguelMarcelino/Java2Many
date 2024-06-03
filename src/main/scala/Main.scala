import base.{FileTranspiler, Transpiler}
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

    val fileTranspiler = new FileTranspiler()
    fileTranspiler.transpile(options)
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

}
