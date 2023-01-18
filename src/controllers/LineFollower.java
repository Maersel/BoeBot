
package controllers;

import controllers.pathfinding.Pathfinder;
import hardware.Updatable;
import hardware.gripper.Gripper;
import hardware.linesensor.Callback;
import hardware.linesensor.InfraRed;

import java.time.Instant;
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
    private PickUpDropController pickUpDropController;

    private int lineDetection;
    private boolean isOnCrossover;
    private ArrayList<RouteOptions[]> queue;
    private RouteOptions[] activeRoute;
    private int queueStep;
    private int routeStep;

    private boolean isFinished;
    private boolean isOnLastAction;
    private boolean hasPassedLastCrossover;

    private boolean premove;
    private boolean isPremoving;

    private boolean isReturning;

    private int noLines;
    private int instant;


    public LineFollower(MovementController movementController, AddDelay addDelay, Pathfinder pathfinder,
                        InfraRed leftSensor, InfraRed rightSensor, InfraRed middleSensor) {
        this.movementController = movementController;
        this.addDelay = addDelay;
        this.pathfinder = pathfinder;
        this.leftSensor = leftSensor;
        this.middleSensor = middleSensor;
        this.rightSensor = rightSensor;
        this.lineDetection = 0;

        this.queue = new ArrayList<>();

        this.queueStep = 0;
        this.routeStep = 0;

        this.setCallbacks();
    }

    private void nextStep() {
//        System.out.println("Current step: \t" + this.step + "\t" + this.activeRoute[this.step]);
        this.activeRoute = this.queue.get(this.queueStep);

        if (this.routeStep == this.activeRoute.length - 2) this.hasPassedLastCrossover = true;
        this.routeStep++;
    }

    public void addRoute(int startingPoint, int endPoint, RouteOptions pickUpOrDrop) {
        ArrayList<Integer> path = pathfinder.nodePath(startingPoint, endPoint);
        RouteOptions[] newRoute = pathfinder.pathDirections(path);

        if (pickUpOrDrop == RouteOptions.PICK_UP) {
            newRoute = pathfinder.routePickUp(newRoute);
        } else if (pickUpOrDrop == RouteOptions.DROP) {
            newRoute = pathfinder.routeDrop(newRoute);
        }

        this.queue.add(newRoute);

        this.activeRoute = this.queue.get(this.queueStep);
    }
    public void printRoute() {
        for (RouteOptions routeOptions : this.activeRoute) {
            System.out.println(routeOptions);
        }

//        System.out.println("\nNieuw\n");
//
//        for (RouteOptions routeOptions : this.reverseRoute(this.activeRoute)) {
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
//        System.out.println(Instant.now().getNano() - this.instant);
//        this.instant = Instant.now().getNano();
//        System.out.println(Integer.toBinaryString(this.lineDetection));
        if (isFinished) return;

        if (this.activeRoute[0] == RouteOptions.PICK_UP && !this.premove) {
            this.premove = true;
            this.isPremoving = true;

            this.pickUpDropController.turnOn(this.activeRoute[0]);
            this.pickUpDropController.forcehasTurnedAround();

            this.nextStep();
        }

        if (this.isPremoving) {
            this.lineDetection = 0;
            return;
        }

        if (this.isOnLastAction) {
            this.pickUpDropController.turnOn((this.activeRoute[this.activeRoute.length - 1] == RouteOptions.PICK_UP) ? RouteOptions.PICK_UP : RouteOptions.DROP);

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

                this.executeRouteCommand(this.activeRoute[this.routeStep]);
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
                break;
            case DROP:
                break;
            default:
                System.out.println("Route error");
                break;
        }
    }

    @Override
    public void returnToStart() {
        if (this.isReturning) {
            System.out.println("Klaar met activeRoute\t" + this.queueStep);
            this.isFinished = true;
            this.addDelay.addDelay("Finished route cooldown", 3000, () -> {
                this.isFinished = false;
            });
            this.queueStep++;
            this.routeStep = 0;
            System.out.println("Start nieuwe route\t" + this.queueStep);

            this.printRoute();
            return;
        }

        if (this.isPremoving) {
            this.isPremoving = false;
            return;
        }
        System.out.println("Returning");

        this.isReturning = true;
        this.activeRoute = this.reverseRoute(this.activeRoute);
        this.routeStep = 0;
        this.resetBooleans();

        System.out.println("Reversing activeRoute");
        System.out.print("New activeRoute:");
        for (RouteOptions routeOptions : activeRoute) {
            System.out.print("\t" + routeOptions);
        }
        System.out.print("\n");
    }

    private void resetBooleans() {
        this.isOnLastAction = false;
        this.hasPassedLastCrossover = false;
    }
}