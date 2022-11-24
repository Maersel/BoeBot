import TI.*;

public class Gripper {

    private final Servo servo;

    public Gripper(int inputPin) {
        this.servo = new Servo(inputPin);
    }

    private void open(){
        for (int i = 1325; i <= 1850; i += 3) {
            servo.update(i);
            BoeBot.wait(5);
            System.out.println(i);
        }
    }

    private void close(){
        for (int i = 1850; i >= 1325; i -= 3) {
            servo.update(i);
            BoeBot.wait(5);
            System.out.println(i);
        }
    }

}
