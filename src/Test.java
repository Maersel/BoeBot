//import TI.BoeBot;
//import TI.SerialConnection;
//import controllers.LineFollower;
//import controllers.pathfinding.Pathfinder;
//import hardware.Updatable;
//import hardware.bluetooth.Bluetooth;
//import hardware.button.Button;
//import hardware.led.NeoPixel;
//import java.util.ArrayList;
//
//public class Test {
//
//    //     ---------
//    private ArrayList<Updatable> devices;
//
//    private ArrayList<Updatable> allwaysOnDevices;
//    private ArrayList<Updatable> restDevices;
//    private ArrayList<Updatable> bluetoothDevices;
//    private ArrayList<Updatable> lineFollowingDevices;
//    private ArrayList<Updatable> remoteDevices;
//    private ArrayList<Updatable> goatKillingDevices;
//    private ArrayList<Updatable> emergencyDevices;
//    //     ---------
//
//    private Pathfinder pathfinder;
//
//    private Bluetooth bluetooth;
//
//
//
//    private LineFollower lineFollower;
//
//
//    private Button emergencyButton;
//
//
//
//
//    public ArrayList<Updatable> getDevices() {
//        return devices;
//    }
//
//    public void init() {
//        this.pathfinder = new Pathfinder();
//
//        this.bluetooth = new Bluetooth(null, null, this.lineFollower, null, new NeoPixel());
//    }
//
//    public void run() {
//        while (true) {
//            bluetooth.update();
//            BoeBot.wait(1);
//        }
//    }
//
//
//
//
//
//
//}




//import hardware.led.NeoPixel;
//
//import java.awt.*;
//
//public class Test {
//
//
//}




