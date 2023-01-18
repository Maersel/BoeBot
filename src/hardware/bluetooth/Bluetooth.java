package hardware.bluetooth;

import TI.SerialConnection;
import controllers.*;
import hardware.Updatable;
import hardware.gripper.Gripper;
import hardware.led.NeoPixel;

import java.util.ArrayList;


public class Bluetooth implements Updatable, BluetoothCallback
//        , CheckState
{

    private final SerialConnection serial = new SerialConnection();
    private final MovementController movementController;
    private final StateController stateController;
    private final LineFollower lineFollower;
    private final Gripper gripper;
    private final NeoPixel neoPixel;


    // ----- States -----
    private boolean mapMode = true;
    private boolean remoteMode = false;
    // ------------------


    private int neopixelId = 0;
    private boolean neopixelOn = false;

    //    Timer t = new Timer(230);
    private int alpha = 0;
    private int point1;
    private int point2;
    private int mode;

    private Boolean isDone = true;

    private ArrayList<Integer> pointsQueue;

    public Bluetooth(MovementController movementController
            , StateController stateController
            , LineFollower lineFollower
            , Gripper gripper
            , NeoPixel neoPixel
    ) {
        this.movementController = movementController;
        this.stateController = stateController;
        this.lineFollower = lineFollower;
        this.gripper = gripper;
        this.neoPixel = neoPixel;
        pointsQueue = new ArrayList<>();
    }

//    private void pointWrite(int data) {
//        if (alpha == 0) {
//            point1 = data;
//            System.out.println(point1);
//            alpha++;
//        } else if (alpha == 1) {
//            point2 = data;
//            System.out.println(point2);
//            alpha++;
//        } else if (alpha == 2) {
//            mode = data;
//            System.out.println(mode);
//            alpha++;
//        }
//    }

    @Override
    public void update() {

        if (serial.available() > 0) {
            int data = serial.readByte();

            if (mapMode) {

                if (data == 50) {
                    mapMode = false;
                    remoteMode = true;
                    System.out.println("mapMode = false\tremoteMode = true");
                } else if (data <= 45) {

//                    pointWrite(data);
//                    if (alpha == 3) {
//                        if (mode == 41) {
//                            lineFollower.setRoute(point1, point2, RouteOptions.PICK_UP);
//                        } else if (mode == 42) {
//                            lineFollower.setRoute(point1, point2, RouteOptions.DROP);
//                        } else {
//                            System.out.println("compleet f*cked");
//                        }
//                        System.out.println(point1 + "\t" + point2 + "\t" + mode);
//                        stateController.changeState(Configuration.LINE_FOLLOWING_STATE);
//                        alpha = 0;
//                        isDone = false;
//                    }

                    if (alpha == 0) {
                        point1 = data;
                        alpha++;
                    } else if (alpha == 1) {
                        point2 = data;
                        alpha++;
                    } else if (alpha == 2) {
                        mode = data;
                        alpha++;
                    }
                    if (alpha == 3) {
                        pointsQueue.add(point1);
                        pointsQueue.add(point2);
                        pointsQueue.add(mode);
                        if (isDone) {
                            checkQueue();
                            isDone = false;
                            stateController.changeState(Configuration.LINE_FOLLOWING_STATE);
                        }
                        alpha = 0;

                        System.out.println("Que add " + point1 + " | " + point2 + " | " + mode);
                        int a = pointsQueue.size() / 3;
                        System.out.println("queue size " + a);
                    }

                }
            }

            if (remoteMode) {

                switch (data) {

                    // ----- State changes -----
                    // lineFollowModeChange doet op het moment nog niks
                    case 50:
                        mapMode = true;
                        remoteMode = false;
                        System.out.println("remoteMode = false\tmapMode = true");
                        break;
                    // -------------------------

//                    t.mark();

                    // ----- movementcontrols -----
                    case 101:
                        movementController.forward();
                        System.out.println("forward");
                        break;
                    case 102:
                        movementController.backwards();
                        System.out.println("backwards");
                        break;
                    case 103:
                        movementController.correctToTheLeft();
                        System.out.println("correctLeft");
                        break;
                    case 104:
                        movementController.correctToTheRight();
                        System.out.println("correctRight");
                        break;
                    case 105:
                        movementController.boosy();
                        System.out.println("boosy");
                        break;
                    case 106:
                        movementController.turnLeft();
                        System.out.println("turnLeft");
                        break;
                    case 107:
                        movementController.turnRight();
                        System.out.println("turnRight");
                        break;
                    // ------------------------------


                    // ----- neopixels -----
//                    case 111:
//                        if (neopixelId != 1) {
//                            neoPixel.setColour(1f, true);
//                            neopixelId = 1;
//                            neopixelOn = true;
//                        } else {
//                            neopixelOn = !neopixelOn;
//                            neoPixel.setColour(1f, neopixelOn);
//                        }
//                        System.out.println("neoPixeleKey1");
//                        break;
//                    case 112:
//                        if (neopixelId != 2) {
//                            neoPixel.setColour(0.5f, true);
//                            neopixelId = 2;
//                            neopixelOn = true;
//                        } else {
//                            neopixelOn = !neopixelOn;
//                            neoPixel.setColour(0.2f, neopixelOn);
//                        }
//                        System.out.println("neoPixeleKey2");
//                        break;
                    // --------------------


                    // ----- gripper -----
                    case 121:
                        gripper.open();
                        System.out.println("open");
                        break;
                    case 122:
                        gripper.close();
                        System.out.println("close");
                        break;
                    // --------------------


                    // dit zorgt er voor dat je de besturings knoppen ingedrukt moet houden om de boebot te laten rijden
//                    default:
//                        if (t.timeout()) {
//                            movementController.stop();
//                            System.out.println("stop");
//                        }
//                        break;
                }

            }

        }

//    @Override
//    public boolean isInValidState() {
//        return (this.stateController.currentState() == Configuration.REMOTE_STATE);
//    }


    }


    public void checkQueue() {
        if (pointsQueue.size() > 0) {
            if (pointsQueue.get(2) == 41) {
                lineFollower.setRoute(pointsQueue.get(0), pointsQueue.get(1), RouteOptions.PICK_UP);
            } else if (pointsQueue.get(2) == 42) {
                lineFollower.setRoute(pointsQueue.get(0), pointsQueue.get(1), RouteOptions.DROP);
            }
            pointsQueue.subList(0, 3).clear();
        } else {
            isDone = true;
        }
    }

    @Override
    public void lineFollowerQueue() {
        checkQueue();
    }
}



