package de.sawtschuk.hc2garmin.data.fit

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class FitFileBuilderTest {

    @Test
    fun `weight file writes BMI field using FIT scale 10`() {
        val fit = FitFileBuilder.buildWeightFitFile(
            weightKg = 75.25,
            fatPercent = 18.7,
            epochSeconds = 1_700_000_000L,
            bmi = 24.6
        )

        assertEquals(246, fit.readLittleEndian16(fit.size - 4))
        assertArrayEquals(
            byteArrayOf(13, 2, 0x84.toByte()),
            fit.findFieldDefinition(fieldNumber = 13)
        )
    }

    @Test
    fun `weight file marks BMI invalid when height is unavailable`() {
        val fit = FitFileBuilder.buildWeightFitFile(
            weightKg = 75.25,
            fatPercent = null,
            epochSeconds = 1_700_000_000L
        )

        assertEquals(0xFFFF, fit.readLittleEndian16(fit.size - 4))
    }

    private fun ByteArray.readLittleEndian16(offset: Int): Int =
        (this[offset].toInt() and 0xFF) or ((this[offset + 1].toInt() and 0xFF) shl 8)

    private fun ByteArray.findFieldDefinition(fieldNumber: Int): ByteArray? {
        val expected = byteArrayOf(fieldNumber.toByte(), 2, 0x84.toByte())
        for (index in 0..size - expected.size) {
            if (copyOfRange(index, index + expected.size).contentEquals(expected)) {
                return copyOfRange(index, index + expected.size)
            }
        }
        return null
    }
}
