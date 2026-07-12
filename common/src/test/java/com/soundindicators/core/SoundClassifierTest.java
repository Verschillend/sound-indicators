package com.soundindicators.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SoundClassifierTest {

    @Test
    void arrowShoot_isProjectile() {
        assertEquals(IndicatorType.PROJECTILE, SoundClassifier.classify("minecraft:entity.arrow.shoot"));
    }

    @Test
    void crossbowShoot_isProjectile() {
        assertEquals(IndicatorType.PROJECTILE, SoundClassifier.classify("minecraft:entity.crossbow.shoot"));
    }

    @Test
    void tridentThrow_isProjectile() {
        assertEquals(IndicatorType.PROJECTILE, SoundClassifier.classify("minecraft:item.trident.throw"));
    }

    @Test
    void grassStep_isFootstep() {
        assertEquals(IndicatorType.FOOTSTEP, SoundClassifier.classify("minecraft:block.grass.step"));
    }

    @Test
    void stoneStep_isFootstep() {
        assertEquals(IndicatorType.FOOTSTEP, SoundClassifier.classify("minecraft:block.stone.step"));
    }

    @Test
    void mobFootstep_isFootstep() {
        assertEquals(IndicatorType.FOOTSTEP, SoundClassifier.classify("minecraft:entity.strider.step"));
    }

    @Test
    void moddedGunShoot_fallsBackToProjectileHeuristic() {
        assertEquals(IndicatorType.PROJECTILE, SoundClassifier.classify("mymod:item.custom_gun.shoot"));
    }

    @Test
    void unrelatedSound_isIgnored() {
        assertNull(SoundClassifier.classify("minecraft:ambient.cave"));
    }

    @Test
    void nullInput_isIgnored() {
        assertNull(SoundClassifier.classify(null));
    }

    @Test
    void pathWithoutNamespace_stillWorks() {
        assertEquals(IndicatorType.FOOTSTEP, SoundClassifier.classify("block.grass.step"));
    }
}
