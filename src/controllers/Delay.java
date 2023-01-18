package controllers;

import TI.Timer;
import hardware.Updatable;
import java.util.ArrayList;

public class Delay implements Updatable {
    private String name;
    private ArrayList<Updatable> devices;
    private Timer timer;
    private TimerCallback callback;

    public Delay(String name, ArrayList<Updatable> devices, int time, TimerCallback timerCallback) {
        this.name = name;
        this.devices = devices;
        this.timer = new Timer(time);
        this.callback = timerCallback;

//        System.out.println("Timer: \t" + this.name + " starting in: " + time);
    }

    @Override
    public void update() {
        if (timer.timeout()) {
//            System.out.println("Timer: \t" + this.name + " firing now");
            devices.remove(this);
            callback.fire();
        }
    }
}
