import TI.BoeBot;

public class Ultrasonic {

    private int echoOutput;
    private int triggerInput;

    public Ultrasonic(int echoOutput, int triggerInput) {
        this.echoOutput = echoOutput;
        this.triggerInput = triggerInput;
    }

    public boolean detectObject() {
        return BoeBot.digitalRead(triggerInput);
    }
}
