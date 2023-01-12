package controllers;

import TI.BoeBot;
import TI.PinMode;
import TI.SerialConnection;
import TI.Timer;
import controllers.pathfinding.Pathfinder;
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

import java.util.ArrayList;

public class StateController implements Updatable, AddDelay, hardware.button.Callback {
    private int state = 0;
    private int previousState;

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


    //     ---------
    private ArrayList<Updatable> devices;

    private ArrayList<Updatable> allwaysOnDevices;
    private ArrayList<Updatable> restDevices;
    private ArrayList<Updatable> bluetoothDevices;
    private ArrayList<Updatable> lineFollowingDevices;
    private ArrayList<Updatable> remoteDevices;
    private ArrayList<Updatable> goatKillingDevices;
    private ArrayList<Updatable> emergencyDevices;
    //     ---------

    private Pathfinder pathfinder;

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

    private GoatScarring goatScering;

    private UltraSonic ultraSonicFront;
    private UltraSonic ultraSonicRear;
    private RemoteController remoteController;

    public ArrayList<Updatable> getDevices() {
        return devices;
    }

    public void init() {
        this.pathfinder = new Pathfinder();

        BoeBot.setMode(GRIPPER_PIN, PinMode.Output); // FIX
        BoeBot.setMode(EMERGENCY_BUTTON_PIN, PinMode.Input); // FIX

        this.gripperMotor = new GripperMotor(GRIPPER_PIN);
        this.gripper = new Gripper(gripperMotor);

        this.motorLeft = new hardware.motor.MovementMotor(MOTOR_PIN_LEFT, true);
        this.motorRight = new hardware.motor.MovementMotor(MOTOR_PIN_RIGHT, false);
        this.movementController = new controllers.MovementController(motorLeft, motorRight, this);

        this.bluetooth = new Bluetooth(new SerialConnection(), this.movementController, this, this.lineFollower, this.gripper, new NeoPixel());

        this.sensorLeft = new InfraRed(SENSOR_PIN_LEFT);
        this.sensorRight = new InfraRed(SENSOR_PIN_RIGHT);
        this.sensorMiddle = new InfraRed(SENSOR_PIN_MIDDLE);

        this.lineFollower = new LineFollower(this.movementController, this, this.pathfinder, this.sensorLeft, this.sensorRight, this.sensorMiddle, this.gripper);

        this.emergencyButton = new Button(EMERGENCY_BUTTON_PIN, this);


//        this.buzzer = new Buzzer(BUZZER_PIN);

        this.goatScering = new GoatScarring(this.movementController, this.buzzer, this);

        this.ultraSonicFront = new UltraSonic(ULTRASONIC_ECHO_PIN_FRONT, ULTRASONIC_TRIGGER_PIN_FRONT, this.goatScering);
//        this.ultraSonicRear = new UltraSonic(ULTRASONIC_ECHO_PIN_REAR, ULTRASONIC_TRIGGER_PIN_REAR, this);

        /**
         this.devices = new ArrayList<>();
         //        this.devices.add(this.gripperMotor);

         this.devices.add(this.motorLeft);
         this.devices.add(this.motorRight);

         this.devices.add(this.neoPixel);


         // de LineFollower class MOET ALTIJD NA de sensoren in de devices lijst
         this.devices.add(this.sensorLeft);
         this.devices.add(this.sensorRight);
         this.devices.add(this.sensorMiddle);
         this.devices.add(this.lineFollower);

         //        this.devices.add(this.emergencyButton);

         //        this.devices.add(this.buzzer);

         //        this.devices.add(this.ultraSonicRear);
         this.devices.add(this.ultraSonicFront);

         this.devices.add(this.bluetooth);

         this.lineFollower.setRoute(8, 15);
         this.lineFollower.addRouteCommand(RouteOptions.PICK_UP);
         */

        //     ---------

        this.allwaysOnDevices = new ArrayList<>();

        this.allwaysOnDevices.add(this.remoteController);
        this.allwaysOnDevices.add(this.emergencyButton);

        //     ---------

        this.restDevices = new ArrayList<>();

        this.restDevices.add(this.bluetooth);

        //     ---------

        this.bluetoothDevices = new ArrayList<>();

        this.bluetoothDevices.add(this.bluetooth);
        this.bluetoothDevices.add(this.motorLeft);
        this.bluetoothDevices.add(this.motorRight);
        this.bluetoothDevices.add(this.gripperMotor);

        //     ---------

        this.lineFollowingDevices = new ArrayList<>();

        this.lineFollowingDevices.add(this.sensorLeft);
        this.lineFollowingDevices.add(this.sensorMiddle);
        this.lineFollowingDevices.add(this.sensorRight);
        this.lineFollowingDevices.add(this.lineFollower);
        this.lineFollowingDevices.add(this.motorLeft);
        this.lineFollowingDevices.add(this.motorRight);
        this.lineFollowingDevices.add(this.ultraSonicFront);
        this.lineFollowingDevices.add(this.ultraSonicRear);
        this.lineFollowingDevices.add(this.buzzer);
        this.lineFollowingDevices.add(this.gripperMotor);

        //     ---------

        this.remoteDevices = new ArrayList<>();

        this.remoteDevices.add(this.motorLeft);
        this.remoteDevices.add(this.motorRight);
        this.remoteDevices.add(this.gripperMotor);

        //     ---------

        this.goatKillingDevices = new ArrayList<>();

        this.goatKillingDevices.add(this.motorLeft);
        this.goatKillingDevices.add(this.motorRight);
        this.goatKillingDevices.add(this.ultraSonicFront);


        //     ---------

        this.emergencyDevices = new ArrayList<>();

        this.emergencyDevices.add(motorLeft);
        this.emergencyDevices.add(motorRight);
        this.emergencyDevices.add(gripperMotor);

        //     ---------


    }

