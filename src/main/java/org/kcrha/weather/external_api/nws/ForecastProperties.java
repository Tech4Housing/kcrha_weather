package org.kcrha.weather.external_api.nws;

import java.util.List;

public record ForecastProperties(List<ForecastPeriod> periods) {
}
