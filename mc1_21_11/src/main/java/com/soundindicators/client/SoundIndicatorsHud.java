package com.soundindicators.client;

import com.soundindicators.core.AngleUtil;
import com.soundindicators.core.IndicatorEntry;
import com.soundindicators.core.IndicatorTracker;
import com.soundindicators.core.IndicatorType;
import com.soundindicators.core.SoundIndicatorsConfig;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;


public class SoundIndicatorsHud implements HudElement {

    private static final Identifier FOOTSTEP_TEXTURE = Identifier.of(SoundIndicatorsClient.MOD_ID, "textures/gui/footstep.png");
    private static final Identifier PROJECTILE_TEXTURE = Identifier.of(SoundIndicatorsClient.MOD_ID, "textures/gui/projectile.png");
    private static final Identifier BLOCK_BREAK_TEXTURE = Identifier.of(SoundIndicatorsClient.MOD_ID, "textures/gui/block_break.png");

    private final Supplier<SoundIndicatorsConfig> configSupplier;
    private final Supplier<IndicatorTracker> trackerSupplier;

    public SoundIndicatorsHud(Supplier<SoundIndicatorsConfig> configSupplier, Supplier<IndicatorTracker> trackerSupplier) {
        this.configSupplier = configSupplier;
        this.trackerSupplier = trackerSupplier;
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        SoundIndicatorsConfig config = configSupplier.get();
        IndicatorTracker tracker = trackerSupplier.get();
        if (config == null || tracker == null || !config.enabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) {
            return;
        }

        int centerX = client.getWindow().getScaledWidth() / 2;
        int centerY = client.getWindow().getScaledHeight() / 2;

        long now = System.currentTimeMillis();
        int ringColor = config.getRingColorArgb();

        for (IndicatorEntry entry : tracker.getActiveEntries()) {
            if (entry.type == IndicatorType.FOOTSTEP && !config.showFootsteps) continue;
            if (entry.type == IndicatorType.PROJECTILE && !config.showProjectiles) continue;
            if (entry.type == IndicatorType.BLOCK_BREAK && !config.showBlockBreaks) continue;

            float fade = config.fadeOut ? entry.fadeAlpha(now, config.displayDurationMs) : 1.0f;
            if (fade <= 0f) continue;

            int colorWithFade = applyAlphaMultiplier(ringColor, fade);

            drawArc(context, centerX, centerY, config.circleDistance, entry.angleDegrees, config.arcWidthDegrees, colorWithFade);
            drawIcon(context, centerX, centerY, config.circleDistance, entry.angleDegrees, entry.type, config.iconSize, fade);
        }
    }

    private void drawArc(DrawContext context, int centerX, int centerY, int radius, double centerAngle, double widthDegrees, int argb) {
        int segments = Math.max(4, (int) (widthDegrees / 3.0));
        double startAngle = centerAngle - widthDegrees / 2.0;
        double step = widthDegrees / segments;
        int dotSize = Math.max(2, radius / 12);

        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + step * i;
            double[] offset = AngleUtil.toScreenOffset(angle, radius);
            int x = centerX + (int) Math.round(offset[0]);
            int y = centerY + (int) Math.round(offset[1]);
            context.fill(x - dotSize / 2, y - dotSize / 2, x + dotSize / 2, y + dotSize / 2, argb);
        }
    }

    private void drawIcon(DrawContext context, int centerX, int centerY, int radius, double angle, IndicatorType type, int iconSize, float alpha) {
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

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(cx, cy);
        context.getMatrices().rotate((float) Math.toRadians(angle));
        context.getMatrices().translate(-cx, -cy);

        int tint = (Math.round(255 * alpha) << 24) | 0xFFFFFF; // white tint, alpha carries the fade
        context.drawTexture(net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED,
                texture, x, y, 0f, 0f, iconSize, iconSize, iconSize, iconSize, tint);

        context.getMatrices().popMatrix();
    }

    private static int applyAlphaMultiplier(int argb, float multiplier) {
        int a = (argb >>> 24) & 0xFF;
        int rgb = argb & 0xFFFFFF;
        int newA = Math.round(a * multiplier);
        return (newA << 24) | rgb;
    }
}
