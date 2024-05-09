package me.mfletcher.minergb;

import com.mojang.logging.LogUtils;
import io.gitlab.mguimard.openrgb.client.OpenRGBClient;
import io.gitlab.mguimard.openrgb.entity.DeviceType;
import io.gitlab.mguimard.openrgb.entity.OpenRGBColor;
import io.gitlab.mguimard.openrgb.entity.OpenRGBDevice;
import io.gitlab.mguimard.openrgb.entity.OpenRGBLed;
import me.mfletcher.minergb.util.BitUtils;
import net.minecraft.core.Vec3i;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;

public class MineRGBClientController {
    private static final OpenRGBClient CLIENT = new OpenRGBClient("127.0.0.1", 6742, "MineRGB");
    private static final List<Map<String, Integer>> KEY_MAP = new ArrayList<>();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<OpenRGBColor[]> BACKGROUND_COLOR_CACHE = new ArrayList<>();
    private static final List<OpenRGBColor[]> HURT_COLOR_CACHE = new ArrayList<>();
    private static final List<OpenRGBColor[]> XP_COLOR_CACHE = new ArrayList<>();
    private static final List<OpenRGBDevice> DEVICES_CACHE = new ArrayList<>();

    private static int hotbarSlot = 0;
    private static double healthRatio = 1.0;
    private static double foodLevelRatio = 1.0;
    private static boolean isHurt = false;
    private static boolean collectedXp = false;

