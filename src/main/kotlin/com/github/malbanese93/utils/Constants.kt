package com.github.malbanese93.utils

// Conversion constants
const val NANOMS_TO_MS = 1_000_000.0
const val S_TO_MS = 1_000.0

// Frequency (op/s)
enum class Frequency(val hz : Double) {
    CPU_FREQUENCY(2.0),
    DELAY_FREQUENCY(.5),
    SOUND_FREQUENCY(.5)
}