package io.gitlab.mguimard.openrgb.entity;

import java.util.Arrays;

/**
 * Represents a device zone
 */
public class OpenRGBZone {
    private final int matrixHeight;
    private final int matrixWidth;
    private final int[][] matrix;
    private String name;
    private ZoneType type;
    private int ledsMin;
    private int ledsMax;
    private int ledsCount;

    /**
     * Returns an instance of an OpenRGBZone
     *
     * @param name         the zone name
     * @param type         the zone type
     * @param ledsMin      the minimum leds
     * @param ledsMax      the maximum leds
     * @param ledsCount    the leds count
     * @param matrixHeight the height of the matrix
     * @param matrixWidth  the width of the matrix
     * @param matrix       the matrix
     */
    public OpenRGBZone(String name, ZoneType type, int ledsMin, int ledsMax, int ledsCount, int matrixHeight, int matrixWidth, int[][] matrix) {
        this.name = name;
        this.type = type;
        this.ledsMin = ledsMin;
        this.ledsMax = ledsMax;
        this.ledsCount = ledsCount;
        this.matrixHeight = matrixHeight;
        this.matrixWidth = matrixWidth;
        this.matrix = matrix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZoneType getType() {
        return type;
    }

    public void setType(ZoneType type) {
        this.type = type;
    }

    public int getLedsMin() {
        return ledsMin;
    }

    public void setLedsMin(int ledsMin) {
        this.ledsMin = ledsMin;
    }

    public int getLedsMax() {
        return ledsMax;
    }

    public void setLedsMax(int ledsMax) {
        this.ledsMax = ledsMax;
    }

    public int getLedsCount() {
        return ledsCount;
    }

    public void setLedsCount(int ledsCount) {
        this.ledsCount = ledsCount;
    }

    @Override
    public String toString() {
        return "OpenRGBZone{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", ledsMin=" + ledsMin +
                ", ledsMax=" + ledsMax +
                ", ledsCount=" + ledsCount +
                ", matrixHeight=" + matrixHeight +
                ", matrixWidth=" + matrixWidth +
                ", matrix=" + Arrays.toString(matrix) +
                "}\n";
    }
}
