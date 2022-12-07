package hardware.led;

import TI.BoeBot;
import hardware.Updatable;


import java.awt.*;
import java.util.ArrayList;


public class NeoPixel implements Updatable {



    static private ArrayList<Integer> list;

    public NeoPixel() {
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
    }

    public NeoPixel(int which) {
        list.add(which);
    }

    public static void setColour(float aColor){
       Color color;
        for (Integer integer : list) {
            color = Color.getHSBColor(aColor, 1f, 1f);
            BoeBot.rgbSet(integer, color);
            BoeBot.rgbShow();
        }
    }

    @Override
    public void update() {

    }
}
