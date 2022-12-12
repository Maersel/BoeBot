package controllers;

import hardware.motor.MovementMotor;

public class MovementController {
    private MovementMotor leftMotor;
    private MovementMotor rightMotor;

    private boolean isTurning;

    public MovementController(MovementMotor leftMotor, MovementMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    public void forward() {
        this.leftMotor.goToSpeed(30);
        this.rightMotor.goToSpeed(30);
    }

    public void backwards() {
        this.leftMotor.goToSpeed(-30);
        this.rightMotor.goToSpeed(-30);
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
//        this.rightMotor.changeSpeed(1);

        this.rightMotor.goToSpeed(60);
        this.leftMotor.goToSpeed(10);
    }

    public void correctRight() {
//        this.leftMotor.changeSpeed(1);
//        this.rightMotor.changeSpeed(-1);

        this.leftMotor.goToSpeed(60);
        this.rightMotor.goToSpeed(10);
    }

    public void boosy() {
        this.leftMotor.goToSpeed(200);
        this.rightMotor.goToSpeed(200);
    }

    public void turnRight() {
        this.leftMotor.goToSpeed(-30);
        this.rightMotor.goToSpeed(30);
    }
    public void turnLeft() {
        this.leftMotor.goToSpeed(30);
        this.rightMotor.goToSpeed(-30);
    }

    public void update() {
        rightMotor.MMupdate();
        leftMotor.MMupdate();
    }
}
