package api

import kotlinx.coroutines.test.runTest
import model.DayData
import model.Forecast
import model.ForecastDay
import model.ForecastResponse
import model.HourData
import model.Location
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun forecastDay(date: String) = ForecastDay(
    date = date,
    day = DayData(minTempC = 10.0, maxTempC = 20.0, avgHumidity = 50, maxWindKph = 15.0),
    hour = listOf(HourData(time = "$date 12:00", windKph = 15.0, windDir = "NW"))
)

private fun responseWithTomorrow() = ForecastResponse(
    location = Location(name = "Kyiv", localtime = "2026-09-21 10:00"),
    forecast = Forecast(forecastday = listOf(forecastDay("2026-09-21"), forecastDay("2026-09-22")))
)

private fun responseWithoutTomorrow() = ForecastResponse(
    location = Location(name = "Kyiv", localtime = "2026-09-21 10:00"),
    forecast = Forecast(forecastday = listOf(forecastDay("2026-09-21")))
)

private class FakeWeatherApiService(
    private val responses: Map<String, ForecastResponse> = emptyMap(),
    private val failingCities: Set<String> = emptySet(),
) : WeatherApiService {
    override suspend fun getForecast(apiKey: String, city: String, days: Int): ForecastResponse {
        if (city in failingCities) throw RuntimeException("boom")
        return responses[city] ?: error("No stub configured for $city")
    }
}

class FetchAllForecastsTest {

    @Test
    fun `returns tomorrow's forecast for every city`() = runTest {
        val cities = listOf("Chisinau", "Madrid", "Kyiv", "Amsterdam")
        val apiService = FakeWeatherApiService(
            responses = cities.associateWith { responseWithTomorrow() }
        )

        val result = fetchAllForecasts(apiKey = "key", apiService = apiService)

        assertEquals(cities.toSet(), result.keys)
        assertEquals("2026-09-22", result.getValue("Kyiv").date)
    }

    @Test
    fun `skips a city when tomorrow's forecast is missing`() = runTest {
        val apiService = FakeWeatherApiService(
            responses = mapOf(
                "Chisinau" to responseWithTomorrow(),
                "Madrid" to responseWithoutTomorrow(),
                "Kyiv" to responseWithTomorrow(),
                "Amsterdam" to responseWithTomorrow(),
            )
        )

        val result = fetchAllForecasts(apiKey = "key", apiService = apiService)

        assertTrue("Madrid" !in result)
        assertEquals(3, result.size)
    }

    @Test
    fun `skips a city when the request fails`() = runTest {
        val apiService = FakeWeatherApiService(
            responses = mapOf(
                "Chisinau" to responseWithTomorrow(),
                "Kyiv" to responseWithTomorrow(),
                "Amsterdam" to responseWithTomorrow(),
            ),
            failingCities = setOf("Madrid"),
        )

        val result = fetchAllForecasts(apiKey = "key", apiService = apiService)

        assertTrue("Madrid" !in result)
        assertEquals(3, result.size)
    }
}
