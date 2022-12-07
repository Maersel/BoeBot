package hardware.gripper;

import hardware.motor.GripperMotor;
import hardware.motor.Motor;

public class Gripper {
    private GripperMotor motor;

    public Gripper(GripperMotor motor) {
        this.motor = motor;

        // default
        this.open();
    }

    public void open() {
        this.motor.goToSpeed(350);
    }

    public void close() {
        this.motor.goToSpeed(-250);
    }
}
