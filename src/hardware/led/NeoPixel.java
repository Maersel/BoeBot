package hardware.led;

import TI.BoeBot;
import TI.Timer;
import hardware.Updatable;

import java.awt.*;


public class NeoPixel implements Updatable {

    private Boolean rightOn;
    private Boolean leftOn;
    private Timer timeOn;
    private Timer timeOff;

    private boolean timerOn = false;

    private float aColor;
    private boolean onOff;

    public NeoPixel(Timer timeOn, Timer timeOff, float aColor) {
        this.timeOn = timeOn;
        this.timeOff = timeOff;
        this.aColor = aColor;
        this.rightOn = true;
        this.leftOn = true;
        timeOff.mark();
        timeOn.mark();
    }

    public NeoPixel() {

    }

    public void setColour(float aColor, boolean onOff){
       Color color;
       boolean state = onOff;
       float bn = 0f;
       if (state){
           bn = 0.4f;
       }
        for (int i = 0; i < 6; i++) {
            color = Color.getHSBColor(aColor, 1f, bn);
            BoeBot.rgbSet(i, color);
        }
    }

    public void reset(){
        Color color = Color.getHSBColor(0f, 0f, 0f);
        for (int i = 0; i < 6; i++) {
            BoeBot.rgbSet(i, color);
        }
    }

    public void blinkingLeft(){
        Color color;
        float bn;

        if (!timerOn) {
            timeOff.mark();
            timeOn.mark();
            timerOn = true;
        }

        if (timeOn.timeout() && leftOn) {
            leftOn = false;
            bn = 0.4f;
            color = Color.getHSBColor(aColor, 1f, bn);
            BoeBot.rgbSet(0, color);
            BoeBot.rgbSet(5, color);
            timerOn = false;
        }

        if (timeOff.timeout() && !leftOn) {
            leftOn = true;
            bn = 0f;
            color = Color.getHSBColor(aColor, 1f, bn);
            BoeBot.rgbSet(0, color);
            BoeBot.rgbSet(5, color);
            timerOn = false;
        }
    }

    public void blinkingRight(){
        Color color;
        float bn;

        if (!timerOn) {
            timeOff.mark();
            timeOn.mark();
            timerOn = true;
        }

        if (timeOn.timeout() && rightOn) {
            rightOn = false;
            bn = 0.4f;
            color = Color.getHSBColor(aColor, 1f, bn);
            BoeBot.rgbSet(2, color);
            BoeBot.rgbSet(3, color);
            timerOn = false;
        }

        if (timeOff.timeout() && !rightOn) {
            rightOn = true;
            bn = 0f;
            color = Color.getHSBColor(aColor, 1f, bn);
            BoeBot.rgbSet(2, color);
            BoeBot.rgbSet(3, color);
            timerOn = false;
        }
    }

    @Override
    public void update() {
        BoeBot.rgbShow();
    }
}
