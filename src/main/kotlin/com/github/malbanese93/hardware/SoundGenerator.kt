package com.github.malbanese93.hardware

class SoundGenerator {
    var isOn = true

    fun doSound() {
        if(isOn) println("** BEEP **")
    }
}