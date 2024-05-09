package io.gitlab.mguimard.openrgb.utils;

import io.gitlab.mguimard.openrgb.entity.OpenRGBColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Color manipulation utility class
 */
public class ColorUtils {

    /**
     * Generate an array of OpenRGBColor using HSV, see https://en.wikipedia.org/wiki/Hue#/media/File:Hsv-hues-cf-lch-hues.png
     * <p>
     * hue angle	color code	color name
     * 0°	        #FF0000	    red
     * 15°	        #FF4000	    vermilion
     * 30°	        #FF8000	    orange
     * 45°	        #FFBF00	    golden yellow
     * 60°	        #FFFF00	    yellow
     * 75°	        #BFFF00	    yellowish green
     * 90°	        #80FF00	    yellowish green, chartreuse
     * 105°	        #40FF00	    leaf green
     * 120°	        #00FF00	    green
     * 135°	        #00FF40	    cobalt green
     * 150°	        #00FF80	    emerald green
     * 165°	        #00FFBF	    turquoise green
     * 180°	        #00FFFF	    turquoise blue
     * 195°	        #00BFFF	    cerulean blue
     * 210°	        #0080FF	    azure
     * 225°	        #0040FF	    blue, cobalt blue
     * 240°	        #0000FF	    blue
     * 255°	        #4000FF	    hyacinth
     * 270°	        #8000FF	    violet
     * 285°	        #BF00FF	    purple
     * 300°	        #FF00FF	    magenta
     * 315°	        #FF00BF	    reddish purple
     * 330°	        #FF0080	    ruby red, crimson
     * 345°	        #FF0040	    carmine
     *
     * @param size   palette size
     * @param startH start hue (0-360)
     * @param startS start saturation (0-1)
     * @param startV start value (0-1)
     * @param endH   end hue (0-360)
     * @param endS   end saturation (0-1)
     * @param endV   end value (0-1)
     * @param waves  number of waves (1-n)
     * @return the rainbow array
     */
    public static OpenRGBColor[] generateRainbow(int size, int startH, double startS, double startV, int endH, double endS, double endV, int waves) {

        if (size <= 0) {
            return new OpenRGBColor[0];
        }

        startH = Math.abs(startH);
        startH = Math.min(startH, 360);
        startS = Math.abs(startS);
        startS = Math.min(startS, 1);
        startV = Math.abs(startV);
        startV = Math.min(startV, 1);

        endH = Math.abs(endH);
        endH = Math.min(endH, 360);
        endS = Math.abs(endS);
        endS = Math.min(endS, 1);
        endV = Math.abs(endV);
        endV = Math.min(endV, 1);

        waves = Math.abs(waves);
        waves = Math.max(1, waves);

        boolean reverse = startH > endH;

        if (startH > endH) {
            int tmp = startH;
            startH = endH;
            endH = tmp;
        }

        double stepH = (endH - startH) / (size / waves * 1.0);
        double stepS = (endS - startS) / (size / waves * 1.0);
        double stepV = (endV - startV) / (size / waves * 1.0);

        List<OpenRGBColor> colors = new ArrayList<>();
        List<OpenRGBColor> waveColors = new ArrayList<>();

        for (int i = 0; i < (1.0 * size / waves); i++) {
            float hue = (float) (startH + stepH * i);
            float saturation = (float) (startS + stepS * i);
            float value = (float) (startV + stepV * i);
            waveColors.add(OpenRGBColor.fromHSB(hue, saturation, value));
        }

        if (reverse) {
            Collections.reverse(waveColors);
        }

        List<OpenRGBColor> reversedWaveColors = new ArrayList<>(waveColors);
        Collections.reverse(reversedWaveColors);

        for (int wave = 0; wave < waves; wave++) {
            colors.addAll(wave % 2 == 0 ? waveColors : reversedWaveColors);
        }

        return colors.toArray(new OpenRGBColor[size]);
    }

}
