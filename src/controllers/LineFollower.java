
package controllers;

import controllers.pathfinding.Pathfinder;
import hardware.Updatable;
import hardware.gripper.Gripper;
import hardware.linesensor.Callback;
import hardware.linesensor.InfraRed;
import javafx.scene.input.PickResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static controllers.RouteOptions.STRAIGHT;

public class LineFollower implements Callback, Updatable {
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
    private boolean hasEndGoal;


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

        ArrayList<RouteOptions[]> queue = new ArrayList<>();

        queue.add(this.route);
        queue.add(this.reverseRoute(this.route));
        queue.add(this.route);
//        if (this.step == this.route.length) {
//            System.out.println("laatste stap");
////            this.route = this.reverseRoute(this.route);
//        }

        if (!this.isFinished) {
            if (this.step == this.route.length) this.isFinished = true;
            this.step = (this.step + 1) % this.route.length;
        }

    }

    public void setRoute(int startingPoint, int endPoint) {
        ArrayList<Integer> path = pathfinder.nodePath(startingPoint, endPoint);

        this.route = pathfinder.pathDirections(path);
        this.step = 0;
    }

    public void routePickUp() {
        if (hasEndGoal) return;
        this.hasEndGoal = true;
        this.appendRouteCommand(RouteOptions.PICK_UP);
    }

    public void routeDrop() {
        if (hasEndGoal) return;
        this.hasEndGoal = true;
        this.prependRouteCommand(RouteOptions.PICK_UP);
        this.appendRouteCommand(RouteOptions.DROP);
    }

    private void appendRouteCommand(RouteOptions command) {
        RouteOptions[] newRoute = Arrays.copyOf(this.route, this.route.length + 1);
        newRoute[newRoute.length - 1] = command;
        this.route = newRoute;
    }

    private void prependRouteCommand(RouteOptions command) {
        RouteOptions[] newRoute = new RouteOptions[this.route.length + 1];
        for (int i = 0; i < this.route.length; i++) {
            newRoute[i + 1] = this.route[i];
        }

        newRoute[0] = command;
        this.route = newRoute;
    }

    public void printRoute() {
        for (RouteOptions routeOptions : this.route) {
            System.out.println(routeOptions);
        }

        System.out.println("\nNieuw\n");

        for (RouteOptions routeOptions : this.reverseRoute(this.route)) {
            System.out.println(routeOptions);
        }
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
        // Stop zodra hij heel de route heeft afgelegd
        if (this.isFinished) return;

        switch (this.lineDetection) {
            case 0b000:
                // GEEN DETECTIE (ziet wit)
                if (this.step == this.route.length - 1 && !this.movementController.isTurning()) {
                    this.movementController.stop();
                    this.executeRouteCommand(this.route[this.step]);
                }
                break;
            case 0b001:
                // ALLEEN RECHTS DETECTIE
                this.movementController.correctToTheRight();
                break;
            case 0b010:
                // ALLEEN MIDDEN DETECTIE
                this.movementController.turnOffTurning();
                this.movementController.forward();
                break;
            case 0b011:
                // RECHTS EN MIDDEN DETECTIE
                break;
            case 0b100:
                // ALLEEN LINKS DETECTIE
                this.movementController.correctToTheLeft();
                break;
            case 0b101:
                // LINKS EN RECHTS DETECTIE
                break;
            case 0b110:
                // LINKS EN MIDDEN DETECTIE
                break;
            case 0b111:
                // KRUISPUNT

                if (this.isOnCrossover) break;
                System.out.println("KRUISPUNT!\tstap: " + this.step + "\t" + this.route[step]);

                this.isOnCrossover = true;
                this.addDelay.addDelay("Crossing crossover", 400, () -> {
                    this.isOnCrossover = false;
                });

                this.executeRouteCommand(this.route[this.step]);
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
                this.movementController.turnAround();
                this.gripper.close();
                this.isTurningAround = true;

                System.out.println("WOOOOOOOOOOOOO");

                break;
            case DROP:
                this.gripper.open();
                break;
            default:
                System.out.println("Route error");
                break;
        }
    }
}