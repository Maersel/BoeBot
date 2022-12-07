package hardware.ultrasonic;

import TI.BoeBot;
import TI.PinMode;
import TI.Timer;
import hardware.Updatable;

public class UltraSonic implements Updatable {
    private int pin;
    private Callback callback;
    Timer timer = new Timer(900);

    @Override
    public void update() {
        if (timer.timeout()){
            getDistance();
        }
    }

private int getDistance() {
    BoeBot.setMode(8, PinMode.Input);
    BoeBot.setMode(2, PinMode.Output);

        BoeBot.digitalWrite(2, true);
        BoeBot.uwait(1);           //Moet nog worden gefixed
        BoeBot.digitalWrite(2, false);

            int rawDistance = BoeBot.pulseIn(8, true, 10000);
            int distance = rawDistance / 58;     // Divide by 29.1 or multiply by 0.0343

                return distance;
    }

    public boolean closeObject(){
        return (this.getDistance() >= 14);
    }
}


