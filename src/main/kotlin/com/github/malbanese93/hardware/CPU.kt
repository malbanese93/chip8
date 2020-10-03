package com.github.malbanese93.hardware

import java.util.logging.Logger

@ExperimentalUnsignedTypes
class CPU(
    val regs : CPURegisters,
    val memory: Memory
) {
    companion object {
        val logger: Logger = Logger.getLogger(this::class.java.name)
    }

    fun update() {
        val opcode = fetchOpcode()
        executeOpcode(opcode)
    }

    private fun fetchOpcode(): UShort {
        return 0xABCD.toUShort()
    }

    private fun executeOpcode(opcode: UShort) {
        logger.info("Executing opcode: <${opcode.toString(16)}>")
    }
}