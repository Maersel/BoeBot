package hardware.led;

import TI.BoeBot;
import hardware.Updatable;


import java.awt.*;
import java.util.ArrayList;


public class NeoPixel implements Updatable {

    public static void setColour(float aColor){
       Color color;
        for (int i = 0; i < 6; i++) {
            color = Color.getHSBColor(aColor, 1f, 1f);
            BoeBot.rgbSet(i, color);
        }
    }

    @Override
    public void update() {
        BoeBot.rgbShow();
    }
}
