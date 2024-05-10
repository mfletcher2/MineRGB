package io.gitlab.mguimard.openrgb.entity;

/**
 * OpenRGB server color mode codes
 */
public enum ColorMode {

    MODE_COLORS_NONE("No colors mode"),
    MODE_COLORS_PER_LED("Color per LED mode"),
    MODE_COLORS_MODE_SPECIFIC("Specific colors mode"),
    MODE_COLORS_RANDOM("Random colors mode");

    private String name;

    ColorMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
