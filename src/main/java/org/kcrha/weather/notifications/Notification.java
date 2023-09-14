package org.kcrha.weather.notifications;

public interface Notification {

    boolean send(String message);
}
