package com.example.wordsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// 将要实例化的Preferences Datastore的名称
private const val LAYOUT_PREFERENCES_NAME = "layout_preferences"

// 使用preferencesDataStore委托创建一个DataStore实例
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCES_NAME
)

// 在LetterListFragment中使用
class SettingsDataStore(context: Context) {

    // Boolean值类型的键
    private val IS_LINEAR_LAYOUT_MANAGER = booleanPreferencesKey("is_linear_layout_manager")

    // 写入Preferences DataStore
    suspend fun saveLayoutToPreferencesStore(isLinearLayoutManager: Boolean, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[IS_LINEAR_LAYOUT_MANAGER] = isLinearLayoutManager
        }
    }

    // 从Preferences DataStore读取
    val preferenceFlow: Flow<Boolean> = context.dataStore.data
        .catch {
            if(it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_LINEAR_LAYOUT_MANAGER] ?: true
        }

}