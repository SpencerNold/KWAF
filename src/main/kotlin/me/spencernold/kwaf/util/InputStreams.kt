package me.spencernold.kwaf.util

import java.io.IOException
import java.io.InputStream
import java.util.*

class InputStreams {

    companion object {

        private const val MAX_INPUT_SIZE = Int.MAX_VALUE - 8
        private const val BUFFER_SIZE: Int = 8192

        @Throws(IOException::class)
        fun readAllBytes(input: InputStream): ByteArray {
            val buffers: MutableList<ByteArray> = ArrayList()
            var size = 0
            var n = 0
            val buffer = ByteArray(BUFFER_SIZE)
            while ((input.read(buffer, 0, BUFFER_SIZE).also { n = it }) > 0) {
                size += n
                if (size > MAX_INPUT_SIZE) throw OutOfMemoryError("Required array size too large")
                buffers.add(Arrays.copyOfRange(buffer, 0, n))
            }
            val bytes = ByteArray(size)
            var index = 0
            for (buf in buffers) {
                System.arraycopy(buf, 0, bytes, index, buf.size)
                index += buf.size
            }
            return bytes
        }
    }
}