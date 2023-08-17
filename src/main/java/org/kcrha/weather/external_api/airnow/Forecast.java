package org.kcrha.weather.external_api.airnow;

import java.time.LocalDate;

public record Forecast(
        LocalDate DateIssue,
        LocalDate DateForecast,
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
