import TI.BoeBot;
import TI.PinMode;
import TI.Servo;
import TI.Timer;

import java.awt.Color;
import java.time.Instant;
import java.util.Collections;

public class RobotMain {

    public static void main(String[] args) {
        MovementController movement = new MovementController(12, 13);

        boolean whiskerRight;
        boolean whiskerLeft;

        setModes();

        movement.turnRight();

//        int[] lengths = {1174, 565, 565, 566, 566, 567, 566, 1173, 567, 566, 566, 566};
//        int outcome = 0b100000010000;
//        int bits = 0;
//
//
//        for (int i = 0; i < 12; i++) {
//            if (lengths[i] > 1100 && lengths[i] < 1300) {
//                System.out.println("true " + i);
//                bits = bits | (1 << i);
//                System.out.println(bits);
//            }
//        }
//
//        System.out.println("\n" + Integer.toBinaryString(bits));
//        System.out.println("\n" + Integer.toBinaryString(outcome));


        System.out.println("Listening....");
        while (true) {
            int pulseLen = BoeBot.pulseIn(15, false, 6000);
            if (pulseLen > 2000) {
                int lengths[] = new int[12];
                for (int i = 0; i < 12; i++) {
                    lengths[i] = BoeBot.pulseIn(15, false, 3000);
                }
                int bits = 0;

                for (int i = 0; i < 12; i++) {
                    if (lengths[i] > 1100 && lengths[i] < 1300) {
                        bits = bits | (1 << i);
                    }
                }

                if (bits == 0b11110100) {
                    System.out.println("Vooruit");
                    movement.forward();
                }
                if (bits == 0b10110011) {
                    System.out.println("Rechts");
                    movement.turnRight();
                }
                if (bits == 0b10110100) {
                    System.out.println("Links");
                    movement.turnLeft();
                }
                if (bits == 0b11110101) {
                    System.out.println("Achteruit");
                    movement.reverse();
                }
                if (bits == 0b11100101) {
                    System.out.println("Stop");
                    movement.stop();
                }
            }
            BoeBot.wait(1);
        }


//        while (true) {
//            whiskerRight = !BoeBot.digitalRead(0);
//            whiskerLeft = !BoeBot.digitalRead(1);
//
//            if (whiskerLeft || whiskerRight) {
//                System.out.println("stop");
//                movement.reverse();
//                BoeBot.wait(1000);
//                movement.turnLeft();
//            }
//            else {
//                movement.forward();
//            }
//
//            BoeBot.wait(1);
//        }
    }

    private static void setModes() {
        // Inputs
        BoeBot.setMode(0, PinMode.Input);
        BoeBot.setMode(1, PinMode.Input);

        BoeBot.setMode(15, PinMode.Input);


        // Outputs
//        BoeBot.setMode(4, PinMode.Output);
    }
}
