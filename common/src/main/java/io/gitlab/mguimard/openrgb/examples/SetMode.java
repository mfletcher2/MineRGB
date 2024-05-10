package io.gitlab.mguimard.openrgb.examples;

import io.gitlab.mguimard.openrgb.client.OpenRGBClient;
import io.gitlab.mguimard.openrgb.entity.OpenRGBDevice;
import io.gitlab.mguimard.openrgb.entity.OpenRGBMode;

import java.io.IOException;

public class SetMode {

    public static void main(String[] args) {
        OpenRGBClient client = new OpenRGBClient("localhost", 6742, "Some client name");

        int deviceIndex = 0;
        int modeIndex = 2;

        try {
            client.connect();

            OpenRGBDevice controller = client.getDeviceController(deviceIndex);
            OpenRGBMode mode = controller.getModes().get(modeIndex);

            System.out.println(mode);

            mode.setSpeed(100);

            client.updateMode(deviceIndex, modeIndex, mode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
