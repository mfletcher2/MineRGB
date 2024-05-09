package io.gitlab.mguimard.openrgb.entity;

/**
 * Wrapper for a RGB color used in this project
 */
public class OpenRGBColor {
    private byte red;
    private byte green;
    private byte blue;

    /**
     * Returns an instance of an OpenRGBColor from RGB components
     *
     * @param red   the red value
     * @param green the green value
     * @param blue  the blue value
     */
    public OpenRGBColor(int red, int green, int blue) {
        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;
    }

    /**
     * Returns an instance of an OpenRGBColor from a color integer value
     *
     * @param value the color integer value
     */
    public static OpenRGBColor fromInt(int value) {
        return new OpenRGBColor(value >> 16 & 255, value >> 8 & 255, value & 255);
    }

    /**
     * Returns an instance of an OpenRGBColor from a HSB color components
     *
     * @param hue        the HSB color hue (0-360)
     * @param saturation the HSB color saturation (0-1)
     * @param brightness the HSB color brightness/value (0-1)
     * @return an OpenRGBColor instance
     */
    public static OpenRGBColor fromHSB(float hue, float saturation, float brightness) {
        hue /= 360;
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 4:
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 5:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                    break;
            }
        }
        return OpenRGBColor.fromInt(0xff000000 | (r << 16) | (g << 8) | (b << 0));
    }

    /**
     * Returns an instance of an OpenRGBColor from a HSB color components
     *
     * @param value the hexadecimal string value
     * @return
     */
    public static OpenRGBColor fromHexaString(String value) {
        try {
            return fromInt(Integer.decode(value));
        } catch (Exception e) {
            return new OpenRGBColor(0xFF, 0xFF, 0xFF);
        }
    }

    /**
     * Gets the color integer value
     *
     * @return the color integer value
     */
    public int getIntValue() {
        return ((red & 0x0ff) << 16) | ((green & 0x0ff) << 8) | (blue & 0x0ff) | 0xFF000000;
    }

    public String getHexaString() {
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    public byte getRed() {
        return red;
    }

    public void setRed(byte red) {
        this.red = red;
    }

    public byte getGreen() {
        return green;
    }

    public void setGreen(byte green) {
        this.green = green;
    }

    public byte getBlue() {
        return blue;
    }

    public void setBlue(byte blue) {
        this.blue = blue;
    }

    @Override
    public String toString() {
        return "OpenRGBColor{" + getHexaString() + '}';
    }
}
