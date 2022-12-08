package hardware.button;

import hardware.Updatable;

public class EmergencyButton extends Button implements Updatable{
    public EmergencyButton(int pin, Callback callback) {
        super(pin, callback);
    }
}
