import TI.BoeBot;
import TI.SerialConnection;
import controllers.MovementController;
import hardware.Updatable;
import hardware.bluetooth.Bluetooth;
import hardware.gripper.Gripper;
import hardware.led.NeoPixel;
import hardware.motor.GripperMotor;
import hardware.motor.MovementMotor;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        Test test = new Test();
        test.init();
        test.run();
    }

    public final int MOTOR_PIN_LEFT = 12;
    public final int MOTOR_PIN_RIGHT = 13;
    public final int GRIPPER_PIN = 11;
    private Bluetooth bluetooth;
    private NeoPixel neoPixel;


    private ArrayList<Updatable> devices;


    private MovementController movementController;
    private MovementMotor motorLeft;
    private MovementMotor motorRight;
    private Gripper gripper;
    private GripperMotor gripperMotor;




    public void init() {

        this.motorLeft = new hardware.motor.MovementMotor(MOTOR_PIN_LEFT, true);
        this.motorRight = new hardware.motor.MovementMotor(MOTOR_PIN_RIGHT, false);
        this.movementController = new controllers.MovementController(motorLeft, motorRight);
        this.gripperMotor = new hardware.motor.GripperMotor(GRIPPER_PIN);
        this.gripper = new Gripper(gripperMotor);
        this.bluetooth = new Bluetooth(new SerialConnection(), this.movementController, this.gripper);
        this.neoPixel = new NeoPixel();


        this.devices = new ArrayList<>();
        this.devices.add(this.motorLeft);
        this.devices.add(this.motorRight);
        this.devices.add(this.neoPixel);

//        this.bluetooth = new Bluetooth(new SerialConnection(), this.movementController);



    }

    private void run() {
        while (true) {
//            this.bluetooth.echoCode();

            this.bluetooth.remote();

            for (Updatable device : devices) {
                device.update();
            }

            BoeBot.wait(1);
        }
    }

}
