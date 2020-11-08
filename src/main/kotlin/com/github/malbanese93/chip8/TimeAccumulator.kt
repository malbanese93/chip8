package com.github.malbanese93.chip8

import com.github.malbanese93.utils.Frequency
import com.github.malbanese93.utils.S_TO_MS
import java.util.*

enum class TimeAccumulatorType(val deltaTimeMs: Double) {
    CPU_ACCUMULATOR(S_TO_MS / Frequency.CPU_FREQUENCY.hz),
    AUDIO_ACCUMULATOR(S_TO_MS / Frequency.SOUND_FREQUENCY.hz),
    DELAY_ACCUMULATOR(S_TO_MS / Frequency.DELAY_FREQUENCY.hz)
}

class TimeAccumulator {
    private val accumulators : EnumMap<TimeAccumulatorType, Double> = EnumMap(
        mapOf(
            TimeAccumulatorType.CPU_ACCUMULATOR to 0.0,
            TimeAccumulatorType.AUDIO_ACCUMULATOR to 0.0,
            TimeAccumulatorType.DELAY_ACCUMULATOR to 0.0
        )
    )

    fun reset() {
        accumulators[TimeAccumulatorType.CPU_ACCUMULATOR] = 0.0
        accumulators[TimeAccumulatorType.AUDIO_ACCUMULATOR] = 0.0
        accumulators[TimeAccumulatorType.DELAY_ACCUMULATOR] = 0.0
    }

    fun updateAccumulators(deltaTimeMs: Double) {
        accumulators[TimeAccumulatorType.CPU_ACCUMULATOR] = accumulators[TimeAccumulatorType.CPU_ACCUMULATOR]!! + deltaTimeMs
        accumulators[TimeAccumulatorType.AUDIO_ACCUMULATOR] = accumulators[TimeAccumulatorType.AUDIO_ACCUMULATOR]!! + deltaTimeMs
        accumulators[TimeAccumulatorType.DELAY_ACCUMULATOR] = accumulators[TimeAccumulatorType.DELAY_ACCUMULATOR]!! + deltaTimeMs
    }

    fun doOperationAfterDeltaTime(
        timeAccumulator : TimeAccumulatorType,
        operation : () -> Unit
    ) {
        val timerAccumulator = accumulators[timeAccumulator]!!

        if(timerAccumulator >= timeAccumulator.deltaTimeMs) {
            accumulators[timeAccumulator] = timerAccumulator - timeAccumulator.deltaTimeMs

            operation()
        }
    }
}