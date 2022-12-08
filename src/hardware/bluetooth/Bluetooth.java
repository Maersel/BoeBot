package hardware.bluetooth;

import TI.BoeBot;
import TI.SerialConnection;
import controllers.Configuration;
import controllers.MovementController;
import controllers.StateController;
import hardware.CheckState;
import hardware.Updatable;
import hardware.led.NeoPixel;

import javax.swing.plaf.nimbus.State;

public class Bluetooth implements CheckState {

    private SerialConnection serial;
    private MovementController movementController;
    private StateController stateController;

    private Callback callback;

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

            if (data == Configuration.EMERGENCY_STATE || data == Configuration.GOAT_SCARING_STATE ||
                    data == Configuration.REMOTE_STATE || data == Configuration.REST_STATE ||
                    data == Configuration.LINE_FOLLOWING_STATE || data == Configuration.SLOWING_DOWN_STATE) { // VERANDER STATE COMMANDS
                this.callback.onBlueToothInput(data);
            }

            if (!isInValidState()) return;

            if (data == 101) {
                movementController.forward();
                System.out.println("forward");
            }else if (data == 100) {
                movementController.backwards();
                System.out.println("backwards");
            }else if (data == 97) {
                movementController.correctLeft();
                System.out.println("correctLeft");
            }else if (data == 103) {
                movementController.correctRight();
                System.out.println("correctRight");
            }else if (data == 32) {
                movementController.boosy();
                System.out.println("boosy");
            }else if (data == 115) {
                movementController.turnLeft();
                System.out.println("turnLeft");
            }else if (data == 102) {
                movementController.turnRight();
                System.out.println("turnRight");
            }else{
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
