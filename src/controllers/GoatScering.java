package controllers;


import TI.BoeBot;
import TI.Timer;
import hardware.motor.MovementMotor;

public class GoatScering {
    private MovementController movementController;
    private Timer timer;
    private Timer timer1;
    private Timer timer2;

    public GoatScering(MovementController movementController) {
        this.movementController = movementController;
         this.timer = new Timer(6000);
         this.timer1 = new Timer(2000);
         this.timer2 = new Timer(4000);
    }

    public void push() {

        boolean moving = true;
        boolean stopping = false;
        timer.mark();
        timer1.mark();
        timer2.mark();
        System.out.println("UISDUIAS");

        movementController.forward();
        System.out.println("forward");
        while (moving) {

                if(timer1.timeout()){
                 movementController.stop();
                 movementController.backwards();
                }
                if(timer2.timeout()){
                    movementController.stop();
                }
                if (timer.timeout()){
                    moving = false;
                }

//            if (backwards && timer.timeout()) {
//                System.out.println("balls");
////                movementController.backwards();
//                backwards = false;
//                stopping = true;
//            }
//            if (stopping && timer1.timeout()) {
//                System.out.println("balls2");
////                movementController.stop();
//                System.out.println("we did it !");
//                return;
//            }
            movementController.update();
            BoeBot.wait(1);
        }


//        int alpha = 0;
//
//
//            while (true) {
//                if (timer.timeout()) {
//                    alpha++;
//                    if (alpha > 2) {
//                        alpha = 0;
//                    }
//                }
//                if (alpha == 0) {
//                    movementController.forward();
//                    System.out.println("forward");
//                }
//                if (alpha == 1) {
//                    movementController.backwards();
//                    System.out.println("backwards");
//                }
//                if (alpha == 2) {
//                    movementController.stop();
//                    System.out.println("4");
//                    break;
//                }


//        boolean forward = false;
//        boolean backwards = false;
//        boolean stopping = false;
//        for (int i = 0; i < 3; i++) {
//            if (timer.timeout()) {
//                forward = true;
//            }
//            if (!forward && timer.timeout()) {
//                movementController.forward();
//                System.out.println("forward");
//            }
//            if (timer.timeout()) {
//                backwards = true;
//            }
//            if (!backwards && timer.timeout()) {
//                movementController.backwards();
//                System.out.println("backwards");
//            }
//            if (timer.timeout()) {
//                stopping = true;
//            }
//            if (!stopping && timer.timeout()) {
//                movementController.stop();
//                System.out.println("stopping");
//            }


//            for (int j = 0; j < 3; j++) {
//                timer1.mark();
//                movementController.forward();
//                System.out.println("Foward");
//                if (timer1.timeout()) {
//                    timer2.mark();
//                    movementController.backwards();
//                    System.out.println("Backwards");
//                    if (timer2.timeout()) {
//                        movementController.stop();
//                        System.out.println("Stopp");
//                    }
//                }
//            }


    }

}

