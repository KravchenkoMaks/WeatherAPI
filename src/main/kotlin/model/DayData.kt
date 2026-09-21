package model

import com.google.gson.annotations.SerializedName

data class DayData(
    @SerializedName("mintemp_c") val minTempC: Double,
    @SerializedName("maxtemp_c") val maxTempC: Double,
    @SerializedName("avghumidity") val avgHumidity: Int,
    @SerializedName("maxwind_kph") val maxWindKph: Double,
)