# WeatherAPI Code Challenge

Fetches the next-day forecast for Chisinau, Madrid, Kyiv, and Amsterdam from
[WeatherAPI.com](https://www.weatherapi.com/) and prints it as a table to STDOUT.

Built with Kotlin, Gradle, and Retrofit.

## Setup

1. Sign up at [WeatherAPI.com](https://www.weatherapi.com/) and get a free API key.
2. Export it as an environment variable:
   ```bash
   export WEATHER_API_KEY=your_key_here
   ```

## Run

```bash
./gradlew run
```

## Example output

```
City         Forecast for 22-09-2026
====================================
             Min(°C)  Max(°C)  Hum(%)   Wind(kph)  Wind Dir
-----------------------------------------------------------
Chisinau     8.0      16.6     69       19.1       NW
Madrid       19.1     29.1     17       11.2       E
Kyiv         8.9      14.7     78       18.7       WSW
Amsterdam    12.0     19.0     73       7.2        WSW
```
