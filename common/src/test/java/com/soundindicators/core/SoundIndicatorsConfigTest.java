package com.soundindicators.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SoundIndicatorsConfigTest {

    @Test
    void fullOpacityOrange_parsesToOpaqueOrange() {
        SoundIndicatorsConfig cfg = new SoundIndicatorsConfig();
        cfg.circleColorHex = "#FFA500";
        cfg.circleOpacityPercent = 100;
        assertEquals(0xFFFFA500, cfg.getRingColorArgb());
    }

    @Test
    void zeroOpacity_parsesToTransparent() {
        SoundIndicatorsConfig cfg = new SoundIndicatorsConfig();
        cfg.circleColorHex = "#FFA500";
        cfg.circleOpacityPercent = 0;
        assertEquals(0x00FFA500, cfg.getRingColorArgb());
    }

    @Test
    void hexWithoutHashPrefix_isAccepted() {
        SoundIndicatorsConfig cfg = new SoundIndicatorsConfig();
        cfg.circleColorHex = "00FF00";
        cfg.circleOpacityPercent = 100;
        assertEquals(0xFF00FF00, cfg.getRingColorArgb());
    }

    @Test
    void malformedHex_fallsBackToDefaultOrange() {
        SoundIndicatorsConfig cfg = new SoundIndicatorsConfig();
        cfg.circleColorHex = "not-a-color";
        cfg.circleOpacityPercent = 100;
        assertEquals(0xFFFFA500, cfg.getRingColorArgb());
    }

    @Test
    void saveThenLoad_roundTripsAllFields(@TempDir Path tempDir) {
        Path file = tempDir.resolve("soundindicators.json");

        SoundIndicatorsConfig original = new SoundIndicatorsConfig();
        original.enabled = false;
        original.showFootsteps = false;
        original.iconSize = 24;
        original.circleDistance = 60;
        original.circleOpacityPercent = 80;
        original.circleColorHex = "#00FFFF";
        original.save(file);

        assertTrue(Files.exists(file));

        SoundIndicatorsConfig loaded = SoundIndicatorsConfig.load(file);
        assertEquals(original.enabled, loaded.enabled);
        assertEquals(original.showFootsteps, loaded.showFootsteps);
        assertEquals(original.iconSize, loaded.iconSize);
        assertEquals(original.circleDistance, loaded.circleDistance);
        assertEquals(original.circleOpacityPercent, loaded.circleOpacityPercent);
        assertEquals(original.circleColorHex, loaded.circleColorHex);
    }

    @Test
    void loadingNonexistentFile_returnsDefaults() {
        SoundIndicatorsConfig loaded = SoundIndicatorsConfig.load(Path.of("/nonexistent/path/soundindicators.json"));
        assertTrue(loaded.enabled);
        assertEquals(16, loaded.iconSize);
    }
}
