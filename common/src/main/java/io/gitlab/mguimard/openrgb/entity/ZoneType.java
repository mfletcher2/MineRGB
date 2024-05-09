package io.gitlab.mguimard.openrgb.entity;

/**
 * OpenRGB zone type codes
 */
public enum ZoneType {
    ZONE_TYPE_SINGLE("Single zone"),
    ZONE_TYPE_LINEAR("Linear zone"),
    ZONE_TYPE_MATRIX("Matrix zone");

    private String name;

    ZoneType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
