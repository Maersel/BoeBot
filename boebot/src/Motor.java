import TI.Servo;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Motor {
    private Servo servo;
    private boolean inverse;
    private int speed;
//    private ArrayList<Pair<Integer, Integer>> turnRates = new ArrayList<>();
    private ArrayList<Integer> turnrate;
    private ArrayList<Integer> turnrateInverse;

    public Motor(int pin, boolean inverse) {
        this.servo = new Servo(pin);
        this.inverse = inverse;
        this.speed = 0;

        setTurnRates();
    }

    private void setTurnRates () {
//        this.turnRates.clear();
//
//        this.turnRates.add(new Pair<Integer, Integer>(1600, 1391));

        this.turnrate = new ArrayList<>();
        this.turnrateInverse = new ArrayList<>();

        this.turnrate.add(1391);
        this.turnrateInverse.add(1600);
    }

    public void setSpeed(int speed) {
        if (speed > 0 && speed < 5) {
            this.speed = speed;
        }
    }

    public void start() {
//        this.servo.update((inverse) ?
//                turnRates.get(speed).getValue() :
//                turnRates.get(speed).getKey());
        this.servo.update((inverse) ?
                turnrateInverse.get(speed) :
                turnrate.get(speed));
    }

    public void reverse() {
        this.servo.update((inverse) ?
                turnrate.get(speed) :
                turnrateInverse.get(speed));
    }

    public void stop() {
        this.servo.update(1500);
    }
}
