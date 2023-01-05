import TI.BoeBot;
import TI.PinMode;
import TI.SerialConnection;
import TI.Timer;
import controllers.*;
import controllers.LineFollower;
import controllers.MovementController;
import controllers.RemoteController;
import controllers.StateController;
import hardware.Updatable;
import hardware.bluetooth.Bluetooth;
import hardware.button.Button;
import hardware.buzzer.Buzzer;
import hardware.gripper.Gripper;
import hardware.led.NeoPixel;
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

    public final int EMERGENCY_BUTTON_PIN = 0;

    public final int BUZZER_PIN = 3;

    public final int ULTRASONIC_ECHO_PIN = 8;
    public final int ULTRASONIC_TRIGGER_PIN = 2;

    private ArrayList<Updatable> devices;
    private Gripper gripper;
    private GripperMotor gripperMotor;

    private Bluetooth bluetooth;
    private NeoPixel neoPixel;

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

    private UltraSonic ultraSonic;
    private RemoteController remoteController;
    private StateController stateController;

    public void init() {
        BoeBot.setMode(GRIPPER_PIN, PinMode.Output); // FIX
        BoeBot.setMode(EMERGENCY_BUTTON_PIN, PinMode.Input); // FIX

        this.gripperMotor = new hardware.motor.GripperMotor(GRIPPER_PIN);
        this.gripper = new Gripper(gripperMotor);

        this.motorLeft = new hardware.motor.MovementMotor(MOTOR_PIN_LEFT, true);
        this.motorRight = new hardware.motor.MovementMotor(MOTOR_PIN_RIGHT, false);
        this.movementController = new controllers.MovementController(motorLeft, motorRight, neoPixel);

        this.bluetooth = new Bluetooth(new SerialConnection(), this.movementController, this.gripper, new NeoPixel());
        this.neoPixel = new NeoPixel(new Timer(800), new Timer(600), 0.05f);

        this.whiskerLeft = new Whisker(WHISKER_PIN_LEFT, this);
        this.whiskerRight = new Whisker(WHISKER_PIN_RIGHT, this);

        this.sensorLeft = new InfraRed(SENSOR_PIN_LEFT);
        this.sensorRight = new InfraRed(SENSOR_PIN_RIGHT);
        this.sensorMiddle = new InfraRed(SENSOR_PIN_MIDDLE);

        this.lineFollower = new LineFollower(this.movementController, this.sensorLeft, this.sensorRight, this.sensorMiddle);

        this.emergencyButton = new Button(EMERGENCY_BUTTON_PIN, this);

        this.stateController = new StateController(this.lineFollower, this.movementController,
                this.bluetooth, this.ultraSonic);

        this.buzzer = new Buzzer(BUZZER_PIN);

        this.goatScering = new GoatScering(this.movementController, this.buzzer);

        this.ultraSonic = new UltraSonic(ULTRASONIC_ECHO_PIN, ULTRASONIC_TRIGGER_PIN, this);


        this.devices = new ArrayList<>();
        this.devices.add(this.gripperMotor);

        this.devices.add(this.motorLeft);
        this.devices.add(this.motorRight);

        this.devices.add(this.neoPixel);

//        this.devices.add(this.whiskerLeft);
//        this.devices.add(this.whiskerRight);

        // de LineFollower class MOET ALTIJD NA de sensoren in de devices lijst
//        this.devices.add(this.sensorLeft);
//        this.devices.add(this.sensorRight);
//        this.devices.add(this.sensorMiddle);
//        this.devices.add(this.lineFollower);

        this.devices.add(this.emergencyButton);

        this.devices.add(this.buzzer);

        this.devices.add(this.ultraSonic);
    }

    private void run() {
        this.movementController.forward();

        while (true) {

//            this.bluetooth.remote();

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
        if (source == this.emergencyButton) {
            this.movementController.emergencyStop();
        }
    }

    boolean isScaringGoats = false;

    @Override
    public void onUltraSonic() {
        if (!isScaringGoats){

//            movementController.stop();
            movementController.emergencyStop();
//            this.goatScering.push();

//            this.devices.add(new Delay("goatscarer forward", this.devices, 1000, () -> {
//                this.movementController.forward();
//            }));
//
//            this.devices.add(new Delay("goatscarerer backwards", this.devices, 1500, () -> {
//                this.movementController.backwards();
//            }));
//


            this.addDelay("goatscare forwards", 1000, () -> {
                this.movementController.forward();
            });

            this.addDelay("goatscare backwards", 1500, () -> {
                this.movementController.backwards();
            });

            this.addDelay("goatscare stop", 2000, () -> {
                this.movementController.stop();
                isScaringGoats = false;

            });

            System.out.println("ultrsasoon");
        }
    }

    public void addDelay(String name, int time, TimerCallback callback) {
        this.devices.add(new Delay(name, this.devices, time, callback));
    }
}
