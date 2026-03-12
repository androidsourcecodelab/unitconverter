package com.androidsourcecodelab.unitconverter.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.androidsourcecodelab.unitconverter.FavoriteConversion
import kotlinx.coroutines.flow.first

class FavoritesRepository(private val context: Context) {

    private val Context.dataStore by preferencesDataStore("favorites")

    private val FAVORITES_KEY = stringSetPreferencesKey("favorites")

    suspend fun saveFavorites(favorites: List<FavoriteConversion>) {

        val set = favorites.map {
            "${it.from}|${it.to}"
        }.toSet()

        context.dataStore.edit { prefs ->
            prefs[FAVORITES_KEY] = set
        }
    }

    suspend fun loadFavorites(): List<FavoriteConversion> {

        val prefs = context.dataStore.data.first()

        val set = prefs[FAVORITES_KEY] ?: emptySet()

        return set.map {

            val parts = it.split("|")

            FavoriteConversion(parts[0], parts[1])

        }
    }
}