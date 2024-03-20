package org.kcrha.weather.notifications.builders;

import lombok.Getter;
import lombok.Setter;
import org.kcrha.weather.Report;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.DailyAggregateForecast;
import org.kcrha.weather.models.rules.parser.RulesFileReader;
import org.kcrha.weather.notifications.ReportFormatter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class EmailReportBuilder implements AlertReportBuilder<Report> {
    ReportFormatter reportFormatter;
    Map<Region, Map<Location, List<DailyAggregateForecast>>> regionalForecasts;
    Map<Region, Map<Location, Map<LocalDate, List<String>>>> allActiveAlerts;

    public EmailReportBuilder(ReportFormatter reportFormatter) {
        this.reportFormatter = reportFormatter;
    }

    public Report getReport() {
        if (regionalForecasts == null) {
            System.out.println("WARNING: getReport called against builder that has no regionalForecasts");
        }
        calculateAllActiveAlerts();

        StringBuilder htmlMessage = new StringBuilder(reportFormatter.formatReportHeader(allActiveAlerts));

        for (Map.Entry<Region, Map<Location, List<DailyAggregateForecast>>> regionForecasts: regionalForecasts.entrySet()) {
            Region region = regionForecasts.getKey();
            if (!allActiveAlerts.containsKey(regionForecasts.getKey())) {
                allActiveAlerts.put(region, new HashMap<>());
            }
            for (Map.Entry<Location, List<DailyAggregateForecast>> locationForecasts : regionForecasts.getValue().entrySet()) {
                Location location = locationForecasts.getKey();

                htmlMessage.append(reportFormatter.formatForecastTableHeader(String.format("%s (%s)", location.location(), region.region())));

                Map<LocalDate, List<String>> activeAlerts = allActiveAlerts.getOrDefault(region, new HashMap<>()).getOrDefault(location, null);

                htmlMessage.append(reportFormatter.formatForecastTable(locationForecasts.getValue(), activeAlerts));
                htmlMessage.append(reportFormatter.formatForecastTableFooter(""));
            }
        }

        htmlMessage.append(reportFormatter.formatReportFooter());

        return new Report(reportFormatter.formatReportHeader(allActiveAlerts), htmlMessage.toString());
    }

    public void setRegionalForecasts(Map<Region, Map<Location, List<DailyAggregateForecast>>> regionalForecasts) {
        this.regionalForecasts = regionalForecasts;
    }

    private void calculateAllActiveAlerts() {
        if (allActiveAlerts != null) {
            System.out.println("calculateAllActiveAlerts called, but allActiveAlerts is not null, ignoring request");
            return;
        }

        allActiveAlerts = new HashMap<>();

        for (Map.Entry<Region, Map<Location, List<DailyAggregateForecast>>> regionForecasts: regionalForecasts.entrySet()) {
            Region region = regionForecasts.getKey();
            if (!allActiveAlerts.containsKey(regionForecasts.getKey())) {
                allActiveAlerts.put(region, new HashMap<>());
            }
            for (Map.Entry<Location, List<DailyAggregateForecast>> locationForecasts : regionForecasts.getValue().entrySet()) {
                Location location = locationForecasts.getKey();
                if (allActiveAlerts.get(region).containsKey(location)) {
                    System.out.println("Warning, may overwrite active alerts for region/location");
                }
                allActiveAlerts.get(region).put(location, getActiveAlerts(region, location));
            }
        }
    }

    public Map<LocalDate, List<String>> getActiveAlerts(Region region, Location location) {
        return RulesFileReader.getDatesWithActiveAlerts(RulesFileReader.getRules(), regionalForecasts.get(region).get(location));
    }
}
