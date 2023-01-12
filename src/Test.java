//import TI.BoeBot;
//import TI.SerialConnection;
//import TI.Timer;
//import controllers.AddDelay;
//import controllers.Delay;
//import controllers.MovementController;
//import controllers.TimerCallback;
//import hardware.Updatable;
//import hardware.bluetooth.Bluetooth;
//import hardware.gripper.Gripper;
//import hardware.led.NeoPixel;
//import hardware.motor.GripperMotor;
//import hardware.motor.MovementMotor;
//
//import java.util.ArrayList;
//
//public class Test implements AddDelay {
//
//    public static void main(String[] args) {
//        Test test = new Test();
//        test.init();
//        test.run();
//    }
//
//    public final int MOTOR_PIN_LEFT = 12;
//    public final int MOTOR_PIN_RIGHT = 13;
//    private Bluetooth bluetooth;
//    public final int GRIPPER_PIN = 11;
//    private NeoPixel neoPixel;
//
//    private final Timer t1 = new Timer(800);
//    private final Timer t2 = new Timer(600);
//
//    private ArrayList<Updatable> devices;
//
//    private MovementController movementController;
//    private MovementMotor motorLeft;
//    private MovementMotor motorRight;
//    private Gripper gripper;
//    private GripperMotor gripperMotor;
//
//    public void init() {
//
//        this.motorLeft = new hardware.motor.MovementMotor(MOTOR_PIN_LEFT, true);
//        this.motorRight = new hardware.motor.MovementMotor(MOTOR_PIN_RIGHT, false);
//        this.neoPixel = new NeoPixel(t1, t2, 0.05f);
//        this.movementController = new controllers.MovementController(motorLeft, motorRight, this , neoPixel);
//        this.gripperMotor = new hardware.motor.GripperMotor(GRIPPER_PIN);
//        this.gripper = new Gripper(gripperMotor);
//        this.bluetooth = new Bluetooth(new SerialConnection(), this.movementController, this.gripper, new NeoPixel());
//
//        this.devices = new ArrayList<>();
//        this.devices.add(this.motorLeft);
//        this.devices.add(this.motorRight);
//
//        this.devices.add(this.neoPixel);
//
//    }
//
//    private void run() {
//        while (true) {
////            this.bluetooth.echoCode();
//
//
//
//            for (Updatable device : devices) {
//                device.update();
//            }
//            BoeBot.wait(1);
//        }
//
//    }
//
//    @Override
//    public void addDelay(String name, int time, TimerCallback callback) {
//        this.devices.add(new Delay(name, this.devices, time, callback));
//    }
//}