import TI.BoeBot;
import TI.PinMode;
import controllers.LineFollower;
import controllers.MovementController;
import controllers.RemoteController;
import controllers.StateController;
import hardware.Updatable;
import hardware.button.Button;
import hardware.gripper.Gripper;
import hardware.linesensor.InfraRed;
import hardware.motor.GripperMotor;
import hardware.motor.MovementMotor;
import hardware.ultrasonic.UltraSonic;
import hardware.whisker.Whisker;

import java.rmi.Remote;
import java.util.ArrayList;

public class BoebotMain implements hardware.whisker.Callback, hardware.button.Callback,
        hardware.ultrasonic.Callback {
    public static void main(String[] args) {
        BoebotMain boebot = new BoebotMain();
        boebot.init();
        boebot.run();
    }

    public final int GRIPPER_PIN = 6;
    public final int MOTOR_PIN_LEFT = 12;
    public final int MOTOR_PIN_RIGHT = 13;
    public final int SENSOR_PIN_LEFT = 1;
    public final int SENSOR_PIN_RIGHT = 3;
    public final int SENSOR_PIN_MIDDLE = 2;

    public final int WHISKER_PIN_LEFT = 0;
    public final int WHISKER_PIN_RIGHT = 1;
    public final int EMERGENCY_BUTTON_PIN = 2;

    private ArrayList<Updatable> devices;
    private Gripper gripper;
    private GripperMotor gripperMotor;

    private MovementController movementController;
    private MovementMotor motorLeft;
    private MovementMotor motorRight;

    private LineFollower lineFollower;
    private InfraRed sensorLeft;
    private InfraRed sensorRight;
    private InfraRed sensorMiddle;

    private Whisker whiskerLeft;
    private Whisker whiskerRight;

    private Button emergencyButton;

    private UltraSonic ultraSonic;
    private RemoteController remoteController;
    private StateController stateController;

    public void init() {
        BoeBot.setMode(GRIPPER_PIN, PinMode.Output); // FIX
        BoeBot.setMode(EMERGENCY_BUTTON_PIN, PinMode.Output); // FIX

        this.gripperMotor = new hardware.motor.GripperMotor(GRIPPER_PIN);
        this.gripper = new Gripper(gripperMotor);

        this.motorLeft = new hardware.motor.MovementMotor(MOTOR_PIN_LEFT, false);
        this.motorRight = new hardware.motor.MovementMotor(MOTOR_PIN_RIGHT, true);
        this.movementController = new controllers.MovementController(motorLeft, motorRight);

        this.whiskerLeft = new Whisker(WHISKER_PIN_LEFT, this);
        this.whiskerRight = new Whisker(WHISKER_PIN_RIGHT, this);

        this.sensorLeft = new InfraRed(SENSOR_PIN_LEFT);
        this.sensorRight = new InfraRed(SENSOR_PIN_RIGHT);
        this.sensorMiddle = new InfraRed(SENSOR_PIN_MIDDLE);

        this.lineFollower = new LineFollower(this.movementController, this.sensorLeft, this.sensorRight, this.sensorMiddle, this.stateController);

//        this.emergencyButton = new Button(EMERGENCY_BUTTON_PIN, this);

        this.stateController = new StateController(this.lineFollower, this.movementController,
                this.remoteController, this.ultraSonic);

        this.devices = new ArrayList<>();
//        this.devices.add(this.gripperMotor);

        this.devices.add(this.motorLeft);
        this.devices.add(this.motorRight);

//        this.devices.add(this.whiskerLeft);
//        this.devices.add(this.whiskerRight);

        // de LineFollower class MOET ALTIJD NA de sensoren in de devices lijst
        this.devices.add(this.sensorLeft);
        this.devices.add(this.sensorRight);
        this.devices.add(this.sensorMiddle);
        this.devices.add(this.lineFollower);

//        this.devices.add(this.emergencyButton);
    }

    private void run() {
        while (true) {
//            if (Math.random() < 0.0005) {
//                if (Math.random() < 0.5) {
//                    this.gripper.open();
//                    System.out.println("open");
//                } else {
//                    this.gripper.close();
//                    System.out.println("sluit");
//                }
//            }

            for (Updatable device : devices) {
                device.update();
            }

            BoeBot.wait(1);
        }
    }

    @Override
    public void onWhiskerContact(Whisker source) {
        if (source == this.whiskerLeft) {
            System.out.println("whisker links contact");
            this.movementController.stop();
        }
        if (source == this.whiskerRight) {
            System.out.println("whisker rechts contact");
            this.movementController.stop();
        }
    }

    @Override
    public void onButtonPress(Button source) {
//        if (source == this.emergencyButton) {
//            this.movementController.emergencyStop();
//        }
    }

    @Override
    public void onUltraSonic(int distance, UltraSonic source) {

    }
}
