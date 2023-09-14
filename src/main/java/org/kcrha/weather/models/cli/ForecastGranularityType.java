package org.kcrha.weather.models.cli;

import lombok.Getter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.kcrha.weather.notifications.NotificationType;

@Getter
public enum ForecastGranularityType {
    LOCATION,
    REGION;
}
