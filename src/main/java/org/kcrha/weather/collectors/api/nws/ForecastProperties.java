package org.kcrha.weather.collectors.api.nws;

import java.util.List;

public record ForecastProperties(List<ForecastPeriod> periods) {
}
