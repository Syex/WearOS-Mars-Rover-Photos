package de.memorian.wearos.marsrover.data.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val KEY_DAILY_IMAGE_URL = "dailyImageUrl"

class MarsRoverDailyImageStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    private val dailyImageUrlKey = stringPreferencesKey(KEY_DAILY_IMAGE_URL)

    suspend fun getDailyImage(): String? =
        dataStore.data.map { preferences -> preferences[dailyImageUrlKey] }.first()

    suspend fun storeDailyImageUrl(dailyImageUrl: String) {
        dataStore.edit { preferences ->
            preferences[dailyImageUrlKey] = dailyImageUrl
        }
    }
}