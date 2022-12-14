package controllers;

import hardware.led.NeoPixel;
import hardware.motor.GripperMotor;

import hardware.motor.MovementMotor;

public class MovementController {
    private MovementMotor leftMotor;
    private MovementMotor rightMotor;



    private final int defaultSpeedRight = 29;
    private final int defaultSpeedLeft = 30;

    private AddDelay addDelay;


    private boolean isTurning;
    private boolean turningDelay;



    public MovementController(MovementMotor leftMotor, MovementMotor rightMotor, AddDelay delay) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.addDelay = delay;
    }

    public boolean isTurning() {
        return this.isTurning;
    }

    public void turnOffTurning() {
        if (!this.turningDelay)
            this.isTurning = false;
    }

    public void forward() {
        if (isTurning) return;
        this.leftMotor.goToSpeed(defaultSpeedLeft);
        this.rightMotor.goToSpeed(defaultSpeedRight);
    }

    public void backwards() {
        this.leftMotor.goToSpeed(-defaultSpeedRight);
        this.rightMotor.goToSpeed(-defaultSpeedLeft);
    }

    public void stop() {
        this.leftMotor.goToSpeed(0);
        this.rightMotor.goToSpeed(0);
    }

    public void emergencyStop() {
        this.leftMotor.emergencyStop();
        this.rightMotor.emergencyStop();
    }

    public void correctToTheRight() {
        if (isTurning) return;

        this.rightMotor.goToSpeed(60);
        this.leftMotor.goToSpeed(10);
    }

    public void correctToTheLeft() {
        if (isTurning) return;
        this.leftMotor.goToSpeed(60);
        this.rightMotor.goToSpeed(10);
    }

    public void boosy() {
        this.leftMotor.goToSpeed(200);
        this.rightMotor.goToSpeed(200);
    }

    public void turnRight() {

        if (!isTurning) {
            System.out.println("turning right");
            this.leftMotor.goToSpeed(0);
            this.rightMotor.goToSpeed(75);

            this.isTurning = true;
            this.turningDelay = true;
            this.addTurningDelay(400);
        }
    }
    public void turnLeft() {
        if (!isTurning) {
            System.out.println("turning left");
            this.leftMotor.goToSpeed(75);
            this.rightMotor.goToSpeed(0);

            this.isTurning = true;
            this.turningDelay = true;
            this.addTurningDelay(400);
        }
    }

    public void turnAround() {
        if (!isTurning) {
            System.out.println("Turning around");

            if (Math.random() > 0.5) {
                this.leftMotor.goToSpeed(-30);
                this.rightMotor.goToSpeed(30);
            } else {
                this.leftMotor.goToSpeed(30);
                this.rightMotor.goToSpeed(-30);
            }

            this.isTurning = true;
            this.turningDelay = true;
            this.addTurningDelay(1500);
        }
    }

    private void addTurningDelay(int time) {
        this.addDelay.addDelay("Turning delay", time, () -> {
            this.turningDelay = false;
        });
    }
}
