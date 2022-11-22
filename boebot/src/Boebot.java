public class Boebot {

    private int leftServo;
    private int rightServo;
    private boolean boebotStatus = true;

    private MovementController boebot;
    private Ultrasonic ultrasonic;
    private Buzzer buzzer;
    private Gripper gripper;
    private Light light;
    public Boebot() {
        this.boebot = new MovementController(leftServo, rightServo);
        this.ultrasonic = new Ultrasonic(0, 1);
        this.buzzer = new Buzzer(2);
        this.gripper = new Gripper(3);
        this.light = new Light(4);
    }

    public void demo1() {
        while (boebotStatus) {
            boebot.forward();
            if (ultrasonic.detectObject()) {
                boebot.stop();
                buzzer.beepThreeTimes();
                if (ultrasonic.detectObject()) {
                    buzzer.beepThreeTimes();
                    if (ultrasonic.detectObject()) {
                        buzzer.beepThreeTimes();
                        boebot.forward();
                    }
                }
            }
        }

    }
}
