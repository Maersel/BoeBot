import TI.BoeBot;
import TI.PinMode;
import controllers.*;
import controllers.pathfinding.Pathfinder;
import hardware.Updatable;
import hardware.bluetooth.Bluetooth;
import hardware.button.Button;
import hardware.buzzer.Buzzer;
import hardware.gripper.Gripper;
import hardware.linesensor.InfraRed;
import hardware.motor.GripperMotor;
import hardware.motor.MovementMotor;
import hardware.ultrasonic.UltraSonic;
import hardware.whisker.Whisker;

import java.util.ArrayList;

public class BoebotMain implements hardware.whisker.Callback, hardware.button.Callback,
        hardware.ultrasonic.Callback, AddDelay {
    public static void main(String[] args) {
        BoebotMain boebot = new BoebotMain();
        boebot.init();
        boebot.run();
    }

    public final int GRIPPER_PIN = 0;
    public final int MOTOR_PIN_LEFT = 12;
    public final int MOTOR_PIN_RIGHT = 13;
    public final int SENSOR_PIN_LEFT = 1;
    public final int SENSOR_PIN_RIGHT = 3;
    public final int SENSOR_PIN_MIDDLE = 2;

    public final int WHISKER_PIN_LEFT = 0;
    public final int WHISKER_PIN_RIGHT = 1;

    public final int EMERGENCY_BUTTON_PIN = 0;

    public final int BUZZER_PIN = 3;

    public final int ULTRASONIC_ECHO_PIN_FRONT = 8;
    public final int ULTRASONIC_TRIGGER_PIN_FRONT = 2;

    public final int ULTRASONIC_ECHO_PIN_REAR = 9;
    public final int ULTRASONIC_TRIGGER_PIN_REAR = 3;

    private Pathfinder pathfinder;

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

    private Buzzer buzzer;

    private GoatScering goatScering;

    private UltraSonic ultraSonicFront;
    private UltraSonic ultraSonicRear;
    private RemoteController remoteController;
    private StateController stateController;

    private Bluetooth bluetooth;

    public void init() {
        this.pathfinder = new Pathfinder();

        BoeBot.setMode(GRIPPER_PIN, PinMode.Output); // FIX
        BoeBot.setMode(EMERGENCY_BUTTON_PIN, PinMode.Input); // FIX

        this.gripperMotor = new GripperMotor(GRIPPER_PIN);
        this.gripper = new Gripper(gripperMotor);

        this.motorLeft = new MovementMotor(MOTOR_PIN_LEFT, true);
        this.motorRight = new MovementMotor(MOTOR_PIN_RIGHT, false);
        this.movementController = new MovementController(motorLeft, motorRight, this);

        this.whiskerLeft = new Whisker(WHISKER_PIN_LEFT, this);
        this.whiskerRight = new Whisker(WHISKER_PIN_RIGHT, this);

        this.sensorLeft = new InfraRed(SENSOR_PIN_LEFT);
        this.sensorRight = new InfraRed(SENSOR_PIN_RIGHT);
        this.sensorMiddle = new InfraRed(SENSOR_PIN_MIDDLE);

        this.lineFollower = new LineFollower(this.movementController, this, this.pathfinder, this.sensorLeft, this.sensorRight, this.sensorMiddle, this.gripper);

        this.emergencyButton = new Button(EMERGENCY_BUTTON_PIN, this);

        this.stateController = new StateController(this.lineFollower, this.movementController,
                this.bluetooth, this.ultraSonicFront);

        this.buzzer = new Buzzer(BUZZER_PIN);

        this.goatScering = new GoatScering(this.movementController, this.buzzer);

        this.ultraSonicFront = new UltraSonic(ULTRASONIC_ECHO_PIN_FRONT, ULTRASONIC_TRIGGER_PIN_FRONT, this);
        this.ultraSonicRear = new UltraSonic(ULTRASONIC_ECHO_PIN_REAR, ULTRASONIC_TRIGGER_PIN_REAR, this);

        this.devices = new ArrayList<>();
        this.devices.add(this.gripperMotor);

        this.devices.add(this.motorLeft);
        this.devices.add(this.motorRight);

//        this.devices.add(this.whiskerLeft);
//        this.devices.add(this.whiskerRight);

        // de LineFollower class MOET ALTIJD NA de sensoren in de devices lijst
        this.devices.add(this.sensorLeft);
        this.devices.add(this.sensorRight);
        this.devices.add(this.sensorMiddle);
        this.devices.add(this.lineFollower);
//
//        this.devices.add(this.emergencyButton);
//
//        this.devices.add(this.buzzer);
//
//        this.devices.add(this.ultraSonicFront);
        this.devices.add(this.ultraSonicRear);

        this.lineFollower.setRoute(8, 15);
        this.lineFollower.addRouteCommand(RouteOptions.PICK_UP);
    }

    private void run() {
        while (true) {
            for (int i = devices.size() - 1; i >= 0; i--) {
                devices.get(i).update();
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
        if (source == this.emergencyButton) {
            System.out.println("BUTTON PRESSED");
            this.movementController.emergencyStop();
        }
    }

    @Override
    public void onUltraSonic() {
        System.out.println("piemols");
    }

    @Override
    public void addDelay(String name, int time, TimerCallback callback) {
        this.devices.add(new Delay(name, this.devices, time, callback));
    }
}
