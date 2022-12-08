
package controllers;

import hardware.Updatable;
import hardware.linesensor.Callback;
import hardware.linesensor.InfraRed;

public class LineFollower implements Callback, Updatable {
    private MovementController movementController;
    private InfraRed leftSensor;
    private InfraRed middleSensor;
    private InfraRed rightSensor;

    //
    private int lineDetection;


    public LineFollower(MovementController movementController, InfraRed leftSensor, InfraRed rightSensor, InfraRed middleSensor) {
        this.movementController = movementController;
        this.leftSensor = leftSensor;
        this.middleSensor = middleSensor;
        this.rightSensor = rightSensor;
        this.lineDetection = 0;

        this.setCallbacks();
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
                System.out.println("geen detectie");
//                this.movementController.stop();
                this.movementController.forward();
                break;
            case 0b001:
                System.out.println("Lijn alleen rechts");
                this.movementController.correctRight();
                break;
            case 0b010:
                System.out.println("Lijn alleen middel");
                this.movementController.forward();
                break;
            case 0b011:
                System.out.println("Lijn middel en rechts");
                this.movementController.correctRight();
                break;
            case 0b100:
                System.out.println("Lijn alleen links");
                this.movementController.correctLeft();
                break;
            case 0b101:
                System.out.println("Lijn links en rechts");
                break;
            case 0b110:
                System.out.println("Lijn middel en links");
                this.movementController.correctLeft();
                break;
            case 0b111:
                System.out.println("Lijn middel, links en rechts");
                //this.movementController.emergencyStop();
                this.movementController.turnRight();
                break;
            default:
                System.out.println("de lul lmao");
                break;
        }

        this.lineDetection = 0;
    }
}