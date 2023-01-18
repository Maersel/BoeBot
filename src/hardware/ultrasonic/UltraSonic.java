package hardware.ultrasonic;

import TI.BoeBot;
import TI.PinMode;
import TI.Timer;
import controllers.AddDelay;
import hardware.Updatable;

public class UltraSonic implements Updatable {
    private int pinEcho;
    private int pinTrigger;
    private Callback callback;
    private Timer timer;
    private AddDelay addDelay;
    private int distance;

    private boolean isTurnedOn;

    public UltraSonic(int pinEcho, int pinTrigger, Callback callback, AddDelay addDelay) {

        this.pinEcho = pinEcho;
        this.pinTrigger = pinTrigger;
        this.callback = callback;
        this.timer = new Timer(150);
        this.addDelay = addDelay;

        this.isTurnedOn = true;

        this.setPins();
    }

    private void setPins() {
        BoeBot.setMode(this.pinEcho, PinMode.Input);
        BoeBot.setMode(this.pinTrigger, PinMode.Output);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void turnOn() {
        this.isTurnedOn = true;
        System.out.println("ultra on");
    }

    public void turnOff() {
        this.isTurnedOn = false;
        System.out.println("ultra on");
    }

    @Override
    public void update() {
        if (timer.timeout() && isTurnedOn) {
            callback.onUltraSonic(this.getDistance());
        }
    }

    public int getDistance() {
        long startTime = System.currentTimeMillis();

        BoeBot.digitalWrite(pinTrigger, true);
        BoeBot.uwait(1);
        BoeBot.digitalWrite(pinTrigger, false);

//        this.addDelay.addDelay("Ultrasonic delay", 139, () -> {
//            System.out.println(BoeBot.pulseIn(pinEcho, true, 10000));
            this.distance = (BoeBot.pulseIn(pinEcho, true, 2500) / 58);
//        });

        long endTime = System.currentTimeMillis();

//        System.out.println("Time:\t" + (endTime-startTime) + "\t\tdistance: \t" + this.distance);

        return this.distance;
    }
}