package de.memorian.wearos.marsrover

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import de.memorian.wearos.marsrover.app.data.persistence.SettingsStore
import de.memorian.wearos.marsrover.tile.MarsRoverTileService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MarsRoverApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var settingsStore: SettingsStore

    private val coroutineScope = MainScope()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        schedulePeriodicImageRefresh()
    }

    private fun schedulePeriodicImageRefresh() {
        coroutineScope.launch(Dispatchers.Default) {
            val refreshInterval = settingsStore.getRefreshInterval()
            MarsRoverTileService.schedulePeriodicImageRefresh(
                this@MarsRoverApplication,
                refreshInterval
            )
        }
    }

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
}