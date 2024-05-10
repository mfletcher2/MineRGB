package io.gitlab.mguimard.openrgb.utils;

import io.gitlab.mguimard.openrgb.entity.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Device parser class. Internal usage.
 */
public class DeviceParser {

    /**
     * Parses and returns an OpenRGBDevice
     *
     * @param buffer         the buffer sent by the server
     * @param serverProtocol the server protocol version
     * @return the device information
     * @throws IOException well you know...
     */
    public static OpenRGBDevice from(ByteBuffer buffer, int serverProtocol) throws IOException {
        OpenRGBDevice device = new OpenRGBDevice();

        ByteArrayInputStream byteArrayInputStream =
                new ByteArrayInputStream(buffer.array());
        LittleEndianInputStream input = new LittleEndianInputStream(byteArrayInputStream);

        // duplicatePacketLength (int)
        input.readInt();

        device.setType(DeviceType.values()[input.readInt()]);
        device.setName(input.readAscii());
        device.setVendor(serverProtocol >= 1 ? input.readAscii() : null);
        device.setDesc(input.readAscii());
        device.setVersion(input.readAscii());
        device.setSerial(input.readAscii());
        device.setLocation(input.readAscii());

        int modeCount = input.readUnsignedShort();
        device.setActiveMode(input.readInt());
        device.setModes(readModes(input, modeCount));

        int zoneCount = input.readUnsignedShort();
        device.setZones(readZones(input, zoneCount));

        int ledCount = input.readUnsignedShort();
        device.setLeds(readLeds(input, ledCount));

        int colorCount = input.readUnsignedShort();
        device.setColors(readColors(input, colorCount));

        return device;
    }

    private static List<OpenRGBMode> readModes(LittleEndianInputStream input, int modeCount) throws IOException {

        List<OpenRGBMode> openRGBModes = new ArrayList<>();

        for (int modeIndex = 0; modeIndex < modeCount; modeIndex++) {
            String modeName = input.readAscii();

            int value = input.readInt();
            int flags = input.readInt();

            FlagsChecker flagsChecker = new FlagsChecker(flags);

            int speedMin = 0;
            int speedMax = 0;

            if (flagsChecker.hasSpeed()) {
                speedMin = input.readInt();
                speedMax = input.readInt();
            } else {
                input.skip(8);
            }

            int colorMin = 0;
            int colorMax = 0;

            if (flagsChecker.hasModeSpecificColor()) {
                colorMin = input.readInt();
                colorMax = input.readInt();
            } else {
                input.skip(8);
            }

            int speed = 0;
            if (flagsChecker.hasSpeed()) {
                speed = input.readInt();
            } else {
                input.skip(4);
            }

            ModeDirection direction;
            if (flagsChecker.hasDirection()) {
                direction = ModeDirection.values()[input.readInt()];
            } else {
                direction = ModeDirection.MODE_DIRECTION_LEFT;
                input.skip(4);
            }

            ColorMode colorMode = ColorMode.values()[input.readInt()];
            int colorLength = input.readUnsignedShort();

            List<OpenRGBColor> openRGBColors = new ArrayList<>();

            for (int colorIndex = 0; colorIndex < colorLength; colorIndex++) {
                OpenRGBColor openRGBColor = readColor(input);
                openRGBColors.add(openRGBColor);
            }

            openRGBModes.add(new OpenRGBMode(modeName, value, flags, speedMin, speedMax, colorMin, colorMax, speed, direction, colorMode, openRGBColors));
        }

        return openRGBModes;
    }

    private static List<OpenRGBZone> readZones(LittleEndianInputStream input, int zoneCount) throws IOException {
        List<OpenRGBZone> openRGBZones = new ArrayList<>();

        for (int zoneIndex = 0; zoneIndex < zoneCount; zoneIndex++) {
            String zoneName = input.readAscii();
            ZoneType type = ZoneType.values()[input.readInt()];
            int ledsMin = input.readInt();
            int ledsMax = input.readInt();
            int ledsCount = input.readInt();

            int matrixSize = input.readUnsignedShort();
            int height = 0;
            int width = 0;

            if (matrixSize > 0) {
                height = input.readInt();
                width = input.readInt();
            }

            int[][] matrix = new int[height][width];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    matrix[i][j] = input.readInt();
                }
            }

            openRGBZones.add(new OpenRGBZone(
                    zoneName, type, ledsMin, ledsMax, ledsCount, height, width, matrix));
        }

        return openRGBZones;
    }

    private static List<OpenRGBLed> readLeds(LittleEndianInputStream input, int ledCount) throws IOException {
        List<OpenRGBLed> leds = new ArrayList<>();
        for (int ledIndex = 0; ledIndex < ledCount; ledIndex++) {
            String ledName = input.readAscii();
            int value = input.readInt();
//            OpenRGBColor openRGBColor = readColor(input);
            leds.add(new OpenRGBLed(ledName, value));
        }
        return leds;
    }

    private static List<OpenRGBColor> readColors(LittleEndianInputStream input, int colorCount) throws IOException {
        List<OpenRGBColor> colors = new ArrayList<>();
        for (int colorIndex = 0; colorIndex < colorCount; colorIndex++) {
            colors.add(readColor(input));
        }
        return colors;
    }

    private static OpenRGBColor readColor(LittleEndianInputStream input) throws IOException {
        OpenRGBColor color = new OpenRGBColor(
                (byte) input.read(),
                (byte) input.read(),
                (byte) input.read());
        input.skip(1);
        return color;
    }

}