package me.mfletcher.minergb;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import org.apache.http.conn.util.InetAddressUtils;

import java.util.ArrayList;
import java.util.List;

@Config(name = MineRGB.MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    List<String> disabledDevices = new ArrayList<>();

    boolean damageFlash = true;

    boolean xpFlash = true;

    String openRGBHost = "127.0.0.1";

    int openRGBPort = 6742;

    @Override
    public void validatePostLoad() throws ValidationException {
        if (!InetAddressUtils.isIPv4Address(openRGBHost) && !InetAddressUtils.isIPv6Address(openRGBHost)) {
            throw new ValidationException("Invalid IP address");
        }
    }
}
