package hardware.motor;

import TI.Servo;
import hardware.Updatable;

public class Motor implements Updatable {
    private Servo servo;
    private int minSpeed;
    private int maxSpeed;
    private int currentSpeed;
    protected int goalSpeed;
    protected int defaultSpeed;
    protected int stepSize = 1;

    public Motor(int pin, int minSpeed, int maxSpeed, int defaultSpeed) {
        this.servo = new Servo(pin);
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.defaultSpeed = defaultSpeed;
        this.currentSpeed = defaultSpeed;

        emergencyStop();
    }

    public void goToSpeed(int goalSpeed) {
        this.goalSpeed = defaultSpeed + goalSpeed;
    }

    public void stop() {
        this.goalSpeed = 0;
    }

    public void emergencyStop() {
        this.goalSpeed = this.defaultSpeed;
        this.servo.update(this.defaultSpeed);
    }

    @Override
    public void update() {
        if (this.goalSpeed > this.currentSpeed && this.currentSpeed < this.maxSpeed) {
            this.currentSpeed += this.stepSize;
        } else if (this.goalSpeed < this.currentSpeed && this.currentSpeed > this.minSpeed) {
            this.currentSpeed -= this.stepSize;
        }

        this.servo.update(this.currentSpeed);
    }
}
