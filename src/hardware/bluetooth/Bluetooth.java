package hardware.bluetooth;

import TI.BoeBot;
import TI.SerialConnection;
import controllers.MovementController;
import hardware.Updatable;
import hardware.led.NeoPixel;

public class Bluetooth {

    private SerialConnection serial;
    private MovementController movementController;

    public Bluetooth(SerialConnection serial, MovementController movementController) {
        this.serial = serial;
        this.movementController = movementController;
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
}
