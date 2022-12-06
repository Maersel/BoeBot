package hardware.linesensor;

import TI.BoeBot;
import hardware.Updatable;

public class InfraRed implements Updatable {
    private int pin;
    private Callback callback;

    public InfraRed(int pin) {
        this.pin = pin;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void update() {
        if (BoeBot.analogRead(pin) > 1450) {
            this.callback.onLineDetection(this);
        }
    }
}
