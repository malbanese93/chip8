package com.github.malbanese93.hardware

import com.github.malbanese93.utils.START_PC

@ExperimentalUnsignedTypes
class CPURegisters {
    companion object {
        const val V_REGS = 16
    }

    val V : UByteArray = UByteArray(V_REGS)
    val I : UByte = 0.toUByte()
    val PC : UByte = START_PC
}