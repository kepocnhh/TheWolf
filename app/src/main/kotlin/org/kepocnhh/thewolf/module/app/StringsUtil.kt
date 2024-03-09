package org.kepocnhh.thewolf.module.app

import android.content.Context
import android.content.res.Configuration
import org.json.JSONObject

internal object StringsUtil {
    private fun toStrings(jsonObject: JSONObject): Strings {
        val type = Strings::class.java
        val con = type.constructors.firstOrNull() ?: error("No constructors!")
        val fields = type.declaredFields.filter { !java.lang.reflect.Modifier.isStatic(it.modifiers) }
        val args = fields.map {
            jsonObject.getString(it.name)
        }.toTypedArray()
        return con.newInstance(*args) as Strings
    }

    private fun toStrings(json: String): Strings {
        return toStrings(JSONObject(json).getJSONObject("data"))
    }

    fun getStringsMap(context: Context): Map<String, Strings> {
        val files = context.assets.list("")
            ?.filter { it.matches("^strings_[a-z]{2}.json\$".toRegex()) }
        if (files.isNullOrEmpty()) error("No files!")
        val locales = files.mapNotNull { name ->
                runCatching {
                    context.assets.open(name).use {
                        toStrings(it.reader().readText())
                    }
                }.getOrNull()
            }
        if (locales.isEmpty()) error("No locales!")
        return locales.associateBy { it.language }
    }

    private fun getStrings(
        configuration: Configuration,
        stringsMap: Map<String, Strings>,
    ): Strings? {
        val language = configuration
            .locales
            .get(0)
            ?.language
            ?: return null
        return stringsMap[language]
    }

    private fun Map<String, Strings>.requireStrings(language: String): Strings {
        return get(language) ?: error("No strings by \"$language\"!")
    }

    fun getStrings(
        configuration: Configuration,
        stringsMap: Map<String, Strings>,
        stringsType: StringsType,
        defaultLanguage: String,
    ): Strings {
        return when (stringsType) {
            StringsType.Auto -> {
                getStrings(configuration, stringsMap)
                    ?: stringsMap.requireStrings(defaultLanguage)
            }
            is StringsType.Locale -> {
                stringsMap.requireStrings(stringsType.language)
            }
        }
    }
}
