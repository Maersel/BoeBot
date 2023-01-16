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
        this.timer = new Timer(20);

        BoeBot.setMode(this.pinEcho, PinMode.Input);
        BoeBot.setMode(this.pinTrigger, PinMode.Output);
    }

    @Override
    public void update() {
        if (timer.timeout()){
            callback.onUltraSonic((int) this.getDistance());
        }
    }

    public float getDistance() {

        BoeBot.digitalWrite(pinTrigger, true);
        BoeBot.uwait(1);           //Moet nog worden gefixed
        BoeBot.digitalWrite(pinTrigger, false);

        int rawDistance = BoeBot.pulseIn(pinEcho, true, 10000);
        float distance = rawDistance / 58;     // Divide by 29.1 or multiply by 0.0343
//        System.out.println("Ultra sonicdistance: " + distance);
        return distance;
    }

//    public boolean closeObject() {
//        float distance = this.getDistance();
//        return (distance <= 15 && distance >= 3);

//    }
}