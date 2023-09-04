package org.kcrha.weather.models.cli;

import lombok.Getter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.kcrha.weather.notifications.NotificationType;

@Getter
public enum TaskType {
    ALERTS("alerts", getAlertOptions()),
    FORECASTS("forecasts", getForecastOptions()),
    REFRESH("refresh", getRefreshOptions()),
    SETUP("setup", getSetupOptions());

    private final String taskName;
    private final OptionGroup optionGroup;

    TaskType(String t, OptionGroup o) {
        this.taskName = t;
        this.optionGroup = o;
    }

    private static OptionGroup getAlertOptions() {
        OptionGroup options = new OptionGroup();
        options.addOption(Option.builder("n")
                .argName("notification")
                .longOpt("notification")
                .required(true)
                .desc("Type of notification to be used when complete")
                .type(NotificationType.class)
                .build());
        return options;
    }


    private static OptionGroup getForecastOptions() {
        OptionGroup options = new OptionGroup();
        options.addOption(Option.builder("n")
                .longOpt("notification")
                .required(true)
                .desc("Type of notification to be used when complete")
                .type(NotificationType.class)
                .build());
        return options;
    }

    private static OptionGroup getRefreshOptions() {
        return new OptionGroup();
    }

    private static OptionGroup getSetupOptions() {
        OptionGroup options = new OptionGroup();
        options.addOption(Option.builder("a")
                .longOpt("airNowApiKey")
                .required(true)
                .desc("API Key for AirNow")
                .type(NotificationType.class)
                .build());
        return options;
    }
}
