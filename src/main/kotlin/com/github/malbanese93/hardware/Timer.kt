package com.github.malbanese93.hardware

import com.github.malbanese93.utils.Frequency
import com.github.malbanese93.utils.S_TO_MS
import java.util.*

enum class TimerType(val deltaTimeMs: Double) {
    CPU_ACCUMULATOR(S_TO_MS / Frequency.CPU_FREQUENCY.hz),
    AUDIO_ACCUMULATOR(S_TO_MS / Frequency.SOUND_FREQUENCY.hz),
    DELAY_ACCUMULATOR(S_TO_MS / Frequency.DELAY_FREQUENCY.hz)
}

class Timer {
    val accumulators : EnumMap<TimerType, Double> = EnumMap(
        mapOf(
            TimerType.CPU_ACCUMULATOR to 0.0,
            TimerType.AUDIO_ACCUMULATOR to 0.0,
            TimerType.DELAY_ACCUMULATOR to 0.0
        )
    )

    fun updateTimers(deltaTimeMs: Double) {
        accumulators[TimerType.CPU_ACCUMULATOR] = accumulators[TimerType.CPU_ACCUMULATOR]!! + deltaTimeMs
        accumulators[TimerType.AUDIO_ACCUMULATOR] = accumulators[TimerType.AUDIO_ACCUMULATOR]!! + deltaTimeMs
        accumulators[TimerType.DELAY_ACCUMULATOR] = accumulators[TimerType.DELAY_ACCUMULATOR]!! + deltaTimeMs
    }

    fun doOperationAfterDeltaTime(
        timer : TimerType,
        operation : () -> Unit
    ) {
        val timerAccumulator = accumulators[timer]!!

        if(timerAccumulator >= timer.deltaTimeMs) {
            accumulators[timer] = timerAccumulator - timer.deltaTimeMs

            operation()
        }
    }
}