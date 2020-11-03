package com.github.malbanese93.exceptions

import java.io.FileNotFoundException
import java.lang.IllegalArgumentException

class InvalidRomSizeException(path : String, size : Long) : IllegalArgumentException("Rom <$path> has invalid size: $size bytes")
class RomNotFoundException(path : String) : FileNotFoundException("Rom not found: <$path>")
class RomUnsupportedFormatException(path : String) : FileNotFoundException("Unsupported rom format for file: <$path>")