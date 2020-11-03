package com.github.malbanese93.exceptions

import java.lang.IllegalArgumentException

class OutOfVideoBufferException(x : Int, y : Int) : IllegalArgumentException("Trying to access out-of-bounds pixel at position ($x, $y)")
class InvalidPixelHeightException(pixelHeight : Int) : IllegalArgumentException("Pixel height $pixelHeight is not valid")