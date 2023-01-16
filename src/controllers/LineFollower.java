
package controllers;

import TI.BoeBot;
import controllers.pathfinding.Pathfinder;
import hardware.Updatable;
import hardware.gripper.Gripper;
import hardware.linesensor.Callback;
import hardware.linesensor.InfraRed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LineFollower implements Callback, hardware.ultrasonic.Callback, Updatable {
    private MovementController movementController;
    private AddDelay addDelay;
    private Pathfinder pathfinder;
    private InfraRed leftSensor;
    private InfraRed middleSensor;
    private InfraRed rightSensor;
    private Gripper gripper;

    private int lineDetection;
    private boolean isOnCrossover;
    private RouteOptions[] route;
    private int step;

    private boolean isTurningAround;
    private boolean isFinished;
    private boolean isOnLastAction;
    private boolean isOnSecondToLastAction;
    private boolean hasEndGoal;

    private int noLines;


    public LineFollower(MovementController movementController, AddDelay addDelay, Pathfinder pathfinder,
                        InfraRed leftSensor, InfraRed rightSensor, InfraRed middleSensor, Gripper gripper) {
        this.movementController = movementController;
        this.addDelay = addDelay;
        this.pathfinder = pathfinder;
        this.leftSensor = leftSensor;
        this.middleSensor = middleSensor;
        this.rightSensor = rightSensor;
        this.lineDetection = 0;
        this.gripper = gripper;
        this.hasEndGoal = false;

        this.setCallbacks();
    }

    private void nextStep() {
        this.step++;
    }

    public void setRoute(int startingPoint, int endPoint, RouteOptions pickUpOrDrop) {
        ArrayList<Integer> path = pathfinder.nodePath(startingPoint, endPoint);
        this.route = pathfinder.pathDirections(path);

        if (pickUpOrDrop == RouteOptions.PICK_UP) {
            this.route = pathfinder.routePickUp(this.route);
        } else if (pickUpOrDrop == RouteOptions.DROP) {
            this.route = pathfinder.routeDrop(this.route);
        }

        this.step = 0;
    }

    public void printRoute() {
        for (RouteOptions routeOptions : this.route) {
            System.out.println(routeOptions);
        }

//        System.out.println("\nNieuw\n");
//
//        for (RouteOptions routeOptions : this.reverseRoute(this.route)) {
//            System.out.println(routeOptions);
//        }
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
                break;
            case 0b001:
                this.movementController.correctToTheRight();
                break;
            case 0b010:
                this.movementController.forward();
                break;
            case 0b011:
                break;
            case 0b100:
                this.movementController.correctToTheLeft();
                break;
            case 0b101:
                break;
            case 0b110:
                break;
            case 0b111: // Kruispunt
                System.out.println("Kruispunt");
                break;
            default:
                System.out.println("ERROR: \t" + Integer.toBinaryString(this.lineDetection));
                break;
        }

        this.lineDetection = 0;
    }

    private RouteOptions[] reverseRoute(RouteOptions[] route) {
        RouteOptions[] newRoute = new RouteOptions[route.length];

        for (int i = 0; i < route.length; i++) {
            switch (route[i]) {
                case STRAIGHT:
                    newRoute[i] = route[i];
                    break;
                case LEFT:
                    newRoute[i] = RouteOptions.RIGHT;
                    break;
                case RIGHT:
                    newRoute[i] = RouteOptions.LEFT;
                    break;
                case PICK_UP:
                    newRoute[i] = RouteOptions.DROP;
                    break;
                case DROP:
                    newRoute[i] = RouteOptions.NOTHING;
                    break;
            }
        }

        RouteOptions[] tempRoute;

        if (newRoute[newRoute.length - 1] == RouteOptions.NOTHING) {
            tempRoute = new RouteOptions[route.length - 2];
            for (int i = 1; i < newRoute.length - 1; i++) {
                tempRoute[i - 1] = newRoute[i];
            }

            newRoute = Arrays.copyOf(newRoute, newRoute.length - 1);
        } else {
            tempRoute = new RouteOptions[route.length];
            tempRoute[0] = newRoute[newRoute.length - 1];

            for (int i = 0; i < newRoute.length - 1; i++) {
                tempRoute[i + 1] = newRoute[i];
            }
            newRoute = tempRoute;
        }

        newRoute = tempRoute;

        Collections.reverse(Arrays.asList(newRoute));

        return newRoute;
    }

    private void executeRouteCommand(RouteOptions command) {
        switch (command) {
            case NOTHING:
                break;
            case STRAIGHT:
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
            case TURN_AROUND:
                this.movementController.turnAround();

                this.nextStep();
                break;
            case PICK_UP:
                this.movementController.backwards();

                this.addDelay.addDelay("BACKWARDS", 100, () -> {
                    this.movementController.turnAround();
                });
//                this.gripper.close();
//                this.isTurningAround = true;

//                System.out.println("WOOOOOOOOOOOOO");
                break;
            case DROP:
                this.gripper.open();
                break;
            default:
                System.out.println("Route error");
                break;
        }
    }

    @Override
    public void onUltraSonic(int distance) {
    }
}