package com.soundindicators.client;

import com.soundindicators.core.IndicatorTracker;
import com.soundindicators.core.IndicatorType;
import com.soundindicators.core.SoundClassifier;
import com.soundindicators.core.SoundIndicatorsConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

import java.nio.file.Path;

public class SoundIndicatorsClient implements ClientModInitializer {

    public static final String MOD_ID = "soundindicators";

    private static SoundIndicatorsConfig config;
    private static IndicatorTracker tracker;
    private static Path configPath;

    @Override
    public void onInitializeClient() {
        configPath = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");
        config = SoundIndicatorsConfig.load(configPath);
        tracker = new IndicatorTracker(config.displayDurationMs);

        HudElementRegistry.addLast(Identifier.of(MOD_ID, "overlay"), new SoundIndicatorsHud(SoundIndicatorsClient::getConfig, SoundIndicatorsClient::getTracker));
    }

    public static SoundIndicatorsConfig getConfig() {
        return config;
    }

    public static IndicatorTracker getTracker() {
        return tracker;
    }

    public static void saveConfig() {
        tracker.setDisplayDurationMillis(config.displayDurationMs);
        config.save(configPath);
    }

    public static void onSoundPlayed(String soundEventId, double soundX, double soundY, double soundZ) {
        if (config == null || !config.enabled) {
            return;
        }

        IndicatorType type = SoundClassifier.classify(soundEventId);
        if (type == null) {
            return;
        }
        if (type == IndicatorType.FOOTSTEP && !config.showFootsteps) {
            return;
        }
        if (type == IndicatorType.PROJECTILE && !config.showProjectiles) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }

        double dx = soundX - player.getX();
        double dz = soundZ - player.getZ();
        if (dx * dx + dz * dz < 0.25) {
            return;
        }

        double angle = com.soundindicators.core.AngleUtil.relativeAngleDegrees(
                player.getYaw(), player.getX(), player.getZ(), soundX, soundZ);

        tracker.addIndicator(type, angle);
    }
}
