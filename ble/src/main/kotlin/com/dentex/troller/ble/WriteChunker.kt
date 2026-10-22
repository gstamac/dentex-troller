package com.dentex.troller.ble

/**
 * Splits an outbound payload into the chunks that are written to the GATT characteristic.
 *
 * The Helmsman application writes at most 20 bytes per GATT write and pauses between the
 * chunks. The motor's BLE module relays the bytes to the application processor, so the MAVLink
 * frames are not modified in any way.
 */
object WriteChunker {

    /** Maximum number of bytes in one GATT write. */
    const val CHUNK_SIZE: Int = 20

    /**
     * Splits [payload] into chunks of at most [chunkSize] bytes.
     *
     * The last chunk holds the remainder and may be shorter. An empty payload produces no
     * chunks.
     */
    fun split(payload: ByteArray, chunkSize: Int = CHUNK_SIZE): List<ByteArray> {
        require(chunkSize > 0) { "The chunk size must be positive" }
        if (payload.isEmpty()) return emptyList()
        val chunks = ArrayList<ByteArray>((payload.size + chunkSize - 1) / chunkSize)
        var offset = 0
        while (offset < payload.size) {
            val end = minOf(offset + chunkSize, payload.size)
            chunks.add(payload.copyOfRange(offset, end))
            offset = end
        }
        return chunks
    }
}
