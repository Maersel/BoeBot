package hardware.bluetooth;

import TI.SerialConnection;
import TI.Timer;
import controllers.MovementController;
import controllers.StateController;
import hardware.CheckState;
import hardware.gripper.Gripper;
import hardware.led.NeoPixel;


public class Bluetooth
//        implements CheckState
{

    private final SerialConnection serial;
    private final MovementController movementController;
    private final Gripper gripper;
    private final NeoPixel neoPixel;
//    private final StateController stateController;
//    private final Callback callback;


    private final int forwardKey = 1;
    private final int backwardKey = 'd';
    private final int turnLeftKey = 's';
    private final int rightTurnKey = 'f';
    private final int correctRightKey = 'g';
    private final int correctLeftKey = 'a';
    private final int boosyKey = 'r';
//    private final StateController stateController;
//    private final Callback callback;
    private final int lineFollowModeChangeKey = '0';
    private final int neoPixeleKey1 = '1';
    private final int neoPixeleKey2 = '2';
    private final int close = '-';
    private final int open = '=';
    private boolean lineFollowMode = false;
    private int neopixel = 0;
    private boolean neopixelOn = false;

    Timer t = new Timer(230);

    public Bluetooth(SerialConnection serial
            , MovementController movementController
            , Gripper gripper
            , NeoPixel neoPixel
//            , StateController stateController, Callback callback
    ) {
        this.serial = serial;
        this.movementController = movementController;
        this.gripper = gripper;
        this.neoPixel = neoPixel;
//        this.stateController = stateController;
//        this.callback = callback;
        t.mark();
    }


    public void echoCode() {
        SerialConnection serial = new SerialConnection(115200);
        while (true) {
            if (serial.available() > 0) {
                int data = serial.readByte();
                serial.writeByte(data); // Echo data
                System.out.println("Received: " + data);
            }
        }
    }

    public void remote() {

        if (serial.available() > 0) {
            int data = serial.readByte();

            t.mark();

            // ----- movementcontrols -----
            if (data == forwardKey) {
                movementController.forward();
                System.out.println("forward");
            } else if (data == backwardKey) {
                movementController.backwards();
                System.out.println("backwards");
            } else if (data == correctLeftKey) {
                movementController.correctLeft();
                System.out.println("correctLeft");
            } else if (data == correctRightKey) {
                movementController.correctRight();
                System.out.println("correctRight");
            } else if (data == boosyKey) {
                movementController.boosy();
                System.out.println("boosy");
            } else if (data == turnLeftKey) {
                movementController.turnLeft();
                System.out.println("turnLeft");
            } else if (data == rightTurnKey) {
                movementController.turnRight();
                System.out.println("turnRight");
            }
            // ------------------------------


            // lineFollowModeChange doet op het moment nog niks
            else if (data == lineFollowModeChangeKey) {
                lineFollowMode = !lineFollowMode;
                System.out.println("lineFollowModeChange");
            }


            // ----- neopixels -----
            else if (data == neoPixeleKey1) {
                if (neopixel != 1) {
                    neoPixel.setColour(1f, true);
                    neopixel = 1;
                    neopixelOn = true;
                } else {
                    neopixelOn = !neopixelOn;
                    neoPixel.setColour(1f, neopixelOn);
                }
                System.out.println("neoPixeleKey1");
            }

            else if (data == neoPixeleKey2) {
                if (neopixel != 2) {
                    neoPixel.setColour(0.5f, true);
                    neopixel = 2;
                    neopixelOn = true;
                } else {
                    neopixelOn = !neopixelOn;
                    neoPixel.setColour(0.2f, neopixelOn);
                }
                System.out.println("neoPixeleKey2");
            }
            // --------------------


            // ----- gripper -----
            else if (data == open) {
                gripper.open();
                System.out.println("open");
            } else if (data == close) {
                gripper.close();
                System.out.println("close");
            }
            // --------------------

        }
        // lineFollowMode doet op het moment nog niks
        else if (lineFollowMode) {
        }

        // dit zorgt er voor dat je de besturings knoppen ingedrukt moet houden om de boebot te laten rijden
        else {
            if (t.timeout()) {
                movementController.stop();

                if (!neopixelOn){
                    neoPixel.reset();
                }

                System.out.println("stop");
            }
        }

    }

//    @Override
//    public boolean isInValidState() {
//        return (this.stateController.currentState() == Configuration.REMOTE_STATE);
//    }

}



