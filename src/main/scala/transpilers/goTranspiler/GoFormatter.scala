package transpilers.goTranspiler

import base.Formatter

class GoFormatter extends Formatter {

  override val command: Seq[String] = Seq("gofmt", "-w")

}
