package exceptions

case class FileCreationException(fileName: String)
    extends RuntimeException(s"Failed to create file with name $fileName.")
