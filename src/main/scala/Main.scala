import base.FileTranspiler

import scala.collection.mutable

object Main {

  final def main(args: Array[String]): Unit = {
    println("Welcome to Java2Many, a Java to many transpiler (obviously...)")

    val options = parseArgs(mutable.Map(), args.toList)

    validateArgs(options)

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

  private def validateArgs(value: Map[String, String]): Unit = {
    val usage =
      "Usage: java2scala [--file file] [--projectDir dir] [--language dir] [--target dir]"

    if (!value.contains("file") && !value.contains("dir")) {
      throw new IllegalArgumentException(
        s"Failed to parse arguments. One of --file or --dir options have to be specified.\n$usage"
      )
    }
    if (!value.contains("language")) {
      throw new IllegalArgumentException(
        s"Failed to parse arguments. Missing --language option.\n$usage"
      )
    }
  }

}
