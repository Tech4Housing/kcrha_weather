package org.kcrha.weather.notifications;

import lombok.Getter;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.AggregateForecast;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class PlainTextFormatter implements ReportFormatter {

    @Override
    public String formatReportHeader() {
        return null;
    }
    public String formatReportHeader(Map<Region, Map<Location, Map<LocalDate, List<String>>>> allActiveAlerts) {
        return null;
    }

    @Override
    public String formatForecastTableHeader(String tableHeader) {
        return tableHeader;
    }

    public String formatForecastTable(List<? extends AggregateForecast> forecasts, Map<LocalDate, List<String>> activeAlerts) { return null; }
    public String formatForecastTable(List<? extends AggregateForecast> forecasts) {
        return forecasts.stream().sorted(Comparator.comparing(AggregateForecast::getDate)).map(dailyAggregatedForecast -> {
            String formattedMetrics = dailyAggregatedForecast.getMetrics().stream().map(forecast -> {
                return forecast.getShortName() + ": " + forecast.getValue();
            }).collect(Collectors.joining(", "));
            return String.format("Date: %s, %s\n", dailyAggregatedForecast.getDate(), formattedMetrics);
        }).collect(Collectors.joining("\n"));
    }

    @Override
    public String formatForecastTableFooter(String tableFooter) {
        return tableFooter;
    }

    @Override
    public String formatReportFooter() {
        return null;
    }
}
