package controllers;

import controllers.TimerCallback;

public interface AddDelay {
    void addDelay(String name, int time, TimerCallback callback);
}