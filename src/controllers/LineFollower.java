
package controllers;

import controllers.pathfinding.Pathfinder;
import hardware.Updatable;
import hardware.led.NeoPixel;
import hardware.linesensor.Callback;
import hardware.linesensor.InfraRed;

import java.util.ArrayList;

public class LineFollower implements Callback, Updatable {
    private MovementController movementController;
    private AddDelay addDelay;
    private Pathfinder pathfinder;
    private InfraRed leftSensor;
    private InfraRed middleSensor;
    private InfraRed rightSensor;

    //
    private int lineDetection;
    private boolean isOnCrossover;
    private RouteOptions[] route;
    private int step;


    public LineFollower(MovementController movementController, AddDelay addDelay, Pathfinder pathfinder,
                        InfraRed leftSensor, InfraRed rightSensor, InfraRed middleSensor) {
        this.movementController = movementController;
        this.addDelay = addDelay;
        this.pathfinder = pathfinder;
        this.leftSensor = leftSensor;
        this.middleSensor = middleSensor;
        this.rightSensor = rightSensor;
        this.lineDetection = 0;

        this.setCallbacks();
    }

    private void nextStep() {
        if (this.step == this.route.length) {
            System.out.println("laatste stap");
        }

        this.step = (this.step + 1) % this.route.length;
    }

    public void setRoute(int startingPoint, int endPoint) {
        ArrayList<Integer> path = pathfinder.nodePath(startingPoint, endPoint);

        this.route = pathfinder.pathDirections(path);
        this.step = 0;
    }

    private void setCallbacks() {
        this.leftSensor.setCallback(this);
        this.middleSensor.setCallback(this);
        this.rightSensor.setCallback(this);
    }

    @Override
    public void onLineDetection(InfraRed source) {
        if (source == this.leftSensor) {
            this.lineDetection |= 1 << 2;
        }
        if (source == this.middleSensor) {
            this.lineDetection |= 1 << 1;
        }
        if (source == this.rightSensor) {
            this.lineDetection |= 1;
        }
    }

    @Override
    public void update() {
        switch (this.lineDetection) {
            case 0b000:
//                System.out.println("geen detectie");
//                this.movementController.stop();
//                this.movementController.forward();
                break;
            case 0b001:
//                System.out.println("Lijn alleen rechts");
                this.movementController.correctToTheRight();
                break;
            case 0b010:
//                System.out.println("Lijn alleen middel");
                this.movementController.turnOffTurning();
                this.movementController.forward();
                break;
            case 0b011:
//                System.out.println("Lijn middel en rechts");
//                this.movementController.correctToTheRight();
                break;
            case 0b100:
//                System.out.println("Lijn alleen links");
                this.movementController.correctToTheLeft();
                break;
            case 0b101:
//                System.out.println("Lijn links en rechts");
                break;
            case 0b110:
//                Syste     m.out.println("Lijn middel en links");
//                this.movementController.correctToTheLeft();
                break;
            case 0b111:
//                this.movementController.turnRight();
//                if (Math.random() >  0.000000000000001) break;

                if (this.isOnCrossover) break;
                System.out.println("KRUISPUNT!\tstap: " + this.step + "\t" + this.route[step]);

                this.isOnCrossover = true;
                this.addDelay.addDelay("Crossing crossover", 400, () -> {
                    this.isOnCrossover = false;
                });

                switch (this.route[this.step]) {
                    case STRAIGHT:

                        this.nextStep();
                        break;
                    case TURN_AROUND:
                        this.movementController.turnAround();

                        this.nextStep();
                        break;
                    case LEFT:
                        this.movementController.turnLeft();

                        this.nextStep();
                        break;
                    case RIGHT:
                        this.movementController.turnRight();

                        this.nextStep();
                        break;
                    default:
                        System.out.println("Route error");
                        break;
                }
                break;
            default:
                System.out.println("de lul lmao");
                System.out.println(Integer.toBinaryString(lineDetection));
                break;
        }

        this.lineDetection = 0;
    }

    private RouteOptions[] reverseRoute(RouteOptions[] route) {
        RouteOptions[] newRoute = new RouteOptions[route.length];

        for (int i = 0; i < route.length; i++) {
            switch (route[i]) {
                case LEFT:
                    newRoute[route.length - i - 1] = RouteOptions.RIGHT;
                    break;
                case RIGHT:
                    newRoute[route.length - i - 1] = RouteOptions.LEFT;
                    break;
                default:
                    newRoute[route.length - i - 1] = route[i];
            }
        }

        return newRoute;
    }
}