package com.fahadali.oneos.session

import android.content.Context
import android.net.Uri
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

object CustomRomCatalog {
    data class Entry(
        val id: String,
        val name: String,
        val uri: Uri,
        val fileName: String?,
        val bytes: Long?,
    )

    private const val PREFS = "oneos_custom_roms"
    private const val KEY_ENTRIES = "entries"

    @Synchronized
    fun all(context: Context): List<Entry> {
        val raw = context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_ENTRIES, null) ?: return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            buildList {
                for (index in 0 until array.length()) {
                    val item = array.getJSONObject(index)
                    val uri = item.optString("uri").takeIf { it.isNotBlank() }?.let(Uri::parse) ?: continue
                    add(
                        Entry(
                            id = item.getString("id"),
                            name = item.getString("name"),
                            uri = uri,
                            fileName = item.optString("fileName").takeIf { it.isNotBlank() },
                            bytes = item.optLong("bytes", -1L).takeIf { it >= 0L },
                        ),
                    )
                }
            }
        }.getOrDefault(emptyList())
    }

    @Synchronized
    fun add(context: Context, uri: Uri, name: String, fileName: String?, bytes: Long?): Entry {
        val app = context.applicationContext
        val entry = Entry(UUID.randomUUID().toString(), name, uri, fileName, bytes)
        val entries = all(app) + entry
        val array = JSONArray()
        entries.forEach { item ->
            array.put(
                JSONObject()
                    .put("id", item.id)
                    .put("name", item.name)
                    .put("uri", item.uri.toString())
                    .put("fileName", item.fileName)
                    .put("bytes", item.bytes ?: -1L),
            )
        }
        app.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_ENTRIES, array.toString())
            .apply()
        return entry
    }

    @Synchronized
    fun remove(context: Context, id: String) {
        val app = context.applicationContext
        val array = JSONArray()
        all(app).filterNot { it.id == id }.forEach { item ->
            array.put(
                JSONObject()
                    .put("id", item.id)
                    .put("name", item.name)
                    .put("uri", item.uri.toString())
                    .put("fileName", item.fileName)
                    .put("bytes", item.bytes ?: -1L),
            )
        }
        app.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_ENTRIES, array.toString())
            .apply()
    }
}
