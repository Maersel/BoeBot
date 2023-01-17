package controllers;

import hardware.Updatable;
import hardware.gripper.Gripper;
import hardware.ultrasonic.Callback;

public class PickUpDropController implements Updatable, Callback {
    private MovementController movementController;
    private AddDelay addDelay;
    private Gripper gripper;
    private LineFollowerCallback callback;

    private RouteOptions target;
    private RouteOptions turningDirection;
    private boolean hasTurnedAround;
    private boolean isTurnedOn;
    private boolean isCloseEnough;
    private boolean doingTheThing;
    private boolean hasPayload;

    public PickUpDropController(MovementController movementController, AddDelay addDelay, Gripper gripper, LineFollowerCallback callback) {
        this.movementController = movementController;
        this.addDelay = addDelay;
        this.gripper = gripper;
        this.callback = callback;
    }

    public void turnOn(RouteOptions target) {
        if (this.isTurnedOn) return;

        System.out.println("Turning PickUpDropController ON");
        this.isTurnedOn = true;
        this.target = target;

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
                    this.turnOff();
                }
            }
        }

    }

    private void turnAround() {
        if (!hasTurnedAround) {
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
}
