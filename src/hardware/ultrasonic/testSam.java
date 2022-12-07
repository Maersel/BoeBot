package hardware.ultrasonic;

import TI.BoeBot;
import TI.PinMode;

public class testSam {

    public testSam() {
        BoeBot.setMode(10, PinMode.Input);
        BoeBot.setMode(9, PinMode.Output);
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
        BoeBot.digitalWrite(9, true);
        BoeBot.uwait(1);
        BoeBot.digitalWrite(9, false);
        int afstand = BoeBot.pulseIn(10, true, 10000);
//            cm = afstand;     // Divide by 29.1 or multiply by 0.0343
        System.out.println(afstand);
    }
}





//        while (true) {
//
//            double cm = 0;
//
//            BoeBot.digitalWrite(9, true);
//            BoeBot.uwait(1);
//            BoeBot.digitalWrite(9, false);
////            BoeBot.wait(1);
////            BoeBot.digitalWrite(11, false);
////            int pulse = BoeBot.pulseIn(11, true, 1000000);
//            int afstand = BoeBot.pulseIn(10, true, 8000);
////            cm = afstand;     // Divide by 29.1 or multiply by 0.0343
//
//            System.out.println(BoeBot.digitalRead(10));
//            System.out.println("Distance " + afstand);
//            BoeBot.uwait(50);
//        }

