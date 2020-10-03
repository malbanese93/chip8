package com.github.malbanese93.hardware

import com.github.malbanese93.utils.START_PC


class CPURegisters {
    companion object {
        const val V_REGS = 16
    }

    val V : IntArray = IntArray(V_REGS)
    var I : Int = 0
    var PC : Int = START_PC
}