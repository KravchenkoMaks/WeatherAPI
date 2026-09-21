package model

data class ForecastDay(
    val date: String,
    val day: DayData,
    val hour: List<HourData>
)