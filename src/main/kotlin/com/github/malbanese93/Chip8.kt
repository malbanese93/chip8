package com.github.malbanese93

import com.github.malbanese93.hardware.*
import com.github.malbanese93.utils.Frequency
import com.github.malbanese93.utils.NANOMS_TO_MS
import com.github.malbanese93.utils.S_TO_MS
import com.github.malbanese93.utils.banner
import java.time.Duration
import java.time.Instant
import java.util.logging.Logger


class Chip8 {
    private val memory = Memory()

    private val cpu = CPU(
        regs = CPURegisters(),
        memory = memory
    )

    private val timer = Timer()

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

            timer.updateTimers(deltaTimeMs)
            timer.doOperationAfterDeltaTime(TimerType.CPU_ACCUMULATOR) { cpu.update() }
            timer.doOperationAfterDeltaTime(TimerType.AUDIO_ACCUMULATOR) { println(">> BEEP") }
            timer.doOperationAfterDeltaTime(TimerType.CPU_ACCUMULATOR) { println(">>> DELAY") }
        }
    }
}