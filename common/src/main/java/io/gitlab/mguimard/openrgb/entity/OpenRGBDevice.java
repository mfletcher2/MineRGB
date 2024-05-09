package io.gitlab.mguimard.openrgb.entity;

import java.util.List;

/**
 * Represents a device in OpenRGB server
 */
public class OpenRGBDevice {

    private io.gitlab.mguimard.openrgb.entity.DeviceType type;
    private String name;
    private String vendor;
    private String desc;
    private String version;
    private String serial;
    private String location;
    private int activeMode;
    private List<io.gitlab.mguimard.openrgb.entity.OpenRGBLed> leds;
    private List<io.gitlab.mguimard.openrgb.entity.OpenRGBColor> colors;
    private List<io.gitlab.mguimard.openrgb.entity.OpenRGBMode> modes;
    private List<io.gitlab.mguimard.openrgb.entity.OpenRGBZone> zones;

    /**
     * Default empty constructor
     */
    public OpenRGBDevice() {
    }


    /**
     * Constructor with all fields
     *
     * @param type       the device type
     * @param name       the device name
     * @param vendor     the device vendor
     * @param desc       the device description
     * @param version    the device version
     * @param serial     the device serial number
     * @param location   the device location
     * @param activeMode the device active mode index
     * @param leds       the device LED array
     * @param colors     the device colors
     * @param modes      the device supported modes
     * @param zones      the devices zones
     */
    public OpenRGBDevice(io.gitlab.mguimard.openrgb.entity.DeviceType type, String name, String vendor, String desc, String version, String serial, String location, int activeMode, List<io.gitlab.mguimard.openrgb.entity.OpenRGBLed> leds, List<io.gitlab.mguimard.openrgb.entity.OpenRGBColor> colors, List<io.gitlab.mguimard.openrgb.entity.OpenRGBMode> modes, List<io.gitlab.mguimard.openrgb.entity.OpenRGBZone> zones) {
        this.type = type;
        this.name = name;
        this.vendor = vendor;
        this.desc = desc;
        this.version = version;
        this.serial = serial;
        this.location = location;
        this.activeMode = activeMode;
        this.leds = leds;
        this.colors = colors;
        this.modes = modes;
        this.zones = zones;
    }

    public io.gitlab.mguimard.openrgb.entity.DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getActiveMode() {
        return activeMode;
    }

    public void setActiveMode(int activeMode) {
        this.activeMode = activeMode;
    }

    public List<io.gitlab.mguimard.openrgb.entity.OpenRGBLed> getLeds() {
        return leds;
    }

    public void setLeds(List<OpenRGBLed> leds) {
        this.leds = leds;
    }

    public List<io.gitlab.mguimard.openrgb.entity.OpenRGBColor> getColors() {
        return colors;
    }

    public void setColors(List<OpenRGBColor> colors) {
        this.colors = colors;
    }

    public List<io.gitlab.mguimard.openrgb.entity.OpenRGBMode> getModes() {
        return modes;
    }

    public void setModes(List<OpenRGBMode> modes) {
        this.modes = modes;
    }

    public List<io.gitlab.mguimard.openrgb.entity.OpenRGBZone> getZones() {
        return zones;
    }

    public void setZones(List<OpenRGBZone> zones) {
        this.zones = zones;
    }

    @Override
    public String toString() {
        return "OpenRGBDevice{\n" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", vendor='" + vendor + '\'' +
                ", desc='" + desc + '\'' +
                ", version='" + version + '\'' +
                ", serial='" + serial + '\'' +
                ", location='" + location + '\'' +
                ", activeMode=" + activeMode +
                ",\n leds=" + leds +
                ",\n colors=" + colors +
                ",\n modes=" + modes +
                ",\n zones=" + zones +
                "\n}";
    }
}
