package hardware.whisker;

import TI.BoeBot;
import TI.PinMode;
import hardware.Updatable;

public class Whisker implements Updatable {
    private int pin;
    private Callback callback;

    public Whisker(int pin, Callback callback) {
        BoeBot.setMode(pin, PinMode.Input);

        this.pin = pin;
        this.callback = callback;
    }

    @Override
    public void update() {
        if (!BoeBot.digitalRead(this.pin)) {
            callback.onWhiskerContact(this);
        }
    }
}
