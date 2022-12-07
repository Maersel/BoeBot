package hardware.motor;

public class GripperMotor extends Motor {

    public GripperMotor(int pin) {
        super(pin, 1325, 1850, 1500);
    }
}
