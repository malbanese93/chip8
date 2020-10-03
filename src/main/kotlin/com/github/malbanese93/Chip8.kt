package com.github.malbanese93

import com.github.malbanese93.hardware.CPU
import com.github.malbanese93.hardware.CPURegisters
import com.github.malbanese93.hardware.Memory
import com.github.malbanese93.utils.Frequency
import com.github.malbanese93.utils.NANOMS_TO_MS
import com.github.malbanese93.utils.S_TO_MS
import com.github.malbanese93.utils.banner
import java.time.Duration
import java.time.Instant
import java.util.logging.Logger

@ExperimentalUnsignedTypes
class Chip8 {
    private val memory = Memory()

    private val cpu = CPU(
        regs = CPURegisters(),
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
        val cpuDeltaTimeMs = S_TO_MS / Frequency.CPU_FREQUENCY.hz
        val audioDeltaTimeMs = S_TO_MS / Frequency.SOUND_FREQUENCY.hz
        val delayDeltaTimeMs = S_TO_MS / Frequency.DELAY_FREQUENCY.hz

        var cpuAccumulator = 0.0
        var soundAccumulator = 0.0
        var delayAccumulator = 0.0

        var oldTime = Instant.now()

        while(true) {
            val newTime = Instant.now()
            val deltaTimeMs = Duration.between(oldTime, newTime).toNanos() / NANOMS_TO_MS
            oldTime = newTime

            cpuAccumulator += deltaTimeMs
            soundAccumulator += deltaTimeMs
            delayAccumulator += deltaTimeMs

            if( cpuAccumulator >= cpuDeltaTimeMs ) {
                cpuAccumulator -= cpuDeltaTimeMs

                cpu.update()
            }

            if( soundAccumulator >= audioDeltaTimeMs ) {
                soundAccumulator -= audioDeltaTimeMs
                println(">> BEEP")
            }

            if( delayAccumulator >= delayDeltaTimeMs ) {
                delayAccumulator -= delayDeltaTimeMs
                println(">>> DELAY")
            }
        }
    }
}