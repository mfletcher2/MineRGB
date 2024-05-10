package io.gitlab.mguimard.openrgb.entity;

import java.util.List;


/**
 * Represents a device mode
 */
public class OpenRGBMode {

    private String name;
    private int value;
    private int flags;
    private int speedMin;
    private int speedMax;
    private int colorMin;
    private int colorMax;
    private int speed;
    private ModeDirection direction;
    private ColorMode colorMode;
    private List<OpenRGBColor> colors;

    /**
     * Default empty constructor
     */
    public OpenRGBMode() {
    }

    /**
     * Returns an instance of an OpenRGBMode
     *
     * @param name      the mode name
     * @param value     the mode value
     * @param flags     the mode flags
     * @param speedMin  the minimum speed
     * @param speedMax  the maximum speed
     * @param colorMin  the minimum color
     * @param colorMax  the maximum color
     * @param speed     the mode speed
     * @param direction the mode direction
     * @param colorMode the mode color mode
     * @param colors    the mode specific colors
     */
    public OpenRGBMode(String name, int value, int flags, int speedMin, int speedMax,
                       int colorMin, int colorMax, int speed, ModeDirection direction, ColorMode colorMode,
                       List<OpenRGBColor> colors) {
        this.name = name;
        this.value = value;
        this.flags = flags;
        this.speedMin = speedMin;
        this.speedMax = speedMax;
        this.colorMin = colorMin;
        this.colorMax = colorMax;
        this.speed = speed;
        this.direction = direction;
        this.colorMode = colorMode;
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getSpeedMin() {
        return speedMin;
    }

    public void setSpeedMin(int speedMin) {
        this.speedMin = speedMin;
    }

    public int getSpeedMax() {
        return speedMax;
    }

    public void setSpeedMax(int speedMax) {
        this.speedMax = speedMax;
    }

    public int getColorMin() {
        return colorMin;
    }

    public void setColorMin(int colorMin) {
        this.colorMin = colorMin;
    }

    public int getColorMax() {
        return colorMax;
    }

    public void setColorMax(int colorMax) {
        this.colorMax = colorMax;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ModeDirection getDirection() {
        return direction;
    }

    public void setDirection(ModeDirection direction) {
        this.direction = direction;
    }

    public ColorMode getColorMode() {
        return colorMode;
    }

    public void setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
    }

    public List<OpenRGBColor> getColors() {
        return colors;
    }

    public void setColors(List<OpenRGBColor> openRGBColors) {
        this.colors = openRGBColors;
    }

    @Override
    public String toString() {
        return "OpenRGBMode{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", flags=" + flags +
                ", speedMin=" + speedMin +
                ", speedMax=" + speedMax +
                ", colorMin=" + colorMin +
                ", colorMax=" + colorMax +
                ", speed=" + speed +
                ", direction=" + direction +
                ", colorMode=" + colorMode +
                ", openRGBColors=" + colors +
                "}\n";
    }
}
