package hardware.ultrasonic;

import TI.BoeBot;
import TI.PinMode;

public class testSam {

    public testSam() {
        BoeBot.setMode(8, PinMode.Input);
        BoeBot.setMode(2, PinMode.Output);
    }

    public static void main(String[] args) {

        testSam test = new testSam();
        System.out.println("Starting....");

        while (true) {
            test.getDistance();
            BoeBot.wait(150);
        }
    }

    public void getDistance() {
        BoeBot.digitalWrite(2, true);
        BoeBot.uwait(1);
        BoeBot.digitalWrite(2, false);

        int rawdistande = BoeBot.pulseIn(8, true, 10000);
        int distance = rawdistande / 58;     // Divide by 29.1 or multiply by 0.0343
        System.out.println(distance);
    }
}