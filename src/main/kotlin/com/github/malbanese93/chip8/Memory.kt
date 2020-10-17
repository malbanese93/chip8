package com.github.malbanese93.chip8

import com.github.malbanese93.extensions.lowByte
import com.github.malbanese93.extensions.toByteHexString
import com.github.malbanese93.extensions.toHexString
import com.github.malbanese93.utils.OutOfRAMException
import com.github.malbanese93.utils.START_PC
import com.github.malbanese93.utils.ValueExceedingByteException


class Memory {
    companion object {
        const val SIZE_IN_BYTES = 4096

        const val FONT_START_ADDRESS = 0
        const val FONT_SIZE_IN_BYTES = 5

        val FONT_SET = listOf(
            0xF0, 0x90, 0x90, 0x90, 0xF0,		// 0
            0x20, 0x60, 0x20, 0x20, 0x70,		// 1
            0xF0, 0x10, 0xF0, 0x80, 0xF0,		// 2
            0xF0, 0x10, 0xF0, 0x10, 0xF0,		// 3
            0x90, 0x90, 0xF0, 0x10, 0x10,		// 4
            0xF0, 0x80, 0xF0, 0x10, 0xF0,		// 5
            0xF0, 0x80, 0xF0, 0x90, 0xF0,		// 6
            0xF0, 0x10, 0x20, 0x40, 0x40,		// 7
            0xF0, 0x90, 0xF0, 0x90, 0xF0,		// 8
            0xF0, 0x90, 0xF0, 0x10, 0xF0,		// 9
            0xF0, 0x90, 0xF0, 0x90, 0x90,		// A
            0xE0, 0x90, 0xE0, 0x90, 0xE0,		// B
            0xF0, 0x80, 0x80, 0x80, 0xF0,		// C
            0xE0, 0x90, 0x90, 0x90, 0xE0,		// D
            0xF0, 0x80, 0xF0, 0x80, 0xF0,		// E
            0xF0, 0x80, 0xF0, 0x80, 0x80        // F
        )
    }

    private val _buffer : IntArray = IntArray(SIZE_IN_BYTES)

    init {
        reset()
    }

    fun reset() {
        clearBuffer()
        loadFonts()
    }

    private fun clearBuffer() {
        for (idx in 0 until SIZE_IN_BYTES)
            _buffer[idx] = 0
    }

    private fun loadFonts() {
        FONT_SET.mapIndexed { index, fontData -> _buffer[FONT_START_ADDRESS + index] = fontData }
    }

    fun loadRom(romContents: ByteArray) {
        for (idx in romContents.indices)
            // truncate padding 1 for numbers > 127
            _buffer[idx + START_PC] = romContents[idx].toInt().lowByte
    }

    operator fun get(address: Int) : Int {
        if(address !in 0..SIZE_IN_BYTES) throw OutOfRAMException(address)

        val result = _buffer[address]
        return if (result in 0..0xFF) result else throw ValueExceedingByteException(
            result
        )
    }

    operator fun set(address: Int, value: Int) {
        if(address !in 0..SIZE_IN_BYTES) throw OutOfRAMException(address)
        if (value !in 0..0xFF) throw ValueExceedingByteException(value)

        _buffer[address] = value
    }

    fun dumpMemory() {
        val stepSize = 16
        for(address in 0 until SIZE_IN_BYTES step stepSize) {
            val nextAddresses = (address until (address + stepSize)).toList()

            val bytesLine = nextAddresses.joinToString(separator = " ") { _buffer[it].toByteHexString }
            println("${address.toHexString}\t$bytesLine")
        }
    }
}