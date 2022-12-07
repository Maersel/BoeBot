package controllers;

import hardware.Updatable;
import hardware.button.Button;
import hardware.ultrasonic.UltraSonic;
import sun.awt.ConstrainableGraphics;

public class StateController implements Updatable {
    private int state;
    private int previousState;
    private LineFollower lineFollower;
    private MovementController movementController;
    private RemoteController remoteController;
    private UltraSonic ultraSonic;
    private GoatScare goatScare;

    private Button emergencyButton;

    public StateController(LineFollower lineFollower, MovementController movementController,
                           RemoteController remoteController, UltraSonic ultraSonic) {
        this.lineFollower = lineFollower;
        this.movementController = movementController;
        this.remoteController = remoteController;
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
        } else if (ultraSonic.isTooClose()) {
            this.state = Configuration.GOAT_SCARING_STATE;
            this.goatScare.turnOn();
        } else if (remoteController.changeState() != -1){
            this.state = remoteController.changeState();
        }

        if (this.state == Configuration.GOAT_SCARING_STATE) {
            if (goatScare.checkIfDone()) this.state = this.previousState;
        }
    }
}
