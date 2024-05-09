package io.gitlab.mguimard.openrgb.examples;

import io.gitlab.mguimard.openrgb.client.OpenRGBClient;
import io.gitlab.mguimard.openrgb.entity.OpenRGBColor;
import io.gitlab.mguimard.openrgb.entity.OpenRGBDevice;
import io.gitlab.mguimard.openrgb.entity.OpenRGBZone;

import java.io.IOException;
import java.util.Arrays;

public class SetZoneColor {

    public static void main(String[] args) {
        OpenRGBClient client = new OpenRGBClient("localhost", 6742, "Some client name");

        try {
            client.connect();

            int deviceIndex = 1;
            int zoneIndex = 0;

            OpenRGBDevice controller = client.getDeviceController(deviceIndex);
            OpenRGBZone zone = controller.getZones().get(zoneIndex);

            OpenRGBColor[] openRGBColors = new OpenRGBColor[zone.getLedsCount()];
            Arrays.fill(openRGBColors, OpenRGBColor.fromHexaString("#FFFF00"));

            client.updateZoneLeds(deviceIndex, zoneIndex, openRGBColors);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
