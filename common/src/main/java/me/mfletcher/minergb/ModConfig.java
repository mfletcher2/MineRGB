package me.mfletcher.minergb;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = MineRGB.MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    List<String> disabledDevices = new ArrayList<>();

    boolean damageFlash = true;

    boolean xpFlash = true;
}
