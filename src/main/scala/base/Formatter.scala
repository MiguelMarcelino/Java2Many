package base

import exceptions.FormatException

import java.nio.file.Path
import java.util.concurrent.TimeUnit
import scala.util.{Failure, Try}

trait Formatter {

  val command: Seq[String]

  def formatCode(filePath: Path): Unit = {
    // Run the command command
    val processBuilder = ProcessBuilder()

    // Set the command to run
    val commandWithFile = s"${command.mkString(" ")} ${filePath.getFileName}"
    processBuilder.command(commandWithFile)

    // Start the process
    val process = processBuilder.start()

    // Wait for the process to finish. I do this synchronously for now, but it might change in the future
    Try {
      process.waitFor(30, TimeUnit.SECONDS)
    } match {
      case Failure(_: InterruptedException) =>
        throw FormatException("Failed to format the Go file")
      case Failure(_: NullPointerException) =>
        throw FormatException("Wrong timeunit was used")
      case _ => ()
    }

    // Check if the process failed
    if (process.exitValue() != 0)
      throw FormatException("Failed to format the Go file")
  }
}
