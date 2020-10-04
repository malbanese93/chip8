package com.github.malbanese93

import com.github.malbanese93.hardware.*
import com.github.malbanese93.utils.NANOMS_TO_MS
import com.github.malbanese93.utils.banner
import java.time.Duration
import java.time.Instant
import java.util.logging.Logger


class Chip8 {
    private val memory = Memory()
    private val timeAccumulator = TimeAccumulator()
    private val soundGenerator = SoundGenerator()

    private val cpu = CPU(
        regs = CPURegisters(),
        timeAccumulator = timeAccumulator,
        soundGenerator = soundGenerator,
        memory = memory
    )

    companion object {
        val logger: Logger = Logger.getLogger(this::class.java.name)
    }

    fun start() {
        printBanner()

        logger.info("Initializing chip8 system...")
        mainLoop()
    }

    private fun printBanner() = println(banner)

    private fun mainLoop() {
        var oldTime = Instant.now()

        while(true) {
            val newTime = Instant.now()
            val deltaTimeMs = Duration.between(oldTime, newTime).toNanos() / NANOMS_TO_MS
            oldTime = newTime

            timeAccumulator.updateAccumulators(deltaTimeMs)
            timeAccumulator.doOperationAfterDeltaTime(TimeAccumulatorType.CPU_ACCUMULATOR)   { cpu.update() }
            timeAccumulator.doOperationAfterDeltaTime(TimeAccumulatorType.AUDIO_ACCUMULATOR) { cpu.decrementSoundTimer() }
            timeAccumulator.doOperationAfterDeltaTime(TimeAccumulatorType.DELAY_ACCUMULATOR) { cpu.decrementDelayTimer() }
        }
    }
}