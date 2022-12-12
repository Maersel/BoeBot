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
        this.timer = new Timer(900);

        BoeBot.setMode(this.pinEcho, PinMode.Input);
        BoeBot.setMode(this.pinTrigger, PinMode.Output);
    }

    @Override
    public void update() {
        if (timer.timeout() && closeObject()){
            callback.onUltraSonic();
        }
    }

    private int getDistance() {

        BoeBot.digitalWrite(2, true);
        BoeBot.uwait(1);           //Moet nog worden gefixed
        BoeBot.digitalWrite(2, false);

        int rawDistance = BoeBot.pulseIn(8, true, 10000);
        int distance = rawDistance / 58;     // Divide by 29.1 or multiply by 0.0343

        return distance;
    }

    public boolean closeObject(){
        return (this.getDistance() <= 14);
    }
}