package controllers;

import TI.BoeBot;
import TI.PinMode;
import hardware.Updatable;
import hardware.gripper.Gripper;

public class RemoteController implements Updatable {
    private MovementController movementController;
    private StateController stateController;
    private Gripper gripper;


    public RemoteController(MovementController movementController, StateController stateController, Gripper gripper) {
        this.movementController = movementController;
        this.stateController = stateController;
        this.gripper = gripper;
    }

//    private void update() {
//        int pulseLen = BoeBot.pulseIn(14, false, 6000);
//        int bits = 0;
//
//        if (pulseLen > 2000) {
//            int lengths[] = new int[12];
//            for (int i = 0; i < 12; i++) {
//                lengths[i] = BoeBot.pulseIn(14, false, 3000);
//            }
//
//            for (int i = 0; i < 12; i++) {
//                if (lengths[i] > 1100 && lengths[i] < 1300) {
//                    bits = bits | (1 << i);
//                }
//            }
//        }

    @Override
    public void update() {
        System.out.println("youvhi told me to");
        BoeBot.setMode(15, PinMode.Input);
        System.out.println("youvhi told me to 2");
//        System.out.println(BoeBot.digitalRead(15));
        int pulseLen = BoeBot.pulseIn(15, false, 6000);
        System.out.println("youvhi told me to 3");
//        int lengths[] = new int[7];
//        int number = 0;

        if (pulseLen > 2000) {
            int lengths[] = new int[7];
            for (int i = 6; i >= 0; i--) {
                lengths[i] = BoeBot.pulseIn(15, false, 20000);
                System.out.print(lengths[i] + " ");
            }
            int number = 0;
            int i = 7;
            for (int length : lengths) {
                i--;
                if (length > 300 && 800 > length) {
                    length = 0;
                } else if (length > 1100 && 1400 > length) {
                    length = 1;
                } else {
                    number = 400;
                }
                number += length << i;
            }
            System.out.println(number);

//            for (int i = 6; i >= 0; i--) {
//                lengths[i] = BoeBot.pulseIn(15, false, 20000);
//                System.out.print(lengths[i]+" ");
//            }
//            int x = 7;
//            for (int length : lengths) {
//                x--;
//
//                if (length < 700 && length > 300) {
//                    length = 0;
//                    System.out.print(length+" ");
//                } else if (length < 1300) {
//                    length = 1;
//                    System.out.print(length+" ");
//                } else {
//                    length = 400;
//                }
//                number += length << x;
//            }

            System.out.println(number);
//        int bits = 0;

//        if ( bits > 0) {
//            System.out.println(Integer.toBinaryString(bits));
//        }
            //button overzicht
            switch (number) {
                case 21:
                    System.out.println("EmergencyStop");
                    this.movementController.emergencyStop();
                    break;
                case 88:
                    System.out.println("Vooruit");
                    this.movementController.forward();
                    break;
                case 18:
                    System.out.println("Rechts");
                    this.movementController.remoteTurnRight();
                    break;
                case 19:
                    System.out.println("Links");
                    this.movementController.remoteTurnLeft();
                    break;
                case 89:
                    System.out.println("Achteruit");
                    this.movementController.backwards();
                    break;
                case 4:
                    System.out.println("Stop");
                    this.movementController.stop();
                    break;
                case 3:
                    System.out.println("Grijpen");
                    this.gripper.close();
                    break;
                case 5:
                    System.out.println("Loslaten");
                    this.gripper.open();

//        else {
//            System.out.println("ELSE");
//        this.movementController.stop();
                    break;
            }

        }
    }

    public boolean remotePressed() {
        return false;
    }

    public Configuration changeState() {
        return Configuration.LINE_FOLLOWING_STATE;
    }
}
