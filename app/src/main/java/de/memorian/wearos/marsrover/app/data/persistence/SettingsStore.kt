package de.memorian.wearos.marsrover.app.data.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import de.memorian.wearos.marsrover.tile.MarsRoverTileService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val KEY_REFRESH_INTERVAL = "refreshInterval"

private const val DEFAULT_REFRESH_INTERVAL = 24

class SettingsStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @ApplicationContext private val context: Context,
) {

    private val refreshIntervalKey = intPreferencesKey(KEY_REFRESH_INTERVAL)

    suspend fun getRefreshInterval(): Int =
        dataStore.data.map { preferences -> preferences[refreshIntervalKey] }.firstOrNull()
            ?: DEFAULT_REFRESH_INTERVAL

    suspend fun storeNewRefreshInterval(newInterval: Int) {
        MarsRoverTileService.schedulePeriodicImageRefresh(context, newInterval)

        dataStore.edit { preferences ->
            preferences[refreshIntervalKey] = newInterval
        }
    }
}