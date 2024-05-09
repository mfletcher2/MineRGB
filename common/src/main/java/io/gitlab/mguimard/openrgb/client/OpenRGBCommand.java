package io.gitlab.mguimard.openrgb.client;

/**
 * OpenRGB server command codes
 */
enum OpenRGBCommand {
    RequestControllerCount(0),
    RequestControllerData(1),
    GetProtocolVersion(40),
    SetClientName(50),
    RequestProfileList(150),
    SaveProfile(151),
    LoadProfile(152),
    DeleteProfile(153),
    ResizeZone(1000),
    UpdateLeds(1050),
    UpdateZoneLeds(1051),
    UpdateSingleLed(1052),
    SetCustomMode(1100),
    UpdateMode(1101);

    private static final OpenRGBCommand[] allValues = values();
    private final int value;

    OpenRGBCommand(int value) {
        this.value = value;
    }

    public static OpenRGBCommand fromOrdinal(int n) {
        return allValues[n];
    }

    public int getValue() {
        return value;
    }
}
