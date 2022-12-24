package hardware.motor;

public class GripperMotor extends Motor {

    public GripperMotor(int pin) {
        super(pin, 1325, 1850, 1500);
    }

    public int getCurrentSpeed() {
        return super.maxSpeed - super.currentSpeed;
    }

    @Override
    public void emergencyStop() {
        super.goalSpeed = super.currentSpeed;
    }
}
