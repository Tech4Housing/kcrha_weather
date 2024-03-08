package org.kcrha.weather.collectors.api.nws.raw_forecast;

import java.util.List;

public record RawForecastProperty(String uom, List<RawForecastValue> values) {
}
