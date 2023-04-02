package de.memorian.wearos.marsrover.app.presentation.settings

import de.memorian.wearos.marsrover.app.data.persistence.SettingsStore
import de.memorian.wearos.marsrover.app.presentation.settings.RefreshIntervalScreenState.ShowPicker
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RefreshIntervalViewModelTest {

    private val testScope = TestScope()
    private val mockSettingsStore = mockk<SettingsStore>(relaxUnitFun = true)

    private val viewModel = RefreshIntervalViewModel(mockSettingsStore, testScope)

    @Test
    fun `loadRefreshInterval emits ShowPicker when store returns a valid value`() =
        testScope.runTest {
            val expectedRefreshInterval = 60
            coEvery { mockSettingsStore.getRefreshInterval() } returns expectedRefreshInterval

            viewModel.loadRefreshInterval()

            val actualScreenState = viewModel.screenStateFlow.first { it is ShowPicker }
            actualScreenState as ShowPicker
            actualScreenState.initialSelection shouldBe expectedRefreshInterval
        }

    @Test
    fun `onNewIntervalConfirmed calls storeNewRefreshInterval with the correct value`() =
        testScope.runTest {
            val expectedNewInterval = 120

            viewModel.onNewIntervalConfirmed(expectedNewInterval)
            testScope.runCurrent()

            coVerify { mockSettingsStore.storeNewRefreshInterval(expectedNewInterval) }
        }
}