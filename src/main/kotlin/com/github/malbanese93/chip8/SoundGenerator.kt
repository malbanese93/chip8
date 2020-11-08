package com.github.malbanese93.chip8

class SoundGenerator {
    var isOn : Boolean = false

    fun doSound() {
        if(isOn) println("** BEEP **")
    }

    fun reset() {
        isOn = false
    }
}