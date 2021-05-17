package com.lexo.notepad.db

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lexo.notepad.util.SortOrder
import com.lexo.notepad.util.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


private const val TAG = "PreferenceManger"

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore = context.dataStore

    val preferenceFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferenceKey.SORT_ORDER] ?: SortOrder.ORDER_BY_NAME.name
            )
            FilterPreferences(sortOrder)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit {
            it[PreferenceKey.SORT_ORDER] = sortOrder.name
        }
    }

    private object PreferenceKey {
        val SORT_ORDER = stringPreferencesKey("sort_order")
    }
}

data class FilterPreferences(val sortOrder: SortOrder)