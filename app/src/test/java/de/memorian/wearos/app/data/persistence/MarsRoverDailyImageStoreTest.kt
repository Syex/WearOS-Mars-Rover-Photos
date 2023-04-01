package de.memorian.wearos.app.data.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import de.memorian.wearos.marsrover.app.data.persistence.MarsRoverDailyImageStore
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MarsRoverDailyImageStoreTest {

    private val dataStore = mockk<DataStore<Preferences>>()
    private val marsRoverDailyImageStore = MarsRoverDailyImageStore(dataStore)

    @Test
    fun `getDailyImage() should return null if no image url is stored`() = runTest {
        every { dataStore.data } returns emptyFlow()

        assertNull(marsRoverDailyImageStore.getDailyImage())
    }

    @Test
    fun `getDailyImage() should return stored image url`() = runTest {
        val testUrl = "http://test.url"
        val preferences = mockk<Preferences> {
            every { this@mockk[stringPreferencesKey("dailyImageUrl")] } returns testUrl
        }
        every { dataStore.data } returns flowOf(preferences)

        assertEquals(testUrl, marsRoverDailyImageStore.getDailyImage())
    }
}