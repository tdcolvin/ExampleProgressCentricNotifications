package com.tdcolvin.exampleprogresscentricnotifications

import android.os.SystemClock
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CustomScarecrowOrderProgress(
    val state: CustomScarecrowOrderProgressState,
    val percentComplete: Int
)

enum class CustomScarecrowOrderProgressState {
    ReceivingOrder,
    PickingClothes,
    Stuffing,
    AddingAccessories,
    DrawingFace,
    Complete
}

class CustomScarecrowOrder {
    private val _progress: MutableStateFlow<CustomScarecrowOrderProgress> = MutableStateFlow(
        CustomScarecrowOrderProgress(
            state = CustomScarecrowOrderProgressState.ReceivingOrder,
            percentComplete = 0
        )
    )
    val progress = _progress.asStateFlow()

    fun startOrder() {
        GlobalScope.launch {
            simulateProgressInState(
                state = CustomScarecrowOrderProgressState.ReceivingOrder,
                totalTimeMs = 10000,
                percentCompleteFrom = 0,
                percentCompleteTo = 10
            )

            simulateProgressInState(
                state = CustomScarecrowOrderProgressState.PickingClothes,
                totalTimeMs = 3000,
                percentCompleteFrom = 10,
                percentCompleteTo = 30
            )

            simulateProgressInState(
                state = CustomScarecrowOrderProgressState.Stuffing,
                totalTimeMs = 3000,
                percentCompleteFrom = 30,
                percentCompleteTo = 50
            )

            simulateProgressInState(
                state = CustomScarecrowOrderProgressState.AddingAccessories,
                totalTimeMs = 3000,
                percentCompleteFrom = 50,
                percentCompleteTo = 80
            )

            simulateProgressInState(
                state = CustomScarecrowOrderProgressState.DrawingFace,
                totalTimeMs = 3000,
                percentCompleteFrom = 80,
                percentCompleteTo = 100
            )

            _progress.value = CustomScarecrowOrderProgress(
                CustomScarecrowOrderProgressState.Complete,
                100
            )
        }
    }

    private suspend fun simulateProgressInState(
        state: CustomScarecrowOrderProgressState,
        totalTimeMs: Int,
        percentCompleteFrom: Int,
        percentCompleteTo: Int
    ) {
        val startTime = SystemClock.elapsedRealtime()
        val endTime = startTime + totalTimeMs

        while(SystemClock.elapsedRealtime() < endTime) {
            val stateFractionComplete = ((SystemClock.elapsedRealtime() - startTime) / totalTimeMs.toFloat())
            
            _progress.value = CustomScarecrowOrderProgress(
                state = state,
                percentComplete = (percentCompleteFrom + stateFractionComplete * (percentCompleteTo - percentCompleteFrom)).toInt()
            )

            delay(50)
        }
    }
}