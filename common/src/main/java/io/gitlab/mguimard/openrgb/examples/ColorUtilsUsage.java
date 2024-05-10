package io.gitlab.mguimard.openrgb.examples;

import io.gitlab.mguimard.openrgb.entity.OpenRGBColor;
import io.gitlab.mguimard.openrgb.utils.ColorUtils;


public class ColorUtilsUsage {

    public static void main(String[] args) {
        int size = 10;

        OpenRGBColor[] openRGBColors = ColorUtils.generateRainbow(size, 0, 1.0, 1.0, 100, 1.0, 1.0, 1);

        for (OpenRGBColor color : openRGBColors) {
            System.out.println(color);
        }
    }

}
