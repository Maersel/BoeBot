package controllers;

import hardware.Updatable;
import hardware.bluetooth.Bluetooth;
import hardware.bluetooth.Callback;
import hardware.button.Button;
import hardware.ultrasonic.UltraSonic;

public class StateController implements Updatable, Callback {
    private int state;
    private int previousState;
    private LineFollower lineFollower;
    private MovementController movementController;
    private Bluetooth bluetooth;
    private UltraSonic ultraSonic;
    private GoatScare goatScare;

    private Button emergencyButton;

    public StateController(LineFollower lineFollower, MovementController movementController,
                           Bluetooth bluetooth, UltraSonic ultraSonic) {
        this.lineFollower = lineFollower;
        this.movementController = movementController;
        this.bluetooth = bluetooth;
        this.ultraSonic = ultraSonic;

        this.state = Configuration.REST_STATE;
    }

    public int currentState() {
        return this.state;
    }

    @Override
    public void update() {
        if (emergencyButton.buttonPressed()) {
            this.state = Configuration.EMERGENCY_STATE;
            this.movementController.emergencyStop();
        } else if (ultraSonic.closeObject()) {
            this.state = Configuration.GOAT_SCARING_STATE;
            this.goatScare.turnOn();

        }
//        else if (ultraSonic.isTooClose()) {
//            this.state = Configuration.GOAT_SCARING_STATE;
//            this.goatScare.turnOn();
//        }

        if (this.state == Configuration.GOAT_SCARING_STATE) {
            if (goatScare.checkIfDone()) this.state = this.previousState;
        }
    }

    @Override // wordt al gefilterd voordat het wordt opgeroepen
    public void onBlueToothInput(int input) {
        this.state = input;

    }
}
