import TI.BoeBot;

public class Buzzer {

    private int inputPin;

    public Buzzer(int inputPin) {
        this.inputPin = inputPin;
    }

    public void beep() {
        BoeBot.freqOut(15, 10000, 1000);
    }

    public void beepThreeTimes() {
        for (int i = 0; i < 3; i++) {
            BoeBot.freqOut(15, 10000, 1000);
            BoeBot.wait(5000);
        }
    }
}
