package hardware.led;

import TI.BoeBot;
import TI.Timer;
import hardware.Updatable;

import java.awt.*;

public class NeoPixelBlinking implements Updatable {

    private int neoPixel1;
    private int neoPixel2;

    private final Timer timeOnOff = new Timer(500);
    private boolean onOff;

    public NeoPixelBlinking(int neoPixel1, int neoPixel2) {
        this.neoPixel1 = neoPixel1;
        this.neoPixel2 = neoPixel2;
        timeOnOff.mark();
    }

    @Override
    public void update() {
        if (timeOnOff.timeout()) {
            BoeBot.rgbSet(neoPixel1, Color.ORANGE);
            BoeBot.rgbSet(neoPixel2, Color.ORANGE);
            onOff = !onOff;
        }
        BoeBot.rgbShow();
    }
}