package com.soundindicators.client;

import com.soundindicators.core.AngleUtil;
import com.soundindicators.core.IndicatorEntry;
import com.soundindicators.core.IndicatorTracker;
import com.soundindicators.core.IndicatorType;
import com.soundindicators.core.SoundIndicatorsConfig;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class SoundIndicatorsHud implements HudElement {

    private static final Identifier FOOTSTEP_TEXTURE =
            Identifier.fromNamespaceAndPath(SoundIndicatorsClient.MOD_ID, "textures/gui/footstep.png");
    private static final Identifier PROJECTILE_TEXTURE =
            Identifier.fromNamespaceAndPath(SoundIndicatorsClient.MOD_ID, "textures/gui/projectile.png");
    private static final Identifier BLOCK_BREAK_TEXTURE =
            Identifier.fromNamespaceAndPath(SoundIndicatorsClient.MOD_ID, "textures/gui/block_break.png");

    private final Supplier<SoundIndicatorsConfig> configSupplier;
    private final Supplier<IndicatorTracker> trackerSupplier;

    public SoundIndicatorsHud(Supplier<SoundIndicatorsConfig> configSupplier, Supplier<IndicatorTracker> trackerSupplier) {
        this.configSupplier = configSupplier;
        this.trackerSupplier = trackerSupplier;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        SoundIndicatorsConfig config = configSupplier.get();
        IndicatorTracker tracker = trackerSupplier.get();
        if (config == null || tracker == null || !config.enabled) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        if (client.player == null) { //todo add back checking if the hud is hidden (f1) (i cant find the method anywhere bruh)
            return;
        }

        int centerX = client.getWindow().getGuiScaledWidth() / 2;
        int centerY = client.getWindow().getGuiScaledHeight() / 2;

        long now = System.currentTimeMillis();
        int ringColor = config.getRingColorArgb();

        for (IndicatorEntry entry : tracker.getActiveEntries()) {
            if (entry.type == IndicatorType.FOOTSTEP && !config.showFootsteps) continue;
            if (entry.type == IndicatorType.PROJECTILE && !config.showProjectiles) continue;
            if (entry.type == IndicatorType.BLOCK_BREAK && !config.showBlockBreaks) continue;

            float fade = config.fadeOut ? entry.fadeAlpha(now, config.displayDurationMs) : 1.0f;
            if (fade <= 0f) continue;

            int colorWithFade = applyAlphaMultiplier(ringColor, fade);

            drawArc(guiGraphics, centerX, centerY, config.circleDistance, entry.angleDegrees, config.arcWidthDegrees, colorWithFade);
            drawIcon(guiGraphics, centerX, centerY, config.circleDistance, entry.angleDegrees, entry.type, config.iconSize, fade);
        }
    }

    private void drawArc(GuiGraphicsExtractor guiGraphics, int centerX, int centerY, int radius, double centerAngle, double widthDegrees, int argb) {
        int segments = Math.max(4, (int) (widthDegrees / 3.0));
        double startAngle = centerAngle - widthDegrees / 2.0;
        double step = widthDegrees / segments;
        int dotSize = Math.max(2, radius / 12);

        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + step * i;
            double[] offset = AngleUtil.toScreenOffset(angle, radius);
            int x = centerX + (int) Math.round(offset[0]);
            int y = centerY + (int) Math.round(offset[1]);
            guiGraphics.fill(x - dotSize / 2, y - dotSize / 2, x + dotSize / 2, y + dotSize / 2, argb);
        }
    }

    private void drawIcon(GuiGraphicsExtractor guiGraphics, int centerX, int centerY, int radius, double angle, IndicatorType type, int iconSize, float alpha) {
        Identifier texture = switch (type) {
            case FOOTSTEP -> FOOTSTEP_TEXTURE;
            case PROJECTILE -> PROJECTILE_TEXTURE;
            case BLOCK_BREAK -> BLOCK_BREAK_TEXTURE;
        };
        double[] offset = AngleUtil.toScreenOffset(angle, radius);
        int x = centerX + (int) Math.round(offset[0]) - iconSize / 2;
        int y = centerY + (int) Math.round(offset[1]) - iconSize / 2;

        float cx = (float) (centerX + offset[0]);
        float cy = (float) (centerY + offset[1]);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(cx, cy);
        guiGraphics.pose().rotate((float) Math.toRadians(angle));
        guiGraphics.pose().translate(-cx, -cy);

        int tint = (Math.round(255 * alpha) << 24) | 0xFFFFFF;
        guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
                texture, x, y, 0f, 0f, iconSize, iconSize, iconSize, iconSize, tint);

        guiGraphics.pose().popMatrix();
    }

    private static int applyAlphaMultiplier(int argb, float multiplier) {
        int a = (argb >>> 24) & 0xFF;
        int rgb = argb & 0xFFFFFF;
        int newA = Math.round(a * multiplier);
        return (newA << 24) | rgb;
    }
}
