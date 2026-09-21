package model

import com.google.gson.Gson
import kotlin.test.Test
import kotlin.test.assertEquals

class ForecastResponseDeserializationTest {

    private val sampleJson = """
        {
          "location": { "name": "Kyiv", "localtime": "2026-09-21 10:00" },
          "forecast": {
            "forecastday": [
              {
                "date": "2026-09-22",
                "day": {
                  "mintemp_c": 8.9,
                  "maxtemp_c": 14.7,
                  "avghumidity": 78,
                  "maxwind_kph": 18.7
                },
                "hour": [
                  { "time": "2026-09-22 12:00", "wind_kph": 18.7, "wind_dir": "WSW" }
                ]
              }
            ]
          }
        }
    """.trimIndent()

    @Test
    fun `maps snake_case API fields onto the Kotlin models`() {
        val response = Gson().fromJson(sampleJson, ForecastResponse::class.java)

        assertEquals("Kyiv", response.location.name)
        val day = response.forecast.forecastday.single()
        assertEquals("2026-09-22", day.date)
        assertEquals(8.9, day.day.minTempC)
        assertEquals(14.7, day.day.maxTempC)
        assertEquals(78, day.day.avgHumidity)
        assertEquals(18.7, day.day.maxWindKph)
        assertEquals("WSW", day.hour.single().windDir)
    }
}
