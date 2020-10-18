package com.github.malbanese93.utils

// Conversion constants
const val NANOMS_TO_MS = 1_000_000.0
const val S_TO_MS = 1_000.0

// Frequency (op/s)
enum class Frequency(val hz : Double) {
    CPU_FREQUENCY(1000.0),
    DELAY_FREQUENCY(60.0),
    SOUND_FREQUENCY(60.0)
}

// Address in memory
const val START_PC = 0x200

// OpcodeMnemonic
const val OPCODE_BYTES = 2