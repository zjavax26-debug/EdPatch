package com.lumpforge.app

import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.EOFException
import java.io.InputStream
import java.nio.charset.Charset

data class WadLump(
    val index: Int,
    val name: String,
    val offset: Long,
    val size: Long
)

data class WadArchive(
    val format: String,
    val lumps: List<WadLump>
)

object WadReader {
    private const val MAX_LUMPS = 500_000
    private val ASCII: Charset = Charsets.US_ASCII

    fun read(input: InputStream, fileLength: Long): WadArchive {
        val stream = DataInputStream(BufferedInputStream(input))
        val header = ByteArray(12)
        stream.readFully(header)

        val format = String(header, 0, 4, ASCII)
        require(format == "IWAD" || format == "PWAD") {
            "La cabecera no corresponde a un WAD válido (IWAD/PWAD)."
        }

        val count = littleEndianInt(header, 4)
        val directoryOffset = littleEndianUnsignedInt(header, 8)

        require(count in 0..MAX_LUMPS) {
            "El WAD declara una cantidad de recursos no válida: $count."
        }

        val directoryLength = count.toLong() * 16L
        require(directoryOffset >= 12L && directoryOffset + directoryLength <= fileLength) {
            "El directorio del WAD está fuera de los límites del archivo."
        }

        skipFully(stream, directoryOffset - 12L)

        val lumps = ArrayList<WadLump>(count)
        repeat(count) { index ->
            val entry = ByteArray(16)
            stream.readFully(entry)
            val offset = littleEndianUnsignedInt(entry, 0)
            val size = littleEndianUnsignedInt(entry, 4)
            val nameBytes = entry.copyOfRange(8, 16)
            val rawName = String(nameBytes, ASCII)
            val name = rawName.substringBefore('\u0000').trim()

            require(offset <= fileLength && size <= fileLength - offset) {
                "El recurso #${index + 1} ($name) apunta fuera del archivo."
            }

            lumps += WadLump(index = index, name = name, offset = offset, size = size)
        }

        return WadArchive(format = format, lumps = lumps)
    }

    private fun littleEndianInt(bytes: ByteArray, start: Int): Int {
        return (bytes[start].toInt() and 0xff) or
            ((bytes[start + 1].toInt() and 0xff) shl 8) or
            ((bytes[start + 2].toInt() and 0xff) shl 16) or
            ((bytes[start + 3].toInt() and 0xff) shl 24)
    }

    private fun littleEndianUnsignedInt(bytes: ByteArray, start: Int): Long {
        return littleEndianInt(bytes, start).toLong() and 0xffffffffL
    }

    private fun skipFully(input: InputStream, bytes: Long) {
        var remaining = bytes
        while (remaining > 0) {
            val skipped = input.skip(remaining)
            if (skipped > 0) {
                remaining -= skipped
            } else {
                if (input.read() == -1) throw EOFException("Fin inesperado al leer el WAD.")
                remaining--
            }
        }
    }
}
