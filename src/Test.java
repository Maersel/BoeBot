//import TI.BoeBot;
//import TI.SerialConnection;
//import controllers.LineFollower;
//import controllers.MovementController;
//import hardware.Updatable;
//import hardware.bluetooth.Bluetooth;
//import hardware.button.Button;
//import hardware.gripper.Gripper;
//import hardware.led.NeoPixel;
//import hardware.linesensor.InfraRed;
//import hardware.motor.GripperMotor;
//import hardware.motor.MovementMotor;
//import hardware.whisker.Whisker;
//
//import java.util.ArrayList;
//
//public class Test {
//
//    public static void main(String[] args) {
//        Test test = new Test();
//        test.init();
//        test.run();
//    }
//
//    public final int MOTOR_PIN_LEFT = 12;
//    public final int MOTOR_PIN_RIGHT = 13;
//
//    private ArrayList<Updatable> devices;
//
//    private Bluetooth bluetooth;
//    private MovementController movementController;
//    private MovementMotor motorLeft;
//    private MovementMotor motorRight;
//
//
//
//
//    public void init() {
//
//        this.motorLeft = new hardware.motor.MovementMotor(MOTOR_PIN_LEFT, true);
//        this.motorRight = new hardware.motor.MovementMotor(MOTOR_PIN_RIGHT, false);
//        this.movementController = new controllers.MovementController(motorLeft, motorRight);
//        this.bluetooth = new Bluetooth(new SerialConnection(), this.movementController, this.sta);
//
//
//        this.devices = new ArrayList<>();
//        this.devices.add(this.motorLeft);
//        this.devices.add(this.motorRight);
////        this.bluetooth = new Bluetooth(new SerialConnection(), this.movementController);
//
//
//
//    }
//
//    private void run() {
//        while (true) {
////            this.bluetooth.echoCode();
////            this.movementController.forward();
//            this.bluetooth.remote();
//
//            for (Updatable device : devices) {
//                device.update();
//            }
//
//            BoeBot.wait(1);
//        }
//    }
//
//}
