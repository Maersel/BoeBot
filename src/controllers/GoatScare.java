package controllers;

import TI.Timer;
import hardware.Updatable;
import hardware.buzzer.Buzzer;
import hardware.ultrasonic.UltraSonic;

public class GoatScare implements Updatable {
    private UltraSonic ultraSonic;
    private Buzzer buzzer;
    private Timer timer;

    public GoatScare(UltraSonic ultraSonic, Buzzer buzzer){
        this.ultraSonic = ultraSonic;
        this.buzzer = buzzer;
        this.timer = new Timer(7000);
    }

    private boolean currentMode;

    public void setMode(boolean state) {
        this.currentMode = state;
    }

    public void turnOn() {
        this.buzzer.turnOn();
    }

    public boolean checkIfDone() {
        return false;
    }

//    public void scaringMethod(){
//        if (this.ultraSonic.closeObject()){
//            this.turnOn();
//        }
//
//    }

    @Override
    public void update() {
        if (timer.timeout() && this.buzzer.isTurnedOn()) {
            this.buzzer.turnOff();
        }
    }
}
