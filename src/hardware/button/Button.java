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

    public boolean buttonPressed() {
        return !BoeBot.digitalRead(this.pin);
    }

    @Override
    public void update() {
        if (this.buttonPressed()) {
            this.callback.onButtonPress(this);
        }
    }
}
