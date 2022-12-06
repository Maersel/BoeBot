//import TI.BoeBot;
//import TI.PinMode;
//import TI.Servo;
//
//public class RobotMain {
//    public static void main(String[] args) {
//        setModes();
//
////        System.out.println("?");
////        Servo s1 = new Servo(12);
////        s1.update(1700);
//
////        automatic();
////        testInfraRedSensor();
////        remote();
//        newMotorTest();
//    }
//
//    private static void setModes() {
//        // Inputs
//        BoeBot.setMode(0, PinMode.Input);
//        BoeBot.setMode(1, PinMode.Input);
//
//        BoeBot.setMode(14, PinMode.Input);
//        BoeBot.setMode(15, PinMode.Input);
//
//
//        // Outputs
////        BoeBot.setMode(7, PinMode.Output);
//    }
//
//    private static void remote() {
//        controllers.MovementController movement = new controllers.MovementController(12, 13);
//        System.out.println("Listening....");
//        while (true) {
//            int pulseLen = BoeBot.pulseIn(14, false, 6000);
//            if (pulseLen > 2000) {
//                System.out.println("n");
//                int lengths[] = new int[12];
//                for (int i = 0; i < 12; i++) {
//                    lengths[i] = BoeBot.pulseIn(14, false, 3000);
//                }
//                int bits = 0;
//
//                for (int i = 0; i < 12; i++) {
//                    if (lengths[i] > 1100 && lengths[i] < 1300) {
//                        bits = bits | (1 << i);
//                    }
//                }
//
//                System.out.println(Integer.toBinaryString(bits));
//
//                if (bits == 0b11110100) {
//                    System.out.println("Vooruit");
//                    movement.forward();
//                }
//                if (bits == 0b10110011) {
//                    System.out.println("Rechts");
//                    movement.turnRight();
//                }
//                if (bits == 0b10110100) {
//                    System.out.println("Links");
//                    movement.turnLeft();
//                }
//                if (bits == 0b11110101) {
//                    System.out.println("Achteruit");
//                    movement.reverse();
//                }
//                if (bits == 0b11100101) {
//                    System.out.println("Stop");
//                    movement.stop();
//                }
//                if (bits == 0b11011111) {
//                    System.out.println("Grijpen");
//                }
//                if (bits == 0b11100000) {
//                    System.out.println("Loslaten");
//                }
//            } else {
////                System.out.println("?");
////                movement.stop();
//            }
//
//            BoeBot.wait(1);
//        }
//    }
//
//    private static void automatic() {
//        controllers.MovementController movement = new controllers.MovementController(12, 13);
//        boolean whiskerRight;
//        boolean whiskerLeft;
//
//        while (true) {
//            whiskerRight = !BoeBot.digitalRead(0);
//            whiskerLeft = !BoeBot.digitalRead(1);
//
//            if (whiskerLeft || whiskerRight) {
//                System.out.println("stop");
//                movement.reverse();
//                BoeBot.wait(1000);
//                if (Math.random() > 0.5) {
//                    movement.turnLeft();
//                } else {
//                    movement.turnRight();
//                }
//            } else {
//                movement.forward();
//            }
//
//            BoeBot.wait(1);
//        }
//    }
//
//    private static void testTranslate() {
//        // test
//        int[] lengths = {1174, 565, 565, 566, 566, 567, 566, 1173, 567, 566, 566, 566};
//        int outcome = 0b100000010000;
//        int bits = 0;
//
//
//        for (int i = 0; i < 12; i++) {
//            if (lengths[i] > 1100 && lengths[i] < 1300) {
//                System.out.println("true " + i);
//                bits = bits | (1 << i);
//                System.out.println(bits);
//            }
//        }
//
//        System.out.println("\n" + Integer.toBinaryString(bits));
//        System.out.println("\n" + Integer.toBinaryString(outcome));
//    }
//
//    private static void testInfraRedSensor() {
//        //scuffed maar kan een circuit afleggen
//        controllers.MovementController movement = new controllers.MovementController(12, 13);
//
//        while (true) {
//            int leftSensor = BoeBot.analogRead(2);
//            int righSensor = BoeBot.analogRead(3);
//            if (leftSensor > 1500) {
//                System.out.println("Links zwart (naar rechts)");
//                movement.turnLeft();
//            } else if (righSensor > 1500) {
//                System.out.println("Rechts zwart (naar links");
//                movement.turnRight();
//            } else {
//                System.out.println("rechtdoor");
//                movement.forward();
//            }
//            BoeBot.wait(10);
//        }
//    }
//
//    private static void newMotorTest() {
//        hardware.motor.Motor motorRight = new hardware.motor.Motor(12, 1300, 1700, 1500, false);
//        hardware.motor.Motor motorLeft = new hardware.motor.Motor(13, 1300, 1700, 1500, true);
//
////        controllers.MovementController movementController = new controllers.MovementController(12, 13);
//
////        while (true) {
////            motor.goToSpeed(1700);
////            for (int i = 0; i < 500; i++) {
////                motor.update();
////                BoeBot.wait(10);
////            }
////            motor.goToSpeed(1300);
////            for (int j = 0; j < 500; j++) {
////                motor.update();
////                BoeBot.wait(10);
////            }
////        }
//
//        while (true) {
//            int middleSensorPin = 1;
//            int leftSensorPin = 2;
//            int rightSensorPin = 3;
//
////            int middleSensor = BoeBot.analogRead(1);
////            int leftSensor = BoeBot.analogRead(2);
////            int rightSensor = BoeBot.analogRead(3);
//
////            if (middleSensor > 1500) {
////                System.out.println("Op de lijn");
////            }
//
////            if (leftSensor < 1500 && rightSensor < 1500) {
////                System.out.println("rechtdoor");
////                motorLeft.goToSpeed(1450);
////                motorRight.goToSpeed(1550);
////            } else if (leftSensor > 1500) {
////                System.out.println("Naar rechts");
////                motorLeft.goToSpeed(1400);
////                motorRight.goToSpeed(1500);
////            } else if (rightSensor > 1450) {
////                System.out.println("Naar links");
////                motorLeft.goToSpeed(1500);
////                motorRight.goToSpeed(1600);
////            }
//
////            if (detect(middleSensorPin)) {
////                motorLeft.goToSpeed(1450);
////                motorRight.goToSpeed(1550);
////            }
//
////            System.out.println("Middle sensor: " + detect(middleSensorPin));
////            System.out.println("Left sensor: " + detect(leftSensorPin));
////            System.out.println("Right sensor: " + detect(rightSensorPin));
////            System.out.println("");
//
//            if (detect(middleSensorPin)) {
//                System.out.println("Rechtdoor");
//                motorLeft.goToSpeed(1450);
//                motorRight.goToSpeed(1550);
//            } else if (detect(leftSensorPin)) {
//                System.out.println("Corrigeer links");
//                motorLeft.goToSpeed(1500);
//                motorRight.goToSpeed(1400);
//            } else if (detect(rightSensorPin)) {
//                System.out.println("Corrigeer rechts");
//                motorRight.goToSpeed(1600);
//                motorLeft.goToSpeed(1500);
//            } else {
//                System.out.println("STOP");
//                motorLeft.stop();
//                motorRight.stop();
//            }
//
//            motorLeft.update();
//            motorRight.update();
//            BoeBot.wait(100);
//        }
//    }
//
//    private static boolean detect(int pin) {
//        return (BoeBot.analogRead(pin) > 1500);
//    }
//}
