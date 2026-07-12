package com.soundindicators.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AngleUtilTest {

    private static final double DELTA = 0.001;

    @Test
    void facingSouth_soundToTheRight_isWest() {
        assertEquals(90.0, AngleUtil.relativeAngleDegrees(0, 0, 0, -10, 0), DELTA);
    }

    @Test
    void facingSouth_soundToTheLeft_isEast() {
        assertEquals(270.0, AngleUtil.relativeAngleDegrees(0, 0, 0, 10, 0), DELTA);
    }

    @Test
    void facingSouth_soundAhead_isSouth() {
        assertEquals(0.0, Math.abs(AngleUtil.relativeAngleDegrees(0, 0, 0, 0, 10)), DELTA);
    }

    @Test
    void facingSouth_soundBehind_isNorth() {
        assertEquals(180.0, AngleUtil.relativeAngleDegrees(0, 0, 0, 0, -10), DELTA);
    }

    @Test
    void facingWest_soundToTheRight_isNorth() {
        assertEquals(90.0, AngleUtil.relativeAngleDegrees(90, 0, 0, 0, -10), DELTA);
    }

    @Test
    void facingNorth_soundToTheRight_isEast() {
        assertEquals(90.0, AngleUtil.relativeAngleDegrees(180, 0, 0, 10, 0), DELTA);
    }

    @Test
    void facingEast_soundToTheRight_isSouth() {
        assertEquals(90.0, AngleUtil.relativeAngleDegrees(270, 0, 0, 0, 10), DELTA);
    }

    @Test
    void diagonalFrontRight_is45Degrees() {
        assertEquals(45.0, AngleUtil.relativeAngleDegrees(0, 0, 0, -10, 10), DELTA);
    }

    @Test
    void soundOnTopOfPlayer_defaultsToZero() {
        assertEquals(0.0, AngleUtil.relativeAngleDegrees(123, 5, 5, 5, 5), DELTA);
    }

    @Test
    void screenOffset_zeroDegrees_isStraightUp() {
        double[] offset = AngleUtil.toScreenOffset(0, 40);
        assertEquals(0.0, offset[0], DELTA);
        assertEquals(-40.0, offset[1], DELTA);
    }

    @Test
    void screenOffset_ninetyDegrees_isStraightRight() {
        double[] offset = AngleUtil.toScreenOffset(90, 40);
        assertEquals(40.0, offset[0], DELTA);
        assertEquals(0.0, offset[1], DELTA);
    }

    @Test
    void screenOffset_oneEightyDegrees_isStraightDown() {
        double[] offset = AngleUtil.toScreenOffset(180, 40);
        assertEquals(0.0, offset[0], DELTA);
        assertEquals(40.0, offset[1], DELTA);
    }
}
