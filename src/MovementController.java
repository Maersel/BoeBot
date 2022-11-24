import TI.BoeBot;
import TI.Servo;
//import javafx.util.Pair;
import java.util.Arrays;

public class MovementController {
    private Motor leftMotor;
    private Motor rightMotor;
    private int speed;

    public MovementController(int pinLeft, int pinRight) {
        this.leftMotor = new Motor(pinLeft, true);
        this.rightMotor = new Motor(pinRight, false);
        this.speed = 1;
    }

    public void stop() {
        leftMotor.stop();
        rightMotor.stop();
    }

    public void forward() {
        leftMotor.start();
        rightMotor.start();
    }

    public void reverse() {
        leftMotor.reverse();
        rightMotor.reverse();
    }

    public void turnLeft() {
        leftMotor.reverse();
        rightMotor.start();
        BoeBot.wait(50);

        leftMotor.stop();
        rightMotor.stop();
    }

    public void turnRight() {
        leftMotor.start();
        rightMotor.reverse();
        BoeBot.wait(50);

        leftMotor.stop();
        rightMotor.stop();
    }

    public int getSpeed() {
        return this.speed;
    }

//    public void setSpeed(int speed) {
//        Integer arr[] = {1, 2, 3, 4};
//        if (Arrays.asList(arr).contains(speed)) {
//            this.speed = speed;
//        } else {
//            System.out.println("Invalid speed");
//        }
//    }
//
//    public Pair<Integer, Integer> getSpeeds() {
//        if (this.speed == 4) {
//            return new Pair<>(1700, 1300);
//        } else if (this.speed == 3) {
//            return new Pair<>(1650, 1350);
//        } else if (this.speed == 2) {
//            return new Pair<>(1600, 1400);
//        } else {
//            return new Pair<>(1550, 1450);
//        }
//    }
}
