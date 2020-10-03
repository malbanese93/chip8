package com.github.malbanese93.hardware

import com.github.malbanese93.UnknownOpcodeException
import com.github.malbanese93.bit.highByte
import com.github.malbanese93.bit.highNibble
import com.github.malbanese93.bit.lowNibble
import com.github.malbanese93.bit.toHexString
import com.github.malbanese93.opcode.OpcodeMnemonic
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
        val opcodeBytes = fetchOpcode()
        val opcodeMnemonic = decode(opcodeBytes)
        execute(opcodeBytes, opcodeMnemonic)
    }

    private fun execute(opcodeBytes : Int, opcodeMnemonic: OpcodeMnemonic) {
        opcodeMnemonic.doOperation(opcodeBytes, this)
        regs.PC += OPCODE_BYTES
    }

    private fun fetchOpcode(): Int {
        val msb = memory.readValue(regs.PC)
        val lsb = memory.readValue(regs.PC + 1)

        return msb.shl(8).or(lsb)
    }

    private fun decode(opcode: Int) : OpcodeMnemonic {
        logger.info("Decoding opcode: <${opcode.toHexString}>")

        val opcodeMnemonic = when(opcode.highByte.highNibble) {
            0x0 -> when (opcode) {
                0x00E0 -> OpcodeMnemonic.CLEAR_DISPLAY
                0x00EE -> OpcodeMnemonic.RETURN_FROM_SUBROUTINE
                else -> OpcodeMnemonic.CALL_MACHINE_CODE
            }
            0x1 -> OpcodeMnemonic.JUMP_TO_NNN
            0x2 -> OpcodeMnemonic.CALL_SUBROUTINE
            0x3 -> OpcodeMnemonic.SKIP_IF_VX_EQ_NN
            0x4 -> OpcodeMnemonic.SKIP_IF_VX_NOT_EQ_NN
            0x5 -> OpcodeMnemonic.SKIP_IF_VX_EQ_VY
            0x6 -> OpcodeMnemonic.SET_VX_TO_NN
            0x7 -> OpcodeMnemonic.SET_VX_TO_VX_PLUS_NN
            0x8 -> when(opcode.lowNibble) {
                0x0 -> OpcodeMnemonic.SET_VX_TO_VY
                0x1 -> OpcodeMnemonic.SET_VX_TO_VX_OR_VY
                0x2 -> OpcodeMnemonic.SET_VX_TO_VX_AND_VY
                0x3 -> OpcodeMnemonic.SET_VX_TO_VX_XOR_VY
                0x4 -> OpcodeMnemonic.SET_VX_TO_VX_PLUS_VY
                0x5 -> OpcodeMnemonic.SET_VX_TO_VX_MINUS_VY
                0x6 -> OpcodeMnemonic.SET_VX_TO_VX_SHR_1
                0x7 -> OpcodeMnemonic.SET_VX_TO_VY_MINUS_VX
                0xE -> OpcodeMnemonic.SET_VX_TO_VX_SHL_1
                else -> throw UnknownOpcodeException(opcode)
            }
            0x9 -> OpcodeMnemonic.SKIP_IF_VX_NOT_EQ_VY
            0xA -> OpcodeMnemonic.SET_I_TO_NNN
            0xB -> OpcodeMnemonic.JUMP_TO_V0_PLUS_NNN
            0xC -> OpcodeMnemonic.SET_VX_TO_RAND_AND_NN
            0xD -> OpcodeMnemonic.DRAW
            0xE -> when(opcode.lowNibble) {
                0x9E -> OpcodeMnemonic.SKIP_IF_KEY_EQ_VX
                0xA1 -> OpcodeMnemonic.SKIP_IF_KEY_NOT_EQ_VX
                else -> throw UnknownOpcodeException(opcode)
            }
            0xF -> when(opcode.lowNibble) {
                0x07 -> OpcodeMnemonic.GET_DELAY
                0x0A -> OpcodeMnemonic.GET_KEY
                0x15 -> OpcodeMnemonic.SET_DELAY_TIMER
                0x18 -> OpcodeMnemonic.SET_SOUND_TIMER
                0x1E -> OpcodeMnemonic.SET_I_TO_I_PLUS_VX
                0x29 -> OpcodeMnemonic.SET_I_TO_SPRITE_LOCATION
                0x33 -> OpcodeMnemonic.BCD
                0x55 -> OpcodeMnemonic.REGISTER_DUMP
                0x65 -> OpcodeMnemonic.REGISTER_LOAD
                else -> throw UnknownOpcodeException(opcode)
            }
            else -> throw UnknownOpcodeException(opcode)
        }

        logger.info("Opcode decoded: $opcodeMnemonic")

        return opcodeMnemonic
    }
}