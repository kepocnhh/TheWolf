package org.kepocnhh.thewolf.module.app

internal sealed interface StringsType {
    data object Auto : StringsType
    data class Locale(val language: String) : StringsType
}
