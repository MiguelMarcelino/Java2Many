package base

import exceptions.FormatException

import java.io.File
import java.util.concurrent.TimeUnit

trait Formatter {

  val command: Seq[String]

  def formatCode(file: File): Unit = {
    // Run the command command
    val processBuilder = ProcessBuilder()

    // Set the command to run
    val commandWithFile = s"${command.mkString(" ")} ${file.getAbsolutePath}"
    processBuilder.command(commandWithFile)

    // Start the process
    val process = processBuilder.start()

    // Wait for the process to finish. I do this synchronously for now, but it might change in the future
    try {
      process.waitFor(30L, TimeUnit.SECONDS)
    } catch {
      case e: InterruptedException =>
        throw FormatException("Timed out while formatting the Go file")
      case e: NullPointerException =>
        throw FormatException("Wrong timeunit was used")
    }

    // Check if the process failed
    if (process.exitValue() != 0)
      throw FormatException("Failed to format the Go file")
  }
}
