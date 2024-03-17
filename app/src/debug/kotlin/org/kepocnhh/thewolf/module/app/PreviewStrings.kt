package org.kepocnhh.thewolf.module.app

internal object PreviewStrings {
    private fun Strings(language: String): Strings {
        val type = Strings::class.java
        val con = type.constructors.firstOrNull() ?: error("No constructors!")
        val fields = type.declaredFields.filter { !java.lang.reflect.Modifier.isStatic(it.modifiers) }
        val args = fields.map {
            if (it.name == "language") language else "${it.name}:$language"
        }.toTypedArray()
        return con.newInstance(*args) as Strings
    }

    val En = Strings(language = "en")
    val Ru = Strings(language = "ru")
}
