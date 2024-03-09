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

    fun getStringsMap(context: Context): Map<String, Strings> {
        val files = context.assets.list("")
            ?.filter { it.matches("^strings_[a-z]{2}.json\$".toRegex()) }
        if (files.isNullOrEmpty()) error("No files!")
        val locales = files.mapNotNull { name ->
                runCatching {
                    context.assets.open(name).use {
                        val json = it.reader().readText()
                        toStrings(JSONObject(json).getJSONObject("data"))
                    }
                }.getOrNull()
            }
        if (locales.isEmpty()) error("No locales!")
        return locales.associateBy { it.language }
    }

    fun getStringsOrNull(
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

    fun getLanguage(
        configuration: Configuration,
        stringsMap: Map<String, Strings>,
        stringsType: StringsType,
        defaultLanguage: String,
    ): String {
        return when (stringsType) {
            StringsType.Auto -> {
                val language = configuration
                    .locales
                    .get(0)
                    ?.language
                if (language == null || !stringsMap.keys.contains(language)) {
                    defaultLanguage
                } else {
                    language
                }
            }
            is StringsType.Locale -> stringsType.language
        }
    }
}