    public void run() {
        while (true) {

//            for (int i = devices.size() - 1; i >= 0; i--) {
//                devices.get(i).update();
//            }

            switch (state) {
                case Configuration.REST_STATE:
                    for (int i = restDevices.size() - 1; i >= 0; i--) {
                        restDevices.get(i).update();
                    }
                    break;
                case Configuration.REMOTE_STATE:
                    for (int i = remoteDevices.size() - 1; i >= 0; i--) {
                        remoteDevices.get(i).update();
                    }
                    break;
                case Configuration.BLUETOOTH_STATE:
                    for (int i = bluetoothDevices.size() - 1; i >= 0; i--) {
                        bluetoothDevices.get(i).update();
                    }
                    break;
                case Configuration.LINE_FOLLOWING_STATE:
                    for (int i = lineFollowingDevices.size() - 1; i >= 0; i--) {
                        lineFollowingDevices.get(i).update();
                    }
                    break;
                case Configuration.GOAT_SCARING_STATE:
                    for (int i = goatKillingDevices.size() - 1; i >= 0; i--) {
                        goatKillingDevices.get(i).update();
                    }
                    break;
                case Configuration.EMERGENCY_STATE:
                    this.movementController.emergencyStop();
                    for (int i = emergencyDevices.size() - 1; i >= 0; i--) {
                        emergencyDevices.get(i).update();
                    }
                    break;
            }

            for (int i = allwaysOnDevices.size() - 1; i >= 0; i--) {
                devices.get(i).update();
            }

            BoeBot.wait(1);
        }
    }

    public void changeState(int state) {
        this.previousState = this.state;
        this.state = state;
    }

    public int getCurrentState() {
        return this.state;
    }

    @Override
    public void update() {

    }

    @Override
    public void onButtonPress(Button source) {
        if (source == this.emergencyButton) {
            System.out.println("BUTTON PRESSED");
            changeState(Configuration.EMERGENCY_STATE);
        }
    }


    @Override
    public void addDelay(String name, int time, TimerCallback callback) {
        this.devices.add(new Delay(name, this.devices, time, callback));
    }
}
