package exceptions

case class FormatException(fileName: String)
    extends RuntimeException(s"Formatting failed for file with name $fileName.")
