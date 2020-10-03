package com.github.malbanese93.hardware

import com.github.malbanese93.bit.highByte
import com.github.malbanese93.bit.highNibble
import com.github.malbanese93.bit.toHexString
import com.github.malbanese93.utils.OPCODE_BYTES
import java.util.logging.Logger


class CPU(
    val regs : CPURegisters,
    val memory: Memory
) {
    companion object {
        val logger: Logger = Logger.getLogger(this::class.java.name)
    }

    fun update() {
        val opcode = fetchOpcode()
        decodeExecuteOpcode(opcode)
    }

    private fun fetchOpcode(): Int {
        val msb = memory.readValue(regs.PC)
        val lsb = memory.readValue(regs.PC + 1)

        return msb.shl(8).or(lsb)
    }

    private fun decodeExecuteOpcode(opcode: Int) {
        logger.info("Executing opcode: <${opcode.toHexString}>")

        when(opcode.highByte.highNibble) {
            0 -> println("Starts with zero")
            1 -> println("Starts with 1")
            else -> println ("X")
        }

        // Fixed-length opcodes
        regs.PC += OPCODE_BYTES
    }
}