package com.github.malbanese93.utils

import com.github.malbanese93.extensions.toHexString
import com.github.malbanese93.chip8.Memory.Companion.SIZE_IN_BYTES
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class ValueExceedingByteException(value : Int) : IllegalStateException("Value <${value.toHexString}> exceeds one byte")
class OutOfRAMException(address : Int) : IllegalArgumentException("The address <${address.toHexString}> is outside the allowed range: 0x0 - ${SIZE_IN_BYTES.toHexString}")
class UnknownOpcodeException(opcode : Int) : IllegalArgumentException("OpcodeMnemonic <${opcode.toHexString}> is not a valid opcode for Chip8")
class StackOverflowException(index : Int) : IllegalArgumentException("Routine stack overflow: requested index $index")
class OutOfVideoBufferException(x : Int, y : Int) : IllegalArgumentException("Trying to access out-of-bounds pixel at position ($x, $y)")
class InvalidPixelHeightException(pixelHeight : Int) : IllegalArgumentException("Pixel height $pixelHeight is not valid")