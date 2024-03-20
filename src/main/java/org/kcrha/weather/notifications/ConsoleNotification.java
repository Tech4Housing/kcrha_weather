package org.kcrha.weather.notifications;

import lombok.Getter;

@Getter
public class ConsoleNotification implements Notification<String, String> {

    public boolean send(String output) {
        System.out.println(output);
        return true;
    }

    public String prepare(String message) {
        return message;
    }
}
