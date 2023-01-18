package hardware.led;

import TI.BoeBot;
import TI.Timer;
import hardware.Updatable;

import java.awt.*;

public class NeoPixelBlinking implements Updatable {

    private int neoPixel1;
    private int neoPixel2;

    private final Timer timeOnOff = new Timer(500);
    private boolean onOff = true;
    private boolean turning = false;

    public NeoPixelBlinking(int neoPixel1, int neoPixel2) {
        this.neoPixel1 = neoPixel1;
        this.neoPixel2 = neoPixel2;
        timeOnOff.mark();
    }

    public void setTurningOn(){
        turning = true;
    }
    public void setTurningOff(){
        turning = false;
    }

    @Override
    public void update() {
        if (turning) {
            if (timeOnOff.timeout()) {
                if (onOff){
                    BoeBot.rgbSet(neoPixel1, Color.ORANGE);
                    BoeBot.rgbSet(neoPixel2, Color.ORANGE);
                } else {
                    BoeBot.rgbSet(neoPixel1, Color.BLACK);
                    BoeBot.rgbSet(neoPixel2, Color.BLACK);
                }
                onOff = !onOff;
            }
        } else {
            BoeBot.rgbSet(neoPixel1, Color.black);
        }
        BoeBot.rgbShow();
    }
}