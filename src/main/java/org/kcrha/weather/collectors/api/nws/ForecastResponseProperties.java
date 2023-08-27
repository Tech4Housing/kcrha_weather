package org.kcrha.weather.collectors.api.nws;

import java.util.List;

public record ForecastResponseProperties(List<ForecastPeriod> periods) {
}
