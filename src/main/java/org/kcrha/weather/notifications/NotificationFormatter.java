package org.kcrha.weather.notifications;

import org.kcrha.weather.models.forecast.AggregateForecast;

import java.util.List;

public interface NotificationFormatter {
    public String formatHeader();

    public String formatForecastTableHeader(String tableHeader);

    public String formatForecastTable(List<? extends AggregateForecast> forecasts);

    public String formatForecastTableFooter(String tableFooter);

    public String formatFooter();
}
