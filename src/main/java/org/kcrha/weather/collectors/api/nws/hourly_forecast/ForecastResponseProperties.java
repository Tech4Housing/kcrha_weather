package org.kcrha.weather.collectors.api.nws.hourly_forecast;

import java.util.List;

public record ForecastResponseProperties(List<ForecastPeriod> periods) {
}
