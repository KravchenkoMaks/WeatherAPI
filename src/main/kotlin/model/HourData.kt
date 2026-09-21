package model

import com.google.gson.annotations.SerializedName

data class HourData(
    @SerializedName("time")      val time: String,
    @SerializedName("wind_kph")  val windKph: Double,
    @SerializedName("wind_dir")  val windDir: String
)