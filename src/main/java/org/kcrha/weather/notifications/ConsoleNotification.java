package org.kcrha.weather.notifications;

import lombok.Getter;
import org.kcrha.weather.models.forecast.AggregateForecast;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ConsoleNotification implements Notification {

    @Override
    public boolean send(String output) {
        System.out.println(output);
        return true;
    }
}
