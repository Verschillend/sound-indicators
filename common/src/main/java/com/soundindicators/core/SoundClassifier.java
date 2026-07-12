package com.soundindicators.core;

import java.util.Locale;
import java.util.Set;

public final class SoundClassifier {

    private SoundClassifier() {
    }

    private static final Set<String> KNOWN_PROJECTILE_PATHS = Set.of(
            "entity.arrow.shoot",
            "entity.crossbow.shoot",
            "entity.trident.throw",
            "entity.trident.riptide_1",
            "entity.trident.riptide_2",
            "entity.trident.riptide_3",
            "entity.firework_rocket.launch",
            "entity.firework_rocket.blast",
            "entity.blaze.shoot",
            "entity.ghast.shoot",
            "entity.witch.throw",
            "entity.llama.spit",
            "entity.egg.throw",
            "entity.snowball.throw",
            "entity.splash_potion.throw",
            "entity.lingering_potion.throw",
            "entity.wind_charge.throw",
            "entity.breeze_wind_charge.shoot",
            "item.trident.throw"
    );

    private static final Set<String> BLOCK_BREAK_FRAGMENTS = Set.of(
            "break"
    );

    private static final Set<String> FOOTSTEP_FRAGMENTS = Set.of(
            "step"
    );

    private static final Set<String> PROJECTILE_FRAGMENTS = Set.of(
            "shoot",
            "throw",
            "launch"
    );

    public static IndicatorType classify(String soundEventId) {
        if (soundEventId == null || soundEventId.isEmpty()) {
            return null;
        }
        String path = soundEventId;
        int colon = path.indexOf(':');
        if (colon >= 0) {
            path = path.substring(colon + 1);
        }
        path = path.toLowerCase(Locale.ROOT);

        if (KNOWN_PROJECTILE_PATHS.contains(path)) {
            return IndicatorType.PROJECTILE;
        }
        for (String fragment : BLOCK_BREAK_FRAGMENTS) {
            if (path.contains(fragment)) {
                return IndicatorType.BLOCK_BREAK;
            }
        }
        for (String fragment : FOOTSTEP_FRAGMENTS) {
            if (path.contains(fragment)) {
                return IndicatorType.FOOTSTEP;
            }
        }
        for (String fragment : PROJECTILE_FRAGMENTS) {
            if (path.contains(fragment)) {
                return IndicatorType.PROJECTILE;
            }
        }
        return null;
    }
}
