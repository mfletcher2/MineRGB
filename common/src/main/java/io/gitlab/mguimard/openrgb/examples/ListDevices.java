package io.gitlab.mguimard.openrgb.examples;

import io.gitlab.mguimard.openrgb.client.OpenRGBClient;
import io.gitlab.mguimard.openrgb.entity.OpenRGBDevice;

import java.io.IOException;

public class ListDevices {

    public static void main(String[] args) {
        OpenRGBClient client = new OpenRGBClient("localhost", 6742, "Some client name");

        try {
            client.connect();

            int controllerCount = client.getControllerCount();

            for (int i = 0; i < controllerCount; i++) {
                OpenRGBDevice controller = client.getDeviceController(i);
                System.out.println(controller.getName());
                //System.out.println(controller.getZones());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
