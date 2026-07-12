package com.soundindicators.config;

import com.soundindicators.client.SoundIndicatorsClient;
import com.soundindicators.core.SoundIndicatorsConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SoundIndicatorsConfigScreen {

    private SoundIndicatorsConfigScreen() {
    }

    public static Screen create(Screen parent) {
        SoundIndicatorsConfig config = SoundIndicatorsClient.getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("soundindicators.config.title"));

        builder.setSavingRunnable(SoundIndicatorsClient::saveConfig);

        ConfigEntryBuilder entry = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("soundindicators.config.category.general"));

        general.addEntry(entry.startBooleanToggle(Component.translatable("soundindicators.config.enabled"), config.enabled)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("soundindicators.config.enabled.tooltip"))
                .setSaveConsumer(value -> config.enabled = value)
                .build());

        general.addEntry(entry.startBooleanToggle(Component.translatable("soundindicators.config.showFootsteps"), config.showFootsteps)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("soundindicators.config.showFootsteps.tooltip"))
                .setSaveConsumer(value -> config.showFootsteps = value)
                .build());

        general.addEntry(entry.startBooleanToggle(Component.translatable("soundindicators.config.showProjectiles"), config.showProjectiles)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("soundindicators.config.showProjectiles.tooltip"))
                .setSaveConsumer(value -> config.showProjectiles = value)
                .build());

        general.addEntry(entry.startBooleanToggle(Component.translatable("soundindicators.config.showBlockBreaks"), config.showBlockBreaks)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("soundindicators.config.showBlockBreaks.tooltip"))
                .setSaveConsumer(value -> config.showBlockBreaks = value)
                .build());

        ConfigCategory appearance = builder.getOrCreateCategory(Component.translatable("soundindicators.config.category.appearance"));

        appearance.addEntry(entry.startIntSlider(Component.translatable("soundindicators.config.iconSize"), config.iconSize, 4, 64)
                .setDefaultValue(16)
                .setTooltip(Component.translatable("soundindicators.config.iconSize.tooltip"))
                .setSaveConsumer(value -> config.iconSize = value)
                .build());

        appearance.addEntry(entry.startIntSlider(Component.translatable("soundindicators.config.circleDistance"), config.circleDistance, 10, 200)
                .setDefaultValue(45)
                .setTooltip(Component.translatable("soundindicators.config.circleDistance.tooltip"))
                .setSaveConsumer(value -> config.circleDistance = value)
                .build());

        appearance.addEntry(entry.startIntSlider(Component.translatable("soundindicators.config.circleOpacity"), config.circleOpacityPercent, 0, 100)
                .setDefaultValue(55)
                .setTooltip(Component.translatable("soundindicators.config.circleOpacity.tooltip"))
                .setSaveConsumer(value -> config.circleOpacityPercent = value)
                .build());

        appearance.addEntry(entry.startStrField(Component.translatable("soundindicators.config.circleColor"), config.circleColorHex)
                .setDefaultValue("#FFA500")
                .setTooltip(Component.translatable("soundindicators.config.circleColor.tooltip"))
                .setErrorSupplier(value -> isValidHexColor(value) ? java.util.Optional.empty()
                        : java.util.Optional.of(Component.translatable("soundindicators.config.circleColor.invalid")))
                .setSaveConsumer(value -> config.circleColorHex = value)
                .build());

        appearance.addEntry(entry.startIntSlider(Component.translatable("soundindicators.config.arcWidth"), config.arcWidthDegrees, 5, 90)
                .setDefaultValue(18)
                .setTooltip(Component.translatable("soundindicators.config.arcWidth.tooltip"))
                .setSaveConsumer(value -> config.arcWidthDegrees = value)
                .build());

        ConfigCategory behavior = builder.getOrCreateCategory(Component.translatable("soundindicators.config.category.behavior"));

        behavior.addEntry(entry.startIntField(Component.translatable("soundindicators.config.displayDuration"), config.displayDurationMs)
                .setDefaultValue(1500)
                .setMin(100).setMax(10000)
                .setTooltip(Component.translatable("soundindicators.config.displayDuration.tooltip"))
                .setSaveConsumer(value -> config.displayDurationMs = value)
                .build());

        behavior.addEntry(entry.startBooleanToggle(Component.translatable("soundindicators.config.fadeOut"), config.fadeOut)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("soundindicators.config.fadeOut.tooltip"))
                .setSaveConsumer(value -> config.fadeOut = value)
                .build());

        return builder.build();
    }

    private static boolean isValidHexColor(String value) {
        if (value == null) return false;
        String v = value.startsWith("#") ? value.substring(1) : value;
        return v.matches("[0-9a-fA-F]{6}") || v.matches("[0-9a-fA-F]{8}");
    }
}
