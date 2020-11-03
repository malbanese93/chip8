package com.github.malbanese93.chip8

import com.github.malbanese93.utils.NANOMS_TO_MS
import com.github.malbanese93.utils.banner
import java.lang.Exception
import java.time.Duration
import java.time.Instant
import java.util.logging.Logger


class Chip8 {
    private val memory = Memory()
    private val timeAccumulator = TimeAccumulator()
    private val soundGenerator = SoundGenerator()
    private val stack = CPURoutineStack()
    private val videoBuffer = VideoBuffer()

    private val cpu = CPU(
        regs = CPURegisters(),
        soundGenerator = soundGenerator,
        memory = memory,
        stack = stack,
        videoBuffer = videoBuffer
    )

    companion object {
        val logger: Logger = Logger.getLogger(this::class.java.name)
    }

    fun start(romContents : ByteArray) {
        printBanner()

        logger.info("Initializing chip8 system...")
        resetComponents()

        memory.loadRom(romContents)
        memory.dumpMemory()

        mainLoop()
    }

    private fun printBanner() = println(banner)

    private fun resetComponents() {
        memory.reset()
        timeAccumulator.reset()
        soundGenerator.reset()
        stack.reset()
        videoBuffer.reset()
    }

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