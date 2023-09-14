package org.kcrha.weather.notifications;

import lombok.Getter;

@Getter
public class ConsoleNotification implements Notification {

    @Override
    public boolean send(String output) {
        System.out.println(output);
        return true;
    }
}
