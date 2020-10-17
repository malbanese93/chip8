package com.github.malbanese93

import com.github.malbanese93.chip8.Chip8
import com.github.malbanese93.rom.RomLoader
import com.github.malbanese93.utils.NoStartArgumentsException

fun main(args: Array<String>) {
    if (args.isEmpty()) throw NoStartArgumentsException()
    val romContents = readRomContents(romPath = args[0])

    val chip8 = Chip8()
    chip8.start(romContents)
}

private fun readRomContents(romPath : String) = RomLoader.readRomContents(romPath)