package com.androidsourcecodelab.unitconverter.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.androidsourcecodelab.unitconverter.data.UnitRepository
import com.androidsourcecodelab.unitconverter.model.UnitCategory
import org.json.JSONArray
import org.json.JSONObject

object CategoryManager {

    private const val PREF_NAME = "category_prefs"
    private const val KEY_VISIBLE = "visible_categories"
    private const val MAX_VISIBLE = 15

    private lateinit var prefs: SharedPreferences

    // LRU list (oldest → newest by lastUsed)
    private val visibleCategories = mutableListOf<CategoryUsage>()

    data class CategoryUsage(
        val name: String,
        var lastUsed: Long
    )

    // ---------------------------
    // INIT (call once at app start)
    // ---------------------------
    fun init(context: Context) {

        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val saved = prefs.getString(KEY_VISIBLE, null)

        if (saved.isNullOrEmpty()) {
            loadDefaults()
        } else {
            load(saved)
        }

        logState("INIT")
    }

    // ---------------------------
    // DEFAULT LOAD
    // ---------------------------
    private fun loadDefaults() {
        visibleCategories.clear()

        val now = System.currentTimeMillis()

        UnitRepository.defaultCategories
            .take(MAX_VISIBLE)
            .forEach {
                visibleCategories.add(CategoryUsage(it.name, now))
            }

        save()
    }

    // ---------------------------
    // PUBLIC API
    // ---------------------------
    fun getVisibleCategories(): List<UnitCategory> {
        return visibleCategories.mapNotNull {
            UnitRepository.getCategoryByName(it.name)
        }
    }

    // Updates LRU cache for visibleCategories.
    // Caller MUST refresh state (e.g., getVisibleCategories()) to reflect changes in UI.
    fun onCategoryUsed(category: UnitCategory) {

        val now = System.currentTimeMillis()

        val existing = visibleCategories.find { it.name == category.name }

        if (existing != null) {
            existing.lastUsed = now
            save()
            logState("UPDATED ${category.name}")
            return
        }

        // New category → may need eviction
        if (visibleCategories.size >= MAX_VISIBLE) {
            evictLRU()
        }

        visibleCategories.add(CategoryUsage(category.name, now))

        save()
        logState("ADDED ${category.name}")
    }

    // ---------------------------
    // LRU EVICTION
    // ---------------------------
    private fun evictLRU() {
        val lru = visibleCategories.minByOrNull { it.lastUsed }

        lru?.let {
            visibleCategories.remove(it)
            Log.d("LRU", "Evicted: ${it.name}")
        }
    }

    // ---------------------------
    // PERSISTENCE
    // ---------------------------
    private fun save() {
        val array = JSONArray()

        visibleCategories.forEach {
            val obj = JSONObject()
            obj.put("name", it.name)
            obj.put("lastUsed", it.lastUsed)
            array.put(obj)
        }

        prefs.edit().putString(KEY_VISIBLE, array.toString()).apply()
    }

    private fun load(json: String) {
        visibleCategories.clear()

        val array = JSONArray(json)

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            visibleCategories.add(
                CategoryUsage(
                    name = obj.getString("name"),
                    lastUsed = obj.getLong("lastUsed")
                )
            )
        }
    }

    // ---------------------------
    // DEBUG (optional)
    // ---------------------------
    private fun logState(tag: String) {
        Log.d("LRU", "$tag → ${visibleCategories.map { it.name }}")
    }
}