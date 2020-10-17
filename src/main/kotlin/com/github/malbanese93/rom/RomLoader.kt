package com.github.malbanese93.rom

import com.github.malbanese93.chip8.Memory
import com.github.malbanese93.exceptions.InvalidRomSizeException
import com.github.malbanese93.exceptions.RomNotFoundException
import com.github.malbanese93.exceptions.RomUnsupportedFormatException
import com.github.malbanese93.utils.START_PC
import java.io.File

object RomLoader {
    private val SUPPORTED_EXTENSIONS = listOf("ch8")

    fun readRomContents(romPath : String) : ByteArray {
        val fileHandle = File(romPath)
        if (!fileHandle.exists()) throw RomNotFoundException(
            romPath
        )
        if (fileHandle.extension !in SUPPORTED_EXTENSIONS) throw RomUnsupportedFormatException(
            romPath
        )
        if (fileHandle.length() !in 1 until Memory.SIZE_IN_BYTES - START_PC) throw InvalidRomSizeException(
            romPath,
            fileHandle.length()
        )

        return fileHandle.readBytes()
    }
}