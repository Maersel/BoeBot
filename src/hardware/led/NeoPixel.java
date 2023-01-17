package hardware.led;

import TI.BoeBot;
import TI.Timer;
import hardware.Updatable;

import java.awt.*;


public class NeoPixel implements Updatable {

    private boolean isOn = false;
    private Color aColor;

    public NeoPixel(Color aColor) {
        this.aColor = aColor;
    }

    public void setColour(){
       if (isOn){
           for (int i = 0; i < 6; i++) {
               BoeBot.rgbSet(i, this.aColor);
           }
       } else {
           for (int i = 0; i < 6; i++) {
               BoeBot.rgbSet(i, Color.BLACK);
           }
       }
    }

    public void reset(){
        for (int i = 0; i < 6; i++) {
            BoeBot.rgbSet(i, Color.BLACK);
        }
    }

    @Override
    public void update() {
        BoeBot.rgbShow();
    }
}