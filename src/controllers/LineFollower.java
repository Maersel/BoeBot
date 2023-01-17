
package controllers;

import controllers.pathfinding.Pathfinder;
import hardware.Updatable;
import hardware.gripper.Gripper;
import hardware.linesensor.Callback;
import hardware.linesensor.InfraRed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LineFollower implements Callback, Updatable, LineFollowerCallback {
    private MovementController movementController;
    private AddDelay addDelay;
    private Pathfinder pathfinder;
    private InfraRed leftSensor;
    private InfraRed middleSensor;
    private InfraRed rightSensor;
    private Gripper gripper;
    private PickUpDropController pickUpDropController;

    private int lineDetection;
    private boolean isOnCrossover;
    private RouteOptions[] route;
    private int step;

    private boolean isTurningAround;
    private boolean isFinished;
    private boolean isOnLastAction;
    private boolean isOnSecondToLastAction;
    private boolean hasEndGoal;
    private boolean hasPassedLastCrossover;

    private boolean premove;
    private boolean isPremoving;

    private boolean isReturning;

    private int noLines;


    public LineFollower(MovementController movementController, AddDelay addDelay, Pathfinder pathfinder,
                        InfraRed leftSensor, InfraRed rightSensor, InfraRed middleSensor, Gripper gripper, PickUpDropController pickUpDropController) {
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
//        System.out.println("Current step:\t" + this.step + "\tmax: " + this.route.leng
        System.out.println("Current step: \t" + this.step + "\t" + this.route[this.step]);

        if (this.step == this.route.length - 2) this.hasPassedLastCrossover = true;
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

    public void setPickUpDropController(PickUpDropController pickUpDropController) {
        this.pickUpDropController = pickUpDropController;
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
//        System.out.println(Integer.toBinaryString(this.lineDetection));
        if (isFinished) return;

        if (this.route[0] == RouteOptions.PICK_UP && !this.premove) {
            this.premove = true;
            this.isPremoving = true;

            this.pickUpDropController.turnOn(this.route[0]);
            this.pickUpDropController.forcehasTurnedAround();

            this.nextStep();
        }

        if (this.isPremoving) {
            this.lineDetection = 0;
            return;
        }

        if (this.isOnLastAction) {
            this.pickUpDropController.turnOn((this.route[this.route.length - 1] == RouteOptions.PICK_UP) ? RouteOptions.PICK_UP : RouteOptions.DROP);

            // mogelijk een probleem later
            if (this.lineDetection == 0b010 || this.lineDetection == 0b111) this.movementController.turnOffTurning();

            this.lineDetection = 0;
            return;
        }

        if (this.lineDetection != 0b000) this.noLines = 0;

        switch (this.lineDetection) {
            case 0b000:
                if (hasPassedLastCrossover && !this.movementController.isTurning()) {
                    this.noLines++;

                    if (this.noLines > 30)
                        this.isOnLastAction = true;
                }
                break;
            case 0b001:
                this.movementController.correctToTheRight();
                break;
            case 0b010:
                if (this.movementController.isTurning())this.movementController.turnOffTurning();
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
                if (isOnCrossover) break;
                System.out.println("Kruispunt");

                this.isOnCrossover = true;
                this.addDelay.addDelay("Crossover delay", 400, () -> {
                    this.isOnCrossover = false;
                });

                this.executeRouteCommand(this.route[this.step]);
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
                this.addDelay.addDelay("extra turning delay", 500, () -> {
                    this.movementController.turnLeft();
                });

                this.nextStep();
                break;
            case RIGHT:
                this.addDelay.addDelay("extra turning delay", 500, () -> {
                    this.movementController.turnRight();
                });

                this.nextStep();
                break;
            case TURN_AROUND:
                this.movementController.turnAround();

                this.nextStep();
                break;
            case PICK_UP:
                System.out.println("PICK UP AAAAAAAAAAAA");
                break;
            case DROP:
//                this.gripper.open();
                break;
            default:
                System.out.println("Route error");
                break;
        }
    }

    @Override
    public void returnToStart() {
        if (this.isReturning) {
            System.out.println("KLAAARRRRRRR");
            this.isFinished = true;
            return;
        }

        if (this.isPremoving) {
            this.isPremoving = false;
            return;
        }
        System.out.println("RETURNING???");

        this.isReturning = true;
        this.route = this.reverseRoute(this.route);
        this.step = 0;
        this.resetBooleans();

        System.out.println("Reversing route");
        System.out.print("New route:");
        for (RouteOptions routeOptions : route) {
            System.out.print("\t" + routeOptions);
        }
        System.out.print("\n");
    }

    private void resetBooleans() {
        this.isOnLastAction = false;
        this.hasPassedLastCrossover = false;
    }
}