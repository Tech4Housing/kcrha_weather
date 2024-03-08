package org.kcrha.weather.collectors.api.nws.raw_forecast;

public record RawForecastProperties(
        RawForecastProperty temperature,
        RawForecastProperty quantitativePrecipitation,
        RawForecastProperty iceAccumulation,
        RawForecastProperty snowfallAmount
) {
}
