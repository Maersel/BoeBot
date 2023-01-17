package controllers;

import hardware.led.NeoPixel;
import hardware.led.NeoPixelBlinking;
import hardware.motor.GripperMotor;

import hardware.motor.MovementMotor;

public class MovementController {
    private StateController stateController;
    private MovementMotor leftMotor;
    private MovementMotor rightMotor;



    private final int defaultSpeedRight = 29;
    private final int defaultSpeedLeft = 30;

    private AddDelay addDelay;

    private NeoPixelBlinking blinkingRight;
    private NeoPixelBlinking blinkingLeft;


    private boolean isTurning;
    private boolean turningDelay;



    public MovementController(MovementMotor leftMotor, MovementMotor rightMotor, AddDelay delay, NeoPixelBlinking blinkingRight, NeoPixelBlinking blinkingLeft) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.addDelay = delay;
        this.blinkingRight = blinkingRight;
        this.blinkingLeft = blinkingLeft;
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

    public void slowForward() {
        if (isTurning) return;
        this.leftMotor.goToSpeed(15);
        this.rightMotor.goToSpeed(15);
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
        this.leftMotor.goToSpeed(0);
    }

    public void correctToTheLeft() {
        if (isTurning) return;
        this.leftMotor.goToSpeed(60);
        this.rightMotor.goToSpeed(0);
    }

    public void boosy() {
        this.leftMotor.goToSpeed(200);
        this.rightMotor.goToSpeed(200);
    }

    public void turnRight() {
        if (!isTurning) {
            System.out.println("turning right");
            this.leftMotor.goToSpeed(-20);
            this.rightMotor.goToSpeed(50);

            this.addTurningDelay(600);

        }
    }
    public void turnLeft() {
        if (!isTurning) {
            System.out.println("turning left");
            this.leftMotor.goToSpeed(50);
            this.rightMotor.goToSpeed(-20);


            this.addTurningDelay(600);
        }
    }

    public RouteOptions turnAround() {
        RouteOptions direction = RouteOptions.NOTHING;
        if (!isTurning) {
            System.out.println("Turning around");

            if (Math.random() > 0.5) {
                this.leftMotor.goToSpeed(-30);
                this.rightMotor.goToSpeed(30);
                direction = RouteOptions.LEFT;
            } else {
                this.leftMotor.goToSpeed(30);
                this.rightMotor.goToSpeed(-30);
                direction = RouteOptions.RIGHT;
            }

            this.addTurningDelay(2000);
        }
        return direction;
    }

    public void remoteTurnLeft() {
        this.rightMotor.goToSpeed(-30);
        this.leftMotor.goToSpeed(30);
    }

    public void remoteTurnRight() {
        this.rightMotor.goToSpeed(30);
        this.leftMotor.goToSpeed(-30);
    }

    private void addTurningDelay(int time) {
        this.isTurning = true;
        this.turningDelay = true;
        this.addDelay.addDelay("Turning delay", time, () -> {
            this.turningDelay = false;
        });
    }
}