    public static void init() {
        try {
            LOGGER.info("Attempting to find OpenRGB compatible devices");

            CLIENT.connect();

            int controllerCount = CLIENT.getControllerCount();

            for (int i = 0; i < controllerCount; i++) {
                OpenRGBDevice controller = CLIENT.getDeviceController(i);
                DEVICES_CACHE.add(controller);

                LOGGER.info("Found OpenRGB-compatible {}: {}", controller.getType().getName(), controller.getName());
                List<OpenRGBLed> leds = controller.getLeds();
                LOGGER.info("Found {} LEDs on this device", leds.size());
                BACKGROUND_COLOR_CACHE.add(new OpenRGBColor[leds.size()]);
                HURT_COLOR_CACHE.add(new OpenRGBColor[leds.size()]);
                XP_COLOR_CACHE.add(new OpenRGBColor[leds.size()]);

                Arrays.fill(HURT_COLOR_CACHE.get(i), new OpenRGBColor(127, 0, 0));
                Arrays.fill(XP_COLOR_CACHE.get(i), OpenRGBColor.fromHexaString("#9ad26c"));

                CLIENT.updateMode(i, 0, controller.getModes().get(0));

                OpenRGBColor[] openRGBColors = new OpenRGBColor[leds.size()];
                Arrays.fill(openRGBColors, new OpenRGBColor(0, 0, 0));

                if (controller.getType() == DeviceType.DEVICE_TYPE_KEYBOARD) {
                    Map<String, Integer> keyMap = new HashMap<>();
                    for (int j = 0; j < leds.size(); j++) {
                        String name = leds.get(j).getName();
                        name = name.substring(5, name.length() - 1);
                        keyMap.put(name, j);
                    }
                    KEY_MAP.add(keyMap);
                    setKeyColor("M", keyMap, new OpenRGBColor(0, 255, 0), openRGBColors);
                    setKeyColor("C", keyMap, new OpenRGBColor(0, 255, 0), openRGBColors);
                } else
                    KEY_MAP.add(null);

                CLIENT.updateLeds(i, openRGBColors);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateLeds() {
        for (int i = 0; i < DEVICES_CACHE.size(); i++) {
            OpenRGBDevice device = DEVICES_CACHE.get(i);
            OpenRGBColor[] colors;
            if (isHurt) colors = HURT_COLOR_CACHE.get(i).clone();
            else if (collectedXp) colors = XP_COLOR_CACHE.get(i).clone();
            else colors = BACKGROUND_COLOR_CACHE.get(i).clone();
            isHurt = false;
            collectedXp = false;
            if (device.getType() == DeviceType.DEVICE_TYPE_KEYBOARD) {
                setKeyColor(Integer.toString(hotbarSlot + 1), KEY_MAP.get(i), new OpenRGBColor(127, 127, 127), colors);
                setHealthColors(KEY_MAP.get(i), colors);
                setFoodColors(KEY_MAP.get(i), colors);
            }
            updateLeds(i, colors);
        }
    }

    public static void updateLeds(int deviceIndex, OpenRGBColor[] colors) {
        try {
            CLIENT.updateLeds(deviceIndex, colors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateLoading(int progress /* out of 100 */) {
        for (int i = 0; i < DEVICES_CACHE.size(); i++) {
            OpenRGBDevice device = DEVICES_CACHE.get(i);
            if (device.getZones().get(0).getMatrixWidth() == 0) return;
            int[][] matrix = device.getZones().get(0).getMatrix();
            OpenRGBColor[] colors = new OpenRGBColor[device.getLeds().size()];
            progress = (int) Math.round(progress / 100.0 * matrix[0].length);
            for (int j = 0; j < matrix[0].length; j++) {
                if (j < progress) {
                    for (int k = 0; k < matrix.length; k++)
                        if (matrix[k][j] != -1)
                            colors[matrix[k][j]] = new OpenRGBColor(0, 255, 0);

                } else {
                    for (int k = 0; k < matrix.length; k++)
                        if (matrix[k][j] != -1)
                            colors[matrix[k][j]] = new OpenRGBColor(0, 0, 0);
                }
            }

            updateLeds(i, colors);
        }
    }

    public static void setBackgroundColor(OpenRGBColor color) {
        for (int i = 0; i < DEVICES_CACHE.size(); i++) {
            Arrays.fill(BACKGROUND_COLOR_CACHE.get(i), color);
        }
    }

    public static void setBackgroundColorFromInt(int color) {
        Vec3i rgb = BitUtils.getRGB(color);
        OpenRGBColor rgbColor = new OpenRGBColor(rgb.getX() / 2, rgb.getY() / 2, rgb.getZ() / 2);
        setBackgroundColor(rgbColor);
    }

    public static void setHotbarSlot(int slot) {
        hotbarSlot = slot;
    }

    public static void setHealth(double health, double max) {
        healthRatio = health / max;
    }

    public static void setFoodLevel(int foodLevel) {
        foodLevelRatio = foodLevel / 20.0;
    }

    public static void setHurt() {
        isHurt = true;
    }

    public static void setXp() {
        collectedXp = true;
    }

    private static void setKeyColor(String key, @Nullable Map<String, Integer> keyMap, OpenRGBColor color, OpenRGBColor[] colorArr) {
        if (keyMap == null) {
            LOGGER.warn("Attempted to set key color on a non-keyboard device");
            return;
        }

        if (!keyMap.containsKey(key)) {
            LOGGER.warn("Attempted to set color on a key that does not exist on this device: {}", key);
            return;
        }

        colorArr[keyMap.get(key)] = color;
    }

    private static void setHealthColors(Map<String, Integer> keyMap, OpenRGBColor[] colorArr) {
        if (keyMap == null) {
            LOGGER.warn("Attempted to set health colors on a non-keyboard device");
            return;
        }

        int health = (int) Math.round(healthRatio * 6);
        for (int i = 1; i <= 6; i++)
            if (health >= i)
                setKeyColor("F" + i, keyMap, new OpenRGBColor(127, 0, 0), colorArr);
            else
                setKeyColor("F" + i, keyMap, new OpenRGBColor(0, 0, 0), colorArr);
    }

    private static void setFoodColors(Map<String, Integer> keyMap, OpenRGBColor[] colorArr) {
        if (keyMap == null) {
            LOGGER.warn("Attempted to set food colors on a non-keyboard device");
            return;
        }

        int food = (int) Math.round(foodLevelRatio * 6);
        for (int i = 1; i <= 6; i++)
            if (food >= i)
                setKeyColor("F" + (13 - i), keyMap, OpenRGBColor.fromHexaString("#75481b"), colorArr);
            else
                setKeyColor("F" + (13 - i), keyMap, new OpenRGBColor(0, 0, 0), colorArr);
    }
}
