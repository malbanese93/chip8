package com.github.malbanese93.chip8

import com.github.malbanese93.utils.OutOfVideoBufferException

class VideoBuffer {
    companion object {
        const val ROW_PIXELS = 64
        const val COL_PIXELS = 32
        const val MIN_PIXEL_SIZE = 1
        const val MAX_PIXEL_SIZE = 15
    }

    private val _buffer : BooleanArray = BooleanArray(ROW_PIXELS * COL_PIXELS)

    init {
        reset()
    }

    fun reset() {
        clearBuffer()
    }

    fun clearBuffer() {
        for (idx in 0 until (ROW_PIXELS * COL_PIXELS))
            _buffer[idx] = false
    }

    operator fun get(x: Int, y: Int) : Boolean {
        if(x !in 0..ROW_PIXELS || y !in 0..COL_PIXELS) throw OutOfVideoBufferException(x, y)

        return _buffer[y * ROW_PIXELS + x]
    }

    operator fun set(x: Int, y: Int, value: Boolean) {
        if(x !in 0..ROW_PIXELS || y !in 0..COL_PIXELS) throw OutOfVideoBufferException(x, y)

        _buffer[y * ROW_PIXELS + x] = value.xor(_buffer[y * ROW_PIXELS + x])
    }
}