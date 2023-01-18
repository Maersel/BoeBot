package hardware.bluetooth;

import TI.SerialConnection;
import controllers.Configuration;
import controllers.MovementController;
import controllers.StateController;
import hardware.CheckState;

import javax.swing.plaf.nimbus.State;

public class Bluetooth implements CheckState {

    private final SerialConnection serial;
    private final MovementController movementController;


    private final char FORWARD_KEY = 'e';
    private final char BACKWARDS_KEY = 'd';
    private final char TURN_LEFT_KEY = 's';
    private final char TURN_RIGHT_KEY = 'f';
    private final char CORRECT_RIGHT_KEY = 'g';
    private final char CORRECT_LEFT_KEY = 'a';
    private final char BOOST_KEY = 'r';
    private final StateController stateController;
    private final Callback callback;

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

            if (data == FORWARD_KEY)
            {
                movementController.forward();
                System.out.println("forward");
            }
            else if (data == BACKWARDS_KEY)
            {
                movementController.backwards();
                System.out.println("backwards");
            }
            else if (data == CORRECT_LEFT_KEY)
            {
                movementController.correctLeft();
                System.out.println("correctLeft");
            }
            else if (data == CORRECT_RIGHT_KEY)
            {
                movementController.correctRight();
                System.out.println("correctRight");
            }
            else if (data == BOOST_KEY)
            {
                movementController.boosy();
                System.out.println("boosy");
            }
            else if (data == TURN_LEFT_KEY) {
                movementController.turnLeft();
                System.out.println("turnLeft");
            }
            else if (data == TURN_RIGHT_KEY) {
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
