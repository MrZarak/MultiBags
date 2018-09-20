package zarak.multibags.api

final object Reference {
  final val ID = "multibags"
  final val NAME = "MultiBags"
  final val JEI_DEP = "after:jei@[4.8,);"
  final val ZARAK_DEP = "required-after:zaraklib@[0.0.1);"
  final val DEP: String = JEI_DEP + ZARAK_DEP
}
