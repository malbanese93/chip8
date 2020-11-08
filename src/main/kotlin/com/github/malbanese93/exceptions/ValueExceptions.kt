package com.github.malbanese93.exceptions

import com.github.malbanese93.extensions.toHexString
import java.lang.IllegalStateException

class ValueExceedingByteException(value : Int) : IllegalStateException("Value <${value.toHexString}> exceeds one byte")
class ValueExceedingNibbleException(value : Int) : IllegalStateException("Value <${value.toHexString}> exceeds one nibble (half byte)")