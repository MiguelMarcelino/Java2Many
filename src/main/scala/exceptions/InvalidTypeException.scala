package exceptions

case class InvalidTypeException(typeName: String)
    extends RuntimeException(s"Invalid class type name $typeName")
