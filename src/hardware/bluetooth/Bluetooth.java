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

    public Bluetooth(SerialConnection serial, MovementController movementController,
                     StateController stateController, Callback callback) {
        this.serial = serial;
        this.movementController = movementController;
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
//            else if ()
//            {
//
//            }
            else{
                movementController.stop();
                System.out.println("stop");
            }
        }
//        else if () {
//
//        }
        else {
            movementController.stop();
            System.out.println("stop");
        }
    }

    @Override
    public boolean isInValidState() {
        return (this.stateController.currentState() == Configuration.REMOTE_STATE);
    }
}
