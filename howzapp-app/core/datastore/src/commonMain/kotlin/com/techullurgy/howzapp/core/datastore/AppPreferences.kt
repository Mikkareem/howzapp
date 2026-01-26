package com.techullurgy.howzapp.core.datastore

import androidx.datastore.preferences.core.Preferences

abstract class AppPreferences {
    class Key<T> internal constructor(
        internal val key: Preferences.Key<T>
    )

    abstract operator fun <T> contains(key: Key<T>): Boolean

    abstract operator fun <T> get(key: Key<T>): T?
}