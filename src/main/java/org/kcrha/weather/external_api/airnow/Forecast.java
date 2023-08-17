package org.kcrha.weather.external_api.airnow;

public record Forecast(
        String DateIssue,
        String DateForecast,
        String ReportingArea,
        String StateCode,
        Float Latitude,
        Float Longitude,
        String ParameterName,
        Integer AQI,
        ForecastCategory Category,
        Boolean ActionDay,
        String Discussion
) {
}
