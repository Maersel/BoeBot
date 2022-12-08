package controllers;

import hardware.motor.MovementMotor;

public class MovementController {
    private MovementMotor leftMotor;
    private MovementMotor rightMotor;

    public MovementController(MovementMotor leftMotor, MovementMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    public void forward() {
        this.leftMotor.goToSpeed(40);
        this.rightMotor.goToSpeed(40);
    }

    public void backwards() {
        this.leftMotor.goToSpeed(-40);
        this.rightMotor.goToSpeed(-40);
    }

    public void stop() {
        this.leftMotor.goToSpeed(0);
        this.rightMotor.goToSpeed(0);
    }

    public void emergencyStop() {
        this.leftMotor.emergencyStop();
        this.rightMotor.emergencyStop();
    }

    public void correctLeft() {
//        this.leftMotor.changeSpeed(-1);
        this.rightMotor.changeSpeed(1);

//        this.rightMotor.goToSpeed(80);
    }

    public void correctRight() {
        this.leftMotor.changeSpeed(1);
//        this.rightMotor.changeSpeed(-1);

//        this.leftMotor.goToSpeed(80);
    }

    public void update() {
        rightMotor.MMupdate();
        leftMotor.MMupdate();
    }
}
