package com.techullurgy.howzapp.core.datastore

import androidx.datastore.preferences.core.MutablePreferences

class EditableAppPreferences internal constructor(
    internal val mutablePreferences: MutablePreferences
) : AppPreferences() {

    override fun <T> contains(key: Key<T>): Boolean {
        return mutablePreferences.contains(key.key)
    }

    override fun <T> get(key: Key<T>): T? {
        return mutablePreferences[key.key]
    }

    operator fun <T> set(key: Key<T>, value: T) {
        mutablePreferences[key.key] = value
    }

    fun <T> remove(key: Key<T>): T {
        return mutablePreferences.remove(key.key)
    }
}