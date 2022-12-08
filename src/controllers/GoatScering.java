package controllers;


import TI.BoeBot;
import TI.Timer;
import hardware.motor.MovementMotor;

public class GoatScering {
    private int distance;
    private MovementController movementController;
    Timer timer = new Timer(2000);

    public GoatScering(MovementController movementController) {
        this.distance = 14;
        this.movementController = movementController;
    }

    public void push() {
        timer.mark();
        System.out.println("UISDUIAS");
        int alpha = 0;


        for (int i = 0; i < 3; i++) {

            while (true) {
                if (timer.timeout()) {
                    alpha++;
                    if (alpha > 2) {
                        alpha = 0;
                    }
                }
                if (alpha == 0) {
                    movementController.forward();
                    System.out.println("forward");
                }
                if (alpha == 1) {
                    movementController.backwards();
                    System.out.println("backwards");
                }
                if (alpha == 2) {
                    movementController.stop();
                    System.out.println("4");
                    break;
                }
            }
        }
    }
}
