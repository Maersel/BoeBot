import TI.BoeBot;
import TI.PinMode;
import TI.Servo;
import TI.Timer;

import java.awt.Color;
import java.time.Instant;
import java.util.Collections;

public class RobotMain {

    public static void main(String[] args) {
        setModes();
        remote();
    }

        public static void remote() {
            Gripper gripper = new Gripper(Configuration.gripperPin);

            MovementController movement = new MovementController(12, 13);
            System.out.println("Listening....");
            boolean isDriving = false;
            Timer t1 = new Timer(500);
            while (true) {

                if (t1.timeout() && isDriving) {
//                    BoeBot.freqOut(15, 1000, 500);
                    System.out.println("BEEP");
                    t1.mark();
                }

                int pulseLen = BoeBot.pulseIn(14, false, 6000);
                if (pulseLen > 2000) {
                    int lengths[] = new int[12];
                    for (int i = 0; i < 12; i++) {
                        lengths[i] = BoeBot.pulseIn(14, false, 3000);
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
                        isDriving = true;

                    }
                    if (bits == 0b10110011) {
                        System.out.println("Rechts");
                        movement.turnRight();
                        isDriving = false;

                    }
                    if (bits == 0b10110100) {
                        System.out.println("Links");
                        movement.turnLeft();
                        isDriving = false;
                    }
                    if (bits == 0b11110101) {
                        System.out.println("Achteruit");
                        movement.reverse();
                        isDriving = true;
                    }
                    if (bits == 0b11100101) {
                        System.out.println("Stop");
                        movement.stop();
                        isDriving = false;
                    }
                    if (bits == 0b11011111) {
                        System.out.println("Grijpen");
                        gripper.close();
                    }
                    if (bits == 0b11100000) {
                        System.out.println("Loslaten");
                        gripper.open();
                    }
                }

                BoeBot.wait(1);
            }
        }

    public static void setModes() {
        // Inputs
        BoeBot.setMode(6, PinMode.Output);
        BoeBot.setMode(1, PinMode.Input);

        BoeBot.setMode(14, PinMode.Input);
        BoeBot.setMode(15, PinMode.Input);


        // Outputs
//        BoeBot.setMode(4, PinMode.Output);
    }
}
