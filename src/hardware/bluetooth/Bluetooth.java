package hardware.bluetooth;

import TI.SerialConnection;
import controllers.Configuration;
import controllers.LineFollower;
import controllers.MovementController;
import controllers.StateController;
import hardware.Updatable;
import hardware.gripper.Gripper;
import hardware.led.NeoPixel;


public class Bluetooth implements Updatable
//        implements CheckState
{

    private final SerialConnection serial;
    private final MovementController movementController;
    private final StateController stateController;
    private final LineFollower lineFollower;
    private final Gripper gripper;
    private final NeoPixel neoPixel;


    // ----- States -----
    private boolean mapMode = true;
    private boolean remoteMode = false;
    // ------------------

    private int alpha;

    private int neopixelId = 0;
    private boolean neopixelOn = false;

//    Timer t = new Timer(230);

    private int point1;
    private int point2;

    public Bluetooth(SerialConnection serial
            , MovementController movementController
            , StateController stateController
            , LineFollower lineFollower
            , Gripper gripper
            , NeoPixel neoPixel
    ) {
        this.serial = serial;
        this.movementController = movementController;
        this.stateController = stateController;
        this.lineFollower = lineFollower;
        this.gripper = gripper;
        this.neoPixel = neoPixel;

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

    @Override
    public void update() {

        if (serial.available() > 0) {
            int data = serial.readByte();

            if (mapMode) {

                if (data == 50) {
                    mapMode = false;
                    remoteMode = true;
                    System.out.println("mapMode = false\tremoteMode = true");
                }

                if (data <= 26) {
                    if (alpha == 0) {

                    }

                }
                if (alpha == 2){
                    lineFollower.setRoute(point1, point2);
                    stateController.changeState(Configuration.LINE_FOLLOWING_STATE);
                }
            }

            if (remoteMode) {

                // ----- State changes -----
                // lineFollowModeChange doet op het moment nog niks
                if (data == 50) {
                    mapMode = true;
                    remoteMode = false;
                    System.out.println("remoteMode = false\tmapMode = true");
                }
                // -------------------------


//                t.mark();

                // ----- movementcontrols -----
                if (data == 101) {
                    movementController.forward();
                    System.out.println("forward");
                } else if (data == 102) {
                    movementController.backwards();
                    System.out.println("backwards");
                } else if (data == 103) {
                    movementController.correctToTheLeft();
                    System.out.println("correctLeft");
                } else if (data == 104) {
                    movementController.correctToTheRight();
                    System.out.println("correctRight");
                } else if (data == 105) {
                    movementController.boosy();
                    System.out.println("boosy");
                } else if (data == 106) {
                    movementController.turnLeft();
                    System.out.println("turnLeft");
                } else if (data == 107) {
                    movementController.turnRight();
                    System.out.println("turnRight");
                }
                // ------------------------------


                // ----- neopixels -----
                else if (data == 111) {
                    if (neopixelId != 1) {
                        neoPixel.setColour(1f, true);
                        neopixelId = 1;
                        neopixelOn = true;
                    } else {
                        neopixelOn = !neopixelOn;
                        neoPixel.setColour(1f, neopixelOn);
                    }
                    System.out.println("neoPixeleKey1");
                } else if (data == 112) {
                    if (neopixelId != 2) {
                        neoPixel.setColour(0.5f, true);
                        neopixelId = 2;
                        neopixelOn = true;
                    } else {
                        neopixelOn = !neopixelOn;
                        neoPixel.setColour(0.2f, neopixelOn);
                    }
                    System.out.println("neoPixeleKey2");
                }
                // --------------------


                // ----- gripper -----
                else if (data == 121) {
                    gripper.open();
                    System.out.println("open");
                } else if (data == 122) {
                    gripper.close();
                    System.out.println("close");
                }
                // --------------------


                // dit zorgt er voor dat je de besturings knoppen ingedrukt moet houden om de boebot te laten rijden
//                else {
//                    if (t.timeout()) {
//                        movementController.stop();
//
//                        if (!neopixelOn) {
//                            neoPixel.reset();
//                        }
//
//                        System.out.println("stop");
//                    }
//                }

            }

        }

    }

//    @Override
//    public boolean isInValidState() {
//        return (this.stateController.currentState() == Configuration.REMOTE_STATE);
//    }

}



