package controllers;

import TI.BoeBot;
import hardware.Updatable;

public class RemoteController implements Updatable {

    public RemoteController() {
    }

    public boolean remotePressed() {
        return false;
    }

    public int changeState() {
        return Configuration.LINE_FOLLOWING_STATE;
    }

    @Override
    public void update() {
        System.out.println(BoeBot.digitalRead(1));
//        int pulseLen = BoeBot.pulseIn(0, false, 6000);
//        if (pulseLen > 2000) {
//            int lengths[] = new int[7];
//            for (int i = 6; i >= 0; i--) {
//                lengths[i] = BoeBot.pulseIn(0, false,
//                        20000);
//            }
//            int number = 0;
//            int i = 7;
//            for (int length : lengths) {
//                i--;
//
//                if (length < 700) {
//                    length = 0;
//                } else if (length < 1300) {
//                    length = 1;
//                }
//                number += length << i;
//            }
//            System.out.println(number);
//        }


//                int bits = 0;
//
//                for (int i = 0; i < 12; i++) {
//                    if (lengths[i] > 1100 && lengths[i] < 1300) {
//                        bits = bits | (1 << i);
//                    }
//                }
    }
}
