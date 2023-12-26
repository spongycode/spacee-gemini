package com.spongycode.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.datastore by preferencesDataStore("settings")
suspend fun DataStore<Preferences>.storeApiKey(key: String) {
    val apiKey = stringPreferencesKey("API_KEY")
    edit { settings ->
        settings[apiKey] = key
    }
}

suspend fun DataStore<Preferences>.getApiKey(): String {
    val apiKey = stringPreferencesKey("API_KEY")
    val preferences = data.first()
    val apiKeyExists = preferences[apiKey] != null
    return if (apiKeyExists) {
        preferences[apiKey] ?: ""
    } else {
        ""
    }
}