import TI.*;

public class Gripper {

    private final Servo servo;

    public Gripper(int inputPin) {
        this.servo = new Servo(inputPin);
    }

    public void open(){
        for (int i = 1325; i <= 1850; i += 3) {
            servo.update(i);
            BoeBot.wait(5);
//            System.out.println(i);
//            System.out.println("op en");
        }
    }

    public void close(){
        for (int i = 1850; i >= 1325; i -= 3) {
            servo.update(i);
            BoeBot.wait(5);
//            System.out.println(i);
//            System.out.println("sluit");
        }
    }

}
