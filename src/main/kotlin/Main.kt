import api.RetrofitClient
import api.fetchAllForecasts
import kotlinx.coroutines.runBlocking
import output.TableFormatter

fun main() = runBlocking {
    try {
        val apiKey = System.getenv("WEATHER_API_KEY") ?: error("WEATHER_API_KEY is missing")
        val forecasts = fetchAllForecasts(apiKey)
        TableFormatter.printForecastTable(forecasts)
    } finally {
        RetrofitClient.shutdown()
    }
}