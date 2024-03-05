package org.kcrha.weather.notifications.builders;

import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.DailyAggregateForecast;

import java.util.List;
import java.util.Map;

public interface ReportBuilder<T> {
    void setRegionalForecasts(Map<Region, Map<Location, List<DailyAggregateForecast>>> forecasts);
    T getReport();
}
