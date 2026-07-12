package com.soundindicators.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class SoundIndicatorsConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public boolean enabled = true;

    public boolean showFootsteps = true;
    public boolean showProjectiles = true;
    public boolean showBlockBreaks = true;

    public int iconSize = 16;
    public int circleDistance = 45;
    public int circleOpacityPercent = 55;
    public String circleColorHex = "#FFA500";

    public int displayDurationMs = 1500;
    public boolean fadeOut = true;
    public int arcWidthDegrees = 18;

    public static SoundIndicatorsConfig load(Path path) {
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                SoundIndicatorsConfig loaded = GSON.fromJson(reader, SoundIndicatorsConfig.class);
                if (loaded != null) {
                    return loaded;
                }
            } catch (IOException | RuntimeException ignored) {
            }
        }
        return new SoundIndicatorsConfig();
    }

    public void save(Path path) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save Sound Indicators config to " + path, e);
        }
    }

    public int getRingColorArgb() {
        String hex = circleColorHex == null ? "" : circleColorHex.trim();
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        int rgb;
        try {
            rgb = hex.length() >= 6 ? (int) Long.parseLong(hex.substring(0, 6), 16) : 0xFFA500;
        } catch (NumberFormatException e) {
            rgb = 0xFFA500;
        }
        int alpha = Math.round(255f * Math.max(0, Math.min(100, circleOpacityPercent)) / 100f);
        return (alpha << 24) | (rgb & 0xFFFFFF);
    }
}
