package com.github.malbanese93.chip8

class SoundGenerator {
    var isOn = true

    fun doSound() {
        if(isOn) println("** BEEP **")
    }
}