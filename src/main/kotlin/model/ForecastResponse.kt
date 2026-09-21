package model

data class ForecastResponse(
    val location: Location,
    val forecast: Forecast
)