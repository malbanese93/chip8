package com.github.malbanese93.chip8

import com.github.malbanese93.exceptions.UnknownOpcodeException
import com.github.malbanese93.extensions.*
import com.github.malbanese93.opcode.OpcodeMnemonic
import java.util.logging.Logger

class CPU(
    val regs : CPURegisters,
    val stack : CPURoutineStack,
    val videoBuffer: VideoBuffer,
    val soundGenerator: SoundGenerator,
    val memory: Memory
) {
    companion object {
        val logger: Logger = Logger.getLogger(this::class.java.name)
    }

    fun update() {
        val opcodeBytes = fetchOpcode()
        val opcodeMnemonic = decode(opcodeBytes)
        opcodeMnemonic.doOperation(opcodeBytes, this)

        soundGenerator.doSound()

        regs.logValues()
    }

    fun decrementDelayTimer() {
        if ( regs.DT > 0 )
            regs.DT--
    }

    fun decrementSoundTimer() {
        if( regs.ST > 0 )
            regs.ST--

        if( regs.ST == 0 )
            soundGenerator.isOn = false
    }

    private fun fetchOpcode(): Int {
        val msb = memory[regs.PC]
        val lsb = memory[regs.PC+1]

        return msb.combineWithByte(lsb)
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
            0xE -> when(opcode.lowByte) {
                0x9E -> OpcodeMnemonic.SKIP_IF_KEY_EQ_VX
                0xA1 -> OpcodeMnemonic.SKIP_IF_KEY_NOT_EQ_VX
                else -> throw UnknownOpcodeException(opcode)
            }
            0xF -> when(opcode.lowByte) {
                0x07 -> OpcodeMnemonic.SET_VX_TO_DELAY_TIMER
                0x0A -> OpcodeMnemonic.GET_KEY
                0x15 -> OpcodeMnemonic.SET_DELAY_TIMER_TO_VX
                0x18 -> OpcodeMnemonic.SET_SOUND_TIMER_TO_VX
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