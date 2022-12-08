package hardware.buzzer;

import TI.BoeBot;
import TI.PinMode;
import TI.Timer;
import hardware.Updatable;


public class Buzzer implements Updatable {
    private int pin;
    private Timer timer;
    private Timer timer2;
    private boolean firstPing;
    private boolean isTurnedOn;

    public Buzzer(int pin) {
        System.out.println("Buzzer constr");
        this.pin = pin;
        this.firstPing = true;
        this.isTurnedOn = false;

        BoeBot.setMode(this.pin, PinMode.Output);
        this.timer = new Timer(3000);
        this.timer2 = new Timer(200);
    }

    public void turnOn() {
        this.isTurnedOn = true;
    }

    public void turnOff() {
        this.isTurnedOn = false;
    }

    public void ping() {
        if (firstPing && this.timer.timeout()) {
            System.out.println("a");
            BoeBot.freqOut(this.pin ,1500, 100);
            this.timer2.mark();
            firstPing = false;
        }
    }
    public void ping2() {
        if (!firstPing && this.timer2.timeout()) {
            System.out.println("b");
            BoeBot.freqOut(this.pin ,1000, 150);
            this.timer.mark();
            firstPing = true;
        }
    }

    @Override
    public void update() {
        if (isTurnedOn) {
            ping();
            ping2();
        }
    }
}
