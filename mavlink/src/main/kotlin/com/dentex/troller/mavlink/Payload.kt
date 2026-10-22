package com.dentex.troller.mavlink

/**
 * Little endian reader over a MAVLink payload.
 *
 * Mirrors `com.MAVLink.Messages.MAVLinkPayload` from the Helmsman APK: every multi byte field is
 * little endian, unsigned types are widened to `Int`/`Long`, and signed types keep their sign.
 */
class PayloadReader(private val bytes: ByteArray) {

    var position: Int = 0
        private set

    val remaining: Int get() = bytes.size - position

    fun readByte(): Int = (bytes[position++].toInt() and 0xFF)

    fun readShort(): Int {
        val value = (bytes[position].toInt() and 0xFF) or
            ((bytes[position + 1].toInt() and 0xFF) shl 8)
        position += 2
        return value
    }

    fun readSignedShort(): Int {
        val value = (bytes[position].toInt() and 0xFF) or
            ((bytes[position + 1].toInt() and 0xFF) shl 8)
        position += 2
        return value.toShort().toInt()
    }

    fun readInt(): Int {
        val value = (bytes[position].toInt() and 0xFF) or
            ((bytes[position + 1].toInt() and 0xFF) shl 8) or
            ((bytes[position + 2].toInt() and 0xFF) shl 16) or
            ((bytes[position + 3].toInt() and 0xFF) shl 24)
        position += 4
        return value
    }

    fun readLong(): Long {
        var value = 0L
        for (index in 7 downTo 0) {
            value = (value shl 8) or (bytes[position + index].toLong() and 0xFF)
        }
        position += 8
        return value
    }

    fun readFloat(): Float = Float.fromBits(readInt())
}

/**
 * Little endian writer used to build MAVLink payloads.
 *
 * Mirrors the `put*` methods of `com.MAVLink.Messages.MAVLinkPayload`.
 */
class PayloadWriter(initialCapacity: Int = 32) {

    private var buffer = ByteArray(initialCapacity)
    private var size: Int = 0

    val length: Int get() = size

    private fun ensure(extra: Int) {
        if (size + extra <= buffer.size) return
        var capacity = if (buffer.isEmpty()) 16 else buffer.size
        while (capacity < size + extra) capacity = capacity shl 1
        buffer = buffer.copyOf(capacity)
    }

    fun writeByte(value: Int) {
        ensure(1)
        buffer[size++] = value.toByte()
    }

    fun writeShort(value: Int) {
        ensure(2)
        buffer[size++] = (value and 0xFF).toByte()
        buffer[size++] = ((value shr 8) and 0xFF).toByte()
    }

    fun writeInt(value: Int) {
        ensure(4)
        buffer[size++] = (value and 0xFF).toByte()
        buffer[size++] = ((value shr 8) and 0xFF).toByte()
        buffer[size++] = ((value shr 16) and 0xFF).toByte()
        buffer[size++] = ((value shr 24) and 0xFF).toByte()
    }

    fun writeLong(value: Long) {
        ensure(8)
        for (index in 0 until 8) {
            buffer[size++] = ((value shr (index * 8)) and 0xFF).toByte()
        }
    }

    fun writeFloat(value: Float) = writeInt(value.toRawBits())

    fun writeBytes(values: ByteArray) {
        ensure(values.size)
        values.copyInto(buffer, size)
        size += values.size
    }

    fun toByteArray(): ByteArray = buffer.copyOf(size)
}
