package io.gitlab.mguimard.openrgb.examples;

import io.gitlab.mguimard.openrgb.client.OpenRGBClient;

import java.io.IOException;

public class ResizeZone {

    public static void main(String[] args) {
        OpenRGBClient client = new OpenRGBClient("localhost", 6742, "Some client name");

        try {
            client.connect();

            int deviceIndex = 1;
            int zoneIndex = 1;
            int size = 16;

            client.resizeZone(deviceIndex, zoneIndex, size);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
