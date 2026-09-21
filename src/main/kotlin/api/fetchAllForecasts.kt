package api

import model.ForecastDay

private val CITIES = listOf("Chisinau", "Madrid", "Kyiv", "Amsterdam")
private const val TOMORROW_INDEX = 1

suspend fun fetchAllForecasts(
    apiKey: String,
    apiService: WeatherApiService = RetrofitClient.apiService,
): Map<String, ForecastDay> {
    val result = mutableMapOf<String, ForecastDay>()

    for (city in CITIES) {
        try {
            val response = apiService.getForecast(
                apiKey = apiKey,
                city = city
            )
            val tomorrow = response.forecast.forecastday.getOrNull(TOMORROW_INDEX)

            if (tomorrow != null) {
                result[city] = tomorrow
            } else {
                println("Warning: There is no data for $city for tomorrow.")
            }

        } catch (e: Exception) {
            println("Error retrieving the forecast for $city: ${e.message}")
        }
    }

    return result
}