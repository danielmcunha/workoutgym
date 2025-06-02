package com.southapps.data.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.southapps.domain.user.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import okio.IOException

object UserPreferencesKeys {
    val USER_SESSION_KEY = stringPreferencesKey("user_session")
}

suspend fun DataStore<Preferences>.saveUserSession(user: User) {
    this.edit { preferences ->
        val userJson = Json.encodeToString(User.serializer(), user)
        preferences[UserPreferencesKeys.USER_SESSION_KEY] = userJson
    }
}

fun DataStore<Preferences>.getUserSession(): Flow<User?> {
    return data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UserPreferencesKeys.USER_SESSION_KEY]?.let { userJson ->
                try {
                    Json.decodeFromString(User.serializer(), userJson)
                } catch (e: Exception) {
                    null
                }
            }
        }
}

suspend fun DataStore<Preferences>.clearUserSession() {
    this.edit { preferences ->
        preferences.remove(UserPreferencesKeys.USER_SESSION_KEY)
    }
}