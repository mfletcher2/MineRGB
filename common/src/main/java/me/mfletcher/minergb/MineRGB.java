package me.mfletcher.minergb;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import io.gitlab.mguimard.openrgb.entity.OpenRGBColor;
import net.minecraft.client.Minecraft;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;


public final class MineRGB {
    public static final String MOD_ID = "minergb";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        // Write common init code here.
        ClientLifecycleEvent.CLIENT_STARTED.register(instance -> MineRGBClientController.init());

        ClientLifecycleEvent.CLIENT_STOPPING.register(instance -> MineRGBClientController.terminate());

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register((player) -> {
            MineRGBClientController.setHotbarSlot(player.getInventory().selected);
            MineRGBClientController.setHealth(player.getHealth(), player.getMaxHealth());
        });

        ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.register((oldPlayer, newPlayer) -> {
            MineRGBClientController.setHotbarSlot(newPlayer.getInventory().selected);
            MineRGBClientController.setHealth(newPlayer.getHealth(), newPlayer.getMaxHealth());
        });

        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register((player) -> MineRGBClientController.clearLEDs());

        ClientTickEvent.CLIENT_LEVEL_POST.register(instance -> {
            Player player = Minecraft.getInstance().player;
            if (player != null && player.tickCount % 2 == 0 && player.isAlive()) {
                new Thread(() -> {
                    MineRGBClientController.setHealth(player.getHealth(), player.getMaxHealth());
                    MineRGBClientController.setFoodLevel(player.getFoodData().getFoodLevel());
                    MineRGBClientController.setHotbarSlot(player.getInventory().selected);
                    if(instance.dimensionTypeRegistration().is(BuiltinDimensionTypes.NETHER.location()) || instance.dimensionTypeRegistration().is(BuiltinDimensionTypes.END.location())) {
                        int color = instance.getBiome(player.blockPosition()).value().getFogColor();
                        MineRGBClientController.setBackgroundColorFromInt(color);
                        MineRGBClientController.updateLeds();
                    } else {
                        if(Minecraft.getInstance().player.isUnderWater()) {
                            int color = instance.getBiome(player.blockPosition()).value().getWaterColor();
                            MineRGBClientController.setBackgroundColorFromInt(color);
                            MineRGBClientController.updateLeds();
                        } else {
                            Vec3 color = instance.getSkyColor(player.getPosition(0), 0);
                            OpenRGBColor rgbColor = new OpenRGBColor((int) (color.x * 127), (int) (color.y * 127), (int) (color.z * 127));
                            MineRGBClientController.setBackgroundColor(rgbColor);
                            MineRGBClientController.updateLeds();
                        }
                    }
                }).start();
            } else if(player != null && !player.isAlive()) {
                new Thread(() -> {
                    MineRGBClientController.setHurt();
                    MineRGBClientController.updateLeds();
                }).start();
            }
        });

    }
}
