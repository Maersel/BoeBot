package controllers;

import hardware.Updatable;
import hardware.gripper.Gripper;
import hardware.ultrasonic.Callback;
import hardware.ultrasonic.UltraSonic;

public class PickUpDropController implements Updatable, Callback {
    private MovementController movementController;
    private StateController stateController;
    private AddDelay addDelay;
    private Gripper gripper;
    private LineFollowerCallback callback;
    private UltraSonic ultraSonic;

    private RouteOptions target;
    private RouteOptions turningDirection;
    private boolean hasTurnedAround;
    private boolean isTurnedOn;
    private boolean isCloseEnough;
    private boolean doingTheThing;
    private boolean hasPayload;

    public PickUpDropController(MovementController movementController, StateController stateController, AddDelay addDelay, Gripper gripper, LineFollowerCallback callback, UltraSonic ultraSonic) {
        this.movementController = movementController;
        this.stateController = stateController;
        this.addDelay = addDelay;
        this.gripper = gripper;
        this.callback = callback;
        this.ultraSonic = ultraSonic;
    }

    public void turnOn(RouteOptions target) {
        if (this.isTurnedOn) return;

        System.out.println("Turning PickUpDropController ON");

        this.stateController.changeState(Configuration.PICK_UP_DROP_STATE);
        this.isTurnedOn = true;
        this.target = target;
        System.out.println("\t\t1");
        this.ultraSonic.turnOn();
        System.out.println("\t\t2");
        if (this.target == RouteOptions.PICK_UP) this.hasPayload = false;
        if (this.target == RouteOptions.DROP) this.hasPayload = true;
    }

    public void turnOff() {
        System.out.println("Turning PickUpDropController OFF");
        this.isTurnedOn = false;
        this.hasTurnedAround = false;
        this.isCloseEnough = false;
        this.doingTheThing = false;
        this.hasPayload = false;
        this.target = null;

        this.ultraSonic.turnOff();
    }

    public void forcehasTurnedAround() {
        this.hasTurnedAround = true;
        this.turningDirection = RouteOptions.NOTHING;
    }

    @Override
    public void update() {
        if (this.isTurnedOn) {
            if (this.target == RouteOptions.PICK_UP) {
                if (!this.gripper.isOpen() && !doingTheThing) this.gripper.open();
//                System.out.println("before turn");
                this.turnAround();
                this.lineCorrection();

//                this.gripper.printCurrentSpeed();

                if (this.doingTheThing && this.isCloseEnough) {
                    this.movementController.stop();
                    this.gripper.close();
                    this.hasPayload = true;
                }

//                System.out.println(this.hasPayload + "\t" + this.gripper.isClosed());

                if (this.hasPayload && gripper.isClosed()) {
                    System.out.println("BEGIN OPNIEUW");
                    this.callback.returnToStart();
                    this.ultraSonic.turnOff();
                    this.turnOff();
                    return;
                }
            }


            if (this.target == RouteOptions.DROP) {
                if (!this.gripper.isClosed() && !this.doingTheThing) this.gripper.close();

                this.turnAround();
                this.lineCorrection();

                if (this.doingTheThing && this.hasPayload) {
                    this.addDelay.addDelay("temp", 1000, () -> {
                        this.movementController.stop();
                        this.gripper.open();
                    });
                    this.hasPayload = false;
                }

                if (!this.hasPayload && gripper.isOpen()) {
                    System.out.println("TURN OFF");
                    this.callback.returnToStart();
                    this.ultraSonic.turnOff();
                    this.turnOff();
                }
            }
        }

    }

    private void turnAround() {
        if (!hasTurnedAround) {
            System.out.println("turnaround()");
            this.turningDirection = this.movementController.turnAround();
            this.hasTurnedAround = true;
        }
    }

    private void lineCorrection() {
        // Als hij klaar is met draaien
        if (!this.movementController.isTurning() && this.hasTurnedAround && !this.doingTheThing) {
            this.doingTheThing = true;

            this.movementController.backwards();

            if (this.turningDirection == RouteOptions.LEFT) {
                this.movementController.correctToTheLeft();
            } else if (this.turningDirection == RouteOptions.RIGHT) {
                this.movementController.correctToTheRight();
            }

            this.addDelay.addDelay("180 correctie", 400, () -> {
                this.movementController.backwards();
            });
        }
    }

    @Override
    public void onUltraSonic(int distance) {
        if (distance > 3 || distance < 1) {
            this.isCloseEnough = false;
            return;
        }
        this.isCloseEnough = true;
    }

    public void setUltrasonic(UltraSonic ultraSonicRear) {
        this.ultraSonic = ultraSonicRear;
        this.ultraSonic.turnOff();
    }
}
