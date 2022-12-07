package hardware.button;

import TI.BoeBot;
import hardware.Updatable;

public class Button implements Updatable {
    private int pin;
    private Callback callback;

    public Button(int pin, Callback callback) {
        this.pin = pin;
        this.callback = callback;
    }

    @Override
    public void update() {
        if (BoeBot.digitalRead(this.pin)) {
            this.callback.onButtonPress(this);
        }
    }
}
