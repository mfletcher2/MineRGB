package io.gitlab.mguimard.openrgb.entity;

/**
 * OpenRGB server device types codes
 */
public enum DeviceType {
    DEVICE_TYPE_MOTHERBOARD("Motherboard"),
    DEVICE_TYPE_DRAM("DRAM"),
    DEVICE_TYPE_GPU("GPU"),
    DEVICE_TYPE_COOLER("Cooler"),
    DEVICE_TYPE_LEDSTRIP("LED strip"),
    DEVICE_TYPE_KEYBOARD("Keyboard"),
    DEVICE_TYPE_MOUSE("Mouse"),
    DEVICE_TYPE_MOUSEMAT("Mouse Mat"),
    DEVICE_TYPE_HEADSET("Headset"),
    DEVICE_TYPE_HEADSET_STAND("Headset stand"),
    DEVICE_TYPE_GAMEPAD("Gamepad"),
    DEVICE_TYPE_LIGHT("Light"),
    DEVICE_TYPE_VIRTUAL("Virtual"),
    DEVICE_TYPE_UNKNOWN("Unknown");

    private String name;

    DeviceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
