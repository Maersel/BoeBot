package controllers;


import TI.BoeBot;
import TI.Timer;
import hardware.Updatable;
import hardware.buzzer.Buzzer;
import hardware.motor.MovementMotor;
import hardware.ultrasonic.Callback;
import hardware.ultrasonic.UltraSonic;

public class GoatScarring implements Updatable, Callback {
    private MovementController movementController;
    private Timer timer;
    private Timer timer1;
    private Timer timer2;
    private Buzzer buzzer;
    private boolean moving;
    private boolean isTurnedOn;
    private boolean isScaringGoats = false;
    private AddDelay addDelay;

    public GoatScarring(MovementController movementController, Buzzer buzzer, AddDelay addDelay) {
        this.movementController = movementController;
        this.timer = new Timer(6000);
        this.timer1 = new Timer(2000);
        this.timer2 = new Timer(4000);
        this.buzzer = buzzer;
        this.moving = false;
        this.addDelay = addDelay;
    }

    public boolean isTurnedOn() {
        return this.isTurnedOn;
    }

    public void turnOn() {
        this.isTurnedOn = true;
    }

    public void turnOff() {
        this.isTurnedOn = false;
    }

    private void markTimers() {
        this.timer.mark();
        this.timer1.mark();
        this.timer2.mark();
    }

    public void push() {
        this.moving = true;
        this.markTimers();

        movementController.forward();
        System.out.println("forward");
//        while (moving) {
//
//
////            if (backwards && timer.timeout()) {
////                System.out.println("balls");
//////                movementController.backwards();
////                backwards = false;
////                stopping = true;
////            }
////            if (stopping && timer1.timeout()) {
////                System.out.println("balls2");
//////                movementController.stop();
////                System.out.println("we did it !");
////                return;
////            }
//            movementController.update();
//            BoeBot.wait(1);
//        }
    }

    @Override
    public void update() {
//        if (this.isTurnedOn()) {
//            if (this.moving) {
//                this.buzzer.turnOn();
//
//                if (timer1.timeout()) {
//                    System.out.println("Stop");
//                    movementController.stop();
//                    movementController.backwards();
//                }
//                if (timer2.timeout()) {
//                    System.out.println("Stop 2e keer");
//                    movementController.stop();
//                }
//                if (timer.timeout()) {
//                    this.markTimers();
//                    this.buzzer.turnOff();
////                    this.moving = false;
//                    this.push();
//                }
//        }
    }

    @Override
    public void onUltraSonic() {
        if (!isScaringGoats) {
            isScaringGoats = true;
            movementController.stop();

            for (int i = 0; i < 3; i++) {
                int time = i * 6240;

                time += 1560;
                this.addDelay.addDelay("goat scare forwards", time, () -> {
                    this.movementController.forward();
                });

                time += 1560;
                this.addDelay.addDelay("goatscare stop", time, () -> {
                    this.movementController.stop();
                });

                time += 1560;
                this.addDelay.addDelay("goat scare backwards", time, () -> {
                    this.movementController.backwards();
                });

                time += 1560;
                this.addDelay.addDelay("goat scare stop", time, () -> {
                    this.movementController.stop();
                });
            }
            this.addDelay.addDelay("done scarring", 18720, () -> {
                isScaringGoats = false;
            });
        }
    }
}

