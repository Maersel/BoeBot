package hardware.motor;

public class MovementMotor extends Motor {
    private boolean isReversed;

    public MovementMotor(int pin, boolean isReversed) {
        super(pin, 1300, 1700, 1500);
        this.isReversed = isReversed;
    }

    @Override
    public void goToSpeed(int goalSpeed) {
        if (isReversed) {
            this.goalSpeed = super.defaultSpeed - goalSpeed;

        } else {
            this.goalSpeed = super.defaultSpeed + goalSpeed;
        }
//        System.out.println(goalSpeed);
        update();
    }

    public void increaseSpeed() {
        if (this.isReversed)
            super.goalSpeed -= super.stepSize;
        else
            super.goalSpeed += super.stepSize;
    }

    public void decreaseSpeed() {
        if(this.isReversed)
            super.goalSpeed -= super.stepSize;
        else
            super.goalSpeed += super.stepSize;
    }

    public void changeSpeed(int amount) {
        if (this.isReversed)
            super.goalSpeed -= amount;
        else
            super.goalSpeed += amount;
    }
}
