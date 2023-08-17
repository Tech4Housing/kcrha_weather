package org.kcrha.weather.external_api.nws;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public record ForecastPeriod(
        Integer number,
        String name,
        LocalDate startTime,
        LocalDate endTime,
        Boolean isDayTime,
        Integer temperature,
        String temperatureUnit,
        GenericUnitOfMeasure probabilityOfPrecipitation,
        GenericUnitOfMeasure dewpoint,
        GenericUnitOfMeasure relativeHumidity,
        String windSpeed,
        String windDirection,
        String icon,
        String shortForecast,
        String detailedForecast
) {}
