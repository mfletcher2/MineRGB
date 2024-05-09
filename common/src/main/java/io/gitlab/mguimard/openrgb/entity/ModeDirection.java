package io.gitlab.mguimard.openrgb.entity;

/**
 * OpenRGB server mode directions codes
 */
public enum ModeDirection {
    MODE_DIRECTION_LEFT("Left"),
    MODE_DIRECTION_RIGHT("Right"),
    MODE_DIRECTION_UP("Up"),
    MODE_DIRECTION_DOWN("Down"),
    MODE_DIRECTION_HORIZONTAL("Horizontal"),
    MODE_DIRECTION_VERTICAL("Vertical");

    private String name;

    ModeDirection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
