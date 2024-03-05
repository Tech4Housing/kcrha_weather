package org.kcrha.weather.notifications;

import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.AggregateForecast;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportFormatter {
    String formatReportHeader();
    String formatReportHeader(Map<Region, Map<Location, Map<LocalDate, List<String>>>> allActiveAlerts);

    String formatForecastTableHeader(String tableHeader);

    String formatForecastTable(List<? extends AggregateForecast> forecasts);
    String formatForecastTable(List<? extends AggregateForecast> forecasts, Map<LocalDate, List<String>> activeAlerts);

    String formatForecastTableFooter(String tableFooter);

    String formatReportFooter();
}
