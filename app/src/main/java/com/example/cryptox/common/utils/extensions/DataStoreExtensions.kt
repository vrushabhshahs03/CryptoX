package com.example.cryptox.common.utils.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.cryptox.common.Constants

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.USER_PREFERENCES_NAME
)