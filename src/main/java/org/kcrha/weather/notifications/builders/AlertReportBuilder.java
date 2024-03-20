package org.kcrha.weather.notifications.builders;

import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AlertReportBuilder<T> extends ReportBuilder<T> {
    Map<LocalDate, List<String>> getActiveAlerts(Region region, Location location);
}
