package output

import model.DayData
import model.ForecastDay
import model.HourData
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertTrue

class TableFormatterTest {

    private fun capturePrintedOutput(block: () -> Unit): String {
        val originalOut = System.out
        val captured = ByteArrayOutputStream()
        System.setOut(PrintStream(captured))
        try {
            block()
        } finally {
            System.setOut(originalOut)
        }
        return captured.toString()
    }

    @Test
    fun `prints a row per city with its data points`() {
        val forecast = ForecastDay(
            date = "2026-09-22",
            day = DayData(minTempC = 8.0, maxTempC = 16.6, avgHumidity = 69, maxWindKph = 19.1),
            hour = listOf(HourData(time = "2026-09-22 12:00", windKph = 19.1, windDir = "NW"))
        )

        val output = capturePrintedOutput {
            TableFormatter.printForecastTable(mapOf("Chisinau" to forecast))
        }

        assertTrue(output.contains("Forecast for 22-09-2026"))
        assertTrue(output.contains("Chisinau"))
        assertTrue(output.contains("8.0"))
        assertTrue(output.contains("16.6"))
        assertTrue(output.contains("69"))
        assertTrue(output.contains("19.1"))
        assertTrue(output.contains("NW"))
    }

    @Test
    fun `falls back to N SLASH A when there is no noon reading`() {
        val forecast = ForecastDay(
            date = "2026-09-22",
            day = DayData(minTempC = 8.0, maxTempC = 16.6, avgHumidity = 69, maxWindKph = 19.1),
            hour = listOf(HourData(time = "2026-09-22 06:00", windKph = 10.0, windDir = "SE"))
        )

        val output = capturePrintedOutput {
            TableFormatter.printForecastTable(mapOf("Chisinau" to forecast))
        }

        assertTrue(output.contains("N/A"))
    }

    @Test
    fun `prints nothing when there are no forecasts`() {
        val output = capturePrintedOutput {
            TableFormatter.printForecastTable(emptyMap())
        }

        assertTrue(output.isEmpty())
    }
}
