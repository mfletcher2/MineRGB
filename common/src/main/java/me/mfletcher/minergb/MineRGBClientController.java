package me.mfletcher.minergb;

import com.mojang.logging.LogUtils;
import io.gitlab.mguimard.openrgb.client.OpenRGBClient;
import io.gitlab.mguimard.openrgb.entity.DeviceType;
import io.gitlab.mguimard.openrgb.entity.OpenRGBColor;
import io.gitlab.mguimard.openrgb.entity.OpenRGBDevice;
import io.gitlab.mguimard.openrgb.entity.OpenRGBLed;
import me.mfletcher.minergb.util.BitUtils;
import me.shedaniel.autoconfig.AutoConfig;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;

public class MineRGBClientController {
    private static final List<Map<String, Integer>> KEY_MAP = new ArrayList<>();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<Integer> INITIAL_MODES = new ArrayList<>();
    private static final List<OpenRGBColor[]> BACKGROUND_COLOR_CACHE = new ArrayList<>();
    private static final List<OpenRGBColor[]> HURT_COLOR_CACHE = new ArrayList<>();
    private static final List<OpenRGBColor[]> XP_COLOR_CACHE = new ArrayList<>();
    private static final List<OpenRGBDevice> DEVICES_CACHE = new ArrayList<>();

    private static ModConfig config;
    private static OpenRGBClient client;
    private static int hotbarSlot = 0;
    private static double healthRatio = 1.0;
    private static double foodLevelRatio = 1.0;
    private static boolean isHurt = false;
    private static boolean collectedXp = false;

    public static void init() {
        try {
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
            LOGGER.info("Attempting to find OpenRGB compatible devices");

            client = new OpenRGBClient(config.openRGBHost, config.openRGBPort, "MineRGB");
            client.connect();

            int controllerCount = client.getControllerCount();

            for (int i = 0; i < controllerCount; i++) {
                OpenRGBDevice controller = client.getDeviceController(i);
                List<OpenRGBLed> leds = controller.getLeds();

                DEVICES_CACHE.add(controller);
                INITIAL_MODES.add(controller.getActiveMode());

                LOGGER.info("Found OpenRGB-compatible {}: {} with {} LEDs", controller.getType().getName(), controller.getName(), leds.size());
                BACKGROUND_COLOR_CACHE.add(new OpenRGBColor[leds.size()]);
                HURT_COLOR_CACHE.add(new OpenRGBColor[leds.size()]);
                XP_COLOR_CACHE.add(new OpenRGBColor[leds.size()]);

                Arrays.fill(HURT_COLOR_CACHE.get(i), new OpenRGBColor(127, 0, 0));
                Arrays.fill(XP_COLOR_CACHE.get(i), OpenRGBColor.fromHexaString("#9ad26c"));

                updateMode(i, 0);

                OpenRGBColor[] openRGBColors = new OpenRGBColor[leds.size()];
                Arrays.fill(openRGBColors, new OpenRGBColor(0, 0, 0));

                if (controller.getType() == DeviceType.DEVICE_TYPE_KEYBOARD && leds.getFirst().getName().startsWith("Key: ")) {
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

                updateLeds(i, openRGBColors);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void terminate() {
        for (int i = 0; i < DEVICES_CACHE.size(); i++)
            updateMode(i, INITIAL_MODES.get(i));
//          CLIENT.disconnect();
    }

    public static void updateLeds() {
        for (int i = 0; i < DEVICES_CACHE.size(); i++) {
            OpenRGBColor[] colors;
            if (isHurt && config.damageFlash) colors = HURT_COLOR_CACHE.get(i).clone();
            else if (collectedXp && config.xpFlash) colors = XP_COLOR_CACHE.get(i).clone();
            else colors = BACKGROUND_COLOR_CACHE.get(i).clone();
            if (KEY_MAP.get(i) != null) {
                setKeyColor(Integer.toString(hotbarSlot + 1), KEY_MAP.get(i), new OpenRGBColor(127, 127, 127), colors);
                setHealthColors(KEY_MAP.get(i), colors);
                setFoodColors(KEY_MAP.get(i), colors);
            }
            updateLeds(i, colors);
        }
        isHurt = false;
        collectedXp = false;
    }

    public static void updateLeds(int deviceIndex, OpenRGBColor[] colors) {
        try {
            String deviceName = DEVICES_CACHE.get(deviceIndex).getName();
            if (!config.disabledDevices.contains(deviceName.substring(0, deviceName.length() - 1)))
                client.updateLeds(deviceIndex, colors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateLoading(int progress /* out of 100 */) {
        for (int i = 0; i < DEVICES_CACHE.size(); i++) {
            OpenRGBDevice device = DEVICES_CACHE.get(i);
            if (device.getZones().getFirst().getMatrixWidth() == 0) continue;
            int[][] matrix = device.getZones().getFirst().getMatrix();
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
        Vector3i rgb = BitUtils.getRGB(color);
        OpenRGBColor rgbColor = new OpenRGBColor(rgb.x / 2, rgb.y / 2, rgb.z / 2);
        setBackgroundColor(rgbColor);
    }

    public static void clearLEDs() {
        for (int i = 0; i < DEVICES_CACHE.size(); i++) {
            OpenRGBColor[] colors = new OpenRGBColor[DEVICES_CACHE.get(i).getLeds().size()];
            Arrays.fill(colors, new OpenRGBColor(0, 0, 0));
            updateLeds(i, colors);
        }
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

    private static void updateMode(int deviceIndex, int modeIndex) {
        try {
            String deviceName = DEVICES_CACHE.get(deviceIndex).getName();
            if (!config.disabledDevices.contains(deviceName.substring(0, deviceName.length() - 1)))
                client.updateMode(deviceIndex, modeIndex, DEVICES_CACHE.get(deviceIndex).getModes().get(modeIndex));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
