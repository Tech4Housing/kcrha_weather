package org.kcrha.weather.models.forecast;

import org.kcrha.weather.models.forecast.metrics.ForecastMetric;

import java.time.LocalDate;
import java.util.List;

public interface AggregateForecast {
    LocalDate getDate();

    List<ForecastMetric<?>> getMetrics();
}
