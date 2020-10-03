package com.github.malbanese93

import com.github.malbanese93.bit.toHexString
import com.github.malbanese93.hardware.Memory.Companion.SIZE_IN_BYTES
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class ValueExceedingByteException(value : Int) : IllegalStateException("Value <${value.toHexString}> exceeds one byte")
class OutOfRAMException(address : Int) : IllegalArgumentException("The address <${address.toHexString}> is outside the allowed range: 0x0 - ${SIZE_IN_BYTES.toHexString}")