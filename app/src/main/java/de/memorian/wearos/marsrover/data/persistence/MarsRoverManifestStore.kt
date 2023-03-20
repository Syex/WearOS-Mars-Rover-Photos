package de.memorian.wearos.marsrover.data.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val KEY_MANIFEST_CURIOSITY = "curiosityManifest"
private const val KEY_MANIFEST_OPPORTUNITY = "opportunityManifest"
private const val KEY_MANIFEST_SPIRIT = "spiritManifest"

class MarsRoverManifestStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    private val curiosityKey = stringPreferencesKey(KEY_MANIFEST_CURIOSITY)
    private val opportunityKey = stringPreferencesKey(KEY_MANIFEST_OPPORTUNITY)
    private val spiritKey = stringPreferencesKey(KEY_MANIFEST_SPIRIT)

    suspend fun getCuriosityManifest(): String? =
        dataStore.data.map { preferences -> preferences[curiosityKey] }.first()

    suspend fun getOpportunityManifest(): String? =
        dataStore.data.map { preferences -> preferences[opportunityKey] }.first()

    suspend fun getSpiritManifest(): String? =
        dataStore.data.map { preferences -> preferences[spiritKey] }.first()

    suspend fun storeCuriosityManifest(manifest: String) {
        dataStore.edit { preferences ->
            preferences[curiosityKey] = manifest
        }
    }

    suspend fun storeOpportunityManifest(manifest: String) {
        dataStore.edit { preferences ->
            preferences[opportunityKey] = manifest
        }
    }

    suspend fun storeSpiritManifest(manifest: String) {
        dataStore.edit { preferences ->
            preferences[spiritKey] = manifest
        }
    }
}