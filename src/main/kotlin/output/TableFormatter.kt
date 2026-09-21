package output

import model.ForecastDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object TableFormatter {

    private const val NOON = "12:00"
    private const val DATE_PATTERN = "dd-MM-yyyy"

    fun printForecastTable(forecasts: Map<String, ForecastDay>) {
        val date = forecasts.values.firstOrNull()?.date ?: return

        val formattedDate = LocalDate.parse(date)
            .format(DateTimeFormatter.ofPattern(DATE_PATTERN))

        val header = String.format("%-12s %-10s", "City", "Forecast for $formattedDate")
        println(header)
        println("=".repeat(header.length))

        val subHeader = String.format(
            "%-12s %-8s %-8s %-8s %-10s %-8s",
            "", "Min(°C)", "Max(°C)", "Hum(%)", "Wind(kph)", "Wind Dir"
        )
        println(subHeader)
        println("-".repeat(subHeader.length))

        forecasts.forEach { (city, day) ->
            val noonWindDir = day.hour
                .firstOrNull { it.time.contains(NOON) }
                ?.windDir
                ?: "N/A"

            val row = String.format(
                "%-12s %-8.1f %-8.1f %-8d %-10.1f %-8s",
                city,
                day.day.minTempC,
                day.day.maxTempC,
                day.day.avgHumidity,
                day.day.maxWindKph,
                noonWindDir
            )
            println(row)
        }
    }
}