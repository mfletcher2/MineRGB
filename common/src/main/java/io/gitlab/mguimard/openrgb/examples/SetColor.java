package io.gitlab.mguimard.openrgb.examples;

import io.gitlab.mguimard.openrgb.client.OpenRGBClient;
import io.gitlab.mguimard.openrgb.entity.OpenRGBColor;
import io.gitlab.mguimard.openrgb.entity.OpenRGBDevice;

import java.io.IOException;
import java.util.Arrays;

public class SetColor {

    public static void main(String[] args) {
        OpenRGBClient client = new OpenRGBClient("localhost", 6742, "Some client name");

        try {
            client.connect();

            int deviceIndex = 0;

            OpenRGBDevice controller = client.getDeviceController(deviceIndex);
            System.out.println(controller);

            OpenRGBColor[] openRGBColors = new OpenRGBColor[controller.getLeds().size()];
            Arrays.fill(openRGBColors, OpenRGBColor.fromHexaString("#00FF00"));

            client.updateLeds(deviceIndex, openRGBColors);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
