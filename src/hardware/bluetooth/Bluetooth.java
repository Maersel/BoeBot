package hardware.bluetooth;

import TI.SerialConnection;
import controllers.Configuration;
import controllers.MovementController;

import javax.swing.plaf.nimbus.State;

public class Bluetooth implements CheckState {

    private final SerialConnection serial;
    private final MovementController movementController;


    private final int forwardKey = 'e';
    private final int backwardKey = 'd';
    private final int turnLeftKey = 's';
    private final int rightTurnKey = 'f';
    private final int correctRightKey = 'g';
    private final int correctLeftKey = 'a';
    private final int boosyKey = 'r';
    private final int lineFollowModeChangeKey = '0';
    private final int neoPixeleKey = '1';
    private final int neoPixeleKey2 = '2';
    private boolean lineFollowMode = false;
    private boolean neopixel1 = false;
    private boolean neopixel2 = false;

    Timer t = new Timer(100);

    public Bluetooth(SerialConnection serial, MovementController movementController,
                     StateController stateController, Callback callback) {
        this.serial = serial;
        this.movementController = movementController;
        t.mark();
        this.stateController = stateController;
        this.callback = callback;
    }


    public void echoCode() {
        if (serial.available() > 0) {
            int data = serial.readByte();
            serial.writeByte(data); // Echo data
            System.out.println("Received: " + data);
        }
    }

    public void remote(){

        if (serial.available() > 0) {
            int data = serial.readByte();

            t.mark();

            if (data == forwardKey)
            {
                movementController.forward();
                System.out.println("forward");
            }
            else if (data == backwardKey)
            {
                movementController.backwards();
                System.out.println("backwards");
            }
            else if (data == correctLeftKey)
            {
                movementController.correctLeft();
                System.out.println("correctLeft");
            }
            else if (data == correctRightKey)
            {
                movementController.correctRight();
                System.out.println("correctRight");
            }
            else if (data == boosyKey)
            {
                movementController.boosy();
                System.out.println("boosy");
            }
            else if (data == turnLeftKey) {
                movementController.turnLeft();
                System.out.println("turnLeft");
            }
            else if (data == rightTurnKey) {
                movementController.turnRight();
                System.out.println("turnRight");
            }
            else if (data == lineFollowModeChangeKey)
            {
                lineFollowMode = !lineFollowMode;
                System.out.println("lineFollowModeChange");
            }
            else if (data == neoPixeleKey)
            {
                neopixel1 = !neopixel1;
                NeoPixel.setColour(1f, neopixel1);
                System.out.println("neoPixeleKey");
            }
            else if (data == neoPixeleKey2)
            {
                neopixel2 = !neopixel2;
                NeoPixel.setColour(0.5f, neopixel2);
                System.out.println("neoPixeleKey");
            }
            else{
                movementController.stop();
                System.out.println("stop");
            }
        }
        else if (lineFollowMode)
        {

        }
        else
        {
            if (t.timeout()) {
                movementController.stop();
                System.out.println("stop");
            }
        }
    }

    @Override
    public boolean isInValidState() {
        return (this.stateController.currentState() == Configuration.REMOTE_STATE);
    }
}
