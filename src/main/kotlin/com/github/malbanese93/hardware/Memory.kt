package com.github.malbanese93.hardware

@ExperimentalUnsignedTypes
class Memory {
    companion object {
        const val SIZE_IN_BYTES = 4096
    }

    val buffer : UByteArray = UByteArray(SIZE_IN_BYTES)
}