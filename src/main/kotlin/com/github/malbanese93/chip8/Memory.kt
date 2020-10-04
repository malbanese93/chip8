package com.github.malbanese93.chip8

import com.github.malbanese93.utils.OutOfRAMException
import com.github.malbanese93.utils.ValueExceedingByteException


class Memory {
    companion object {
        const val SIZE_IN_BYTES = 4096
    }

    private val _buffer : IntArray = IntArray(SIZE_IN_BYTES) {
            idx -> 0xa1 // fill some random data
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
}