package com.github.malbanese93.chip8

import com.github.malbanese93.bit.toHexString
import com.github.malbanese93.utils.START_PC
import java.util.logging.Logger


class CPURegisters {
    companion object {
        const val V_REGS = 16
        val logger: Logger = Logger.getLogger(this::class.java.name)
    }

    fun logValues() {
        logger.info("""
            == REGS SUMMARY ==
            [PC]: ${PC.toHexString}
             [I]: ${I.toHexString}
            [DT]: ${DT.toHexString}
            [ST]: ${ST.toHexString}
             [V]: ${V.mapIndexed { index, i -> "[V${index.toString(16).toUpperCase()}]: ${i.toHexString}"} }
        """.trimIndent())
    }

    val V : IntArray = IntArray(V_REGS)
    var I : Int = 0
    var PC : Int = START_PC
    var DT : Int = 0 // Delay Timer
    var ST : Int = 10 // Sound Timer
}