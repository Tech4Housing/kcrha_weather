package org.kcrha.weather.models;

import org.kcrha.weather.models.metrics.ForecastMetric;

import java.time.LocalDate;
import java.util.List;

public interface AggregateForecast {
    public LocalDate getDate();
    public List<ForecastMetric> getMetrics();
}
