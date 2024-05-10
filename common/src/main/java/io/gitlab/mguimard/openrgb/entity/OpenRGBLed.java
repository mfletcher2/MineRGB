package io.gitlab.mguimard.openrgb.entity;

/**
 * Represents a LED in OpenRGB server
 */
public class OpenRGBLed {

    private String name;
    private int value;

    /**
     * Default constructor, returns an instance of an OpenRGBLed
     *
     * @param name  Led name
     * @param value Led value, internal
     */
    public OpenRGBLed(String name, int value) {
        this.name = name;
        this.value = value;
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

    @Override
    public String toString() {
        return "OpenRGBLed{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
