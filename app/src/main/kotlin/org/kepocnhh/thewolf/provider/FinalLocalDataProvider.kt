package org.kepocnhh.thewolf.provider

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.StringsType
import org.kepocnhh.thewolf.module.app.ThemeState
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

internal class FinalLocalDataProvider(
    context: Context,
    themeState: ThemeState,
) : LocalDataProvider {
    private val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    init {
        if (preferences.getString("themeState", null) == null) {
            preferences
                .edit()
                .putString("themeState", themeState.toJSONObject().toString())
                .commit()
        }
    }

    override var themeState: ThemeState
        get() {
            val json = preferences
                .getString("themeState", null)
                ?: error("No theme state!")
            return JSONObject(json).toThemeState()
        }
        set(value) {
            preferences
                .edit()
                .putString("themeState", value.toJSONObject().toString())
                .commit()
        }

    override var tasks: List<Task>
        get() {
            val json = preferences
                .getString("tasks", null)
                ?: return emptyList()
            val array = JSONArray(json)
            return (0 until array.length()).map { index ->
                array.getJSONObject(index).toTask()
            }
        }
        set(value) {
            val array = JSONArray()
            value.forEach {
                array.put(it.toJSONObject())
            }
            preferences
                .edit()
                .putString("tasks", array.toString())
                .commit()
        }

    companion object {
        private fun ThemeState.toJSONObject(): JSONObject {
            val stringsType = when (stringsType) {
                StringsType.Auto -> "auto"
                is StringsType.Locale -> stringsType.language
            }
            return JSONObject()
                .put("colorsType", colorsType.name)
                .put("stringsType", stringsType)
        }

        private fun JSONObject.toThemeState(): ThemeState {
            val colorsType = getString("colorsType").let { name ->
                ColorsType.entries.firstOrNull { it.name == name }
            } ?: error("No colors type!")
            val stringsType = when (val language = getString("stringsType")) {
                "auto" -> StringsType.Auto
                else -> StringsType.Locale(language = language)
            }
            return ThemeState(
                colorsType = colorsType,
                stringsType = stringsType,
            )
        }

        private fun Task.toJSONObject(): JSONObject {
            return JSONObject()
                .put("id", id.toString())
                .put("title", title)
                .put("created", created.inWholeMilliseconds)
                .put("repeated", repeated.joinToString(separator = ""))
        }

        private fun JSONObject.toTask(): Task {
            return Task(
                id = UUID.fromString(getString("id")),
                title = getString("title"),
                created = getLong("created").milliseconds,
                repeated = optString("repeated").map { "$it".toInt() }.toSet(),
            )
        }
    }
}
