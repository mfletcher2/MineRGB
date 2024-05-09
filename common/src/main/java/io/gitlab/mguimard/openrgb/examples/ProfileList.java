package io.gitlab.mguimard.openrgb.examples;

import io.gitlab.mguimard.openrgb.client.OpenRGBClient;

import java.io.IOException;

public class ProfileList {

    public static void main(String[] args) {
        OpenRGBClient client = new OpenRGBClient("localhost", 6742, "Some client name");

        try {
            client.connect();

            String[] profileList = client.getProfileList();

            for (String profile : profileList) {
                System.out.println(profile);
            }
            if(profileList.length > 0) {
                client.loadProfile(profileList[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
