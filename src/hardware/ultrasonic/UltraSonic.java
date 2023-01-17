package hardware.ultrasonic;

import TI.BoeBot;
import TI.PinMode;
import TI.Timer;
import hardware.Updatable;

public class UltraSonic implements Updatable {
    private int pinEcho;
    private int pinTrigger;
    private Callback callback;
    private Timer timer;

    public UltraSonic(int pinEcho, int pinTrigger, Callback callback) {
        this.pinEcho = pinEcho;
        this.pinTrigger = pinTrigger;
        this.callback = callback;
        this.timer = new Timer(50);

        this.setPins();
    }

    private void setPins() {
        BoeBot.setMode(this.pinEcho, PinMode.Input);
        BoeBot.setMode(this.pinTrigger, PinMode.Output);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void update() {
        if (timer.timeout()) {
            callback.onUltraSonic(this.getDistance());
        }
    }

    public int getDistance() {
        BoeBot.digitalWrite(pinTrigger, true);
        BoeBot.uwait(1);
        BoeBot.digitalWrite(pinTrigger, false);

        return (BoeBot.pulseIn(pinEcho, true, 10000) / 58);
    }
}