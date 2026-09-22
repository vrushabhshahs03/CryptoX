package com.example.cryptox.widget

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object WidgetPrefs {
    val KEY_LOADING = booleanPreferencesKey("loading")
    val KEY_PORTFOLIO_TOTAL = stringPreferencesKey("portfolio_total")
    val KEY_ERROR = booleanPreferencesKey("error")
}