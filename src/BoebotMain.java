import controllers.StateController;

public class BoebotMain {
    public static void main(String[] args) {
        StateController boebot = new StateController();
        boebot.init();
        boebot.run();
    }
}

