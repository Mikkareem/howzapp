package com.techullurgy.howzapp.core.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

fun stringAppPreferencesKey(name: String): AppPreferences.Key<String> {
    return AppPreferences.Key(stringPreferencesKey(name))
}

fun longAppPreferencesKey(name: String): AppPreferences.Key<Long> {
    return AppPreferences.Key(longPreferencesKey(name))
}

internal fun Preferences.toAppPreferences(): AppPreferences =
    EditableAppPreferences(this.toMutablePreferences())