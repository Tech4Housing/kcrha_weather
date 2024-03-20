package org.kcrha.weather.notifications;

public interface Notification<T, U> {

    boolean send(T message);

    T prepare(U message);
}
