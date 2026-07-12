package com.soundindicators.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndicatorTrackerTest {

    @Test
    void newlyAddedIndicators_areActive() {
        IndicatorTracker tracker = new IndicatorTracker(1000);
        tracker.addIndicator(IndicatorType.FOOTSTEP, 90);
        tracker.addIndicator(IndicatorType.PROJECTILE, 270);

        assertEquals(2, tracker.getActiveEntries().size());
    }

    @Test
    void expiredIndicators_areRemoved() throws InterruptedException {
        IndicatorTracker tracker = new IndicatorTracker(50);
        tracker.addIndicator(IndicatorType.FOOTSTEP, 45);

        assertEquals(1, tracker.getActiveEntries().size());

        Thread.sleep(120);

        assertTrue(tracker.getActiveEntries().isEmpty());
    }

    @Test
    void clear_removesEverything() {
        IndicatorTracker tracker = new IndicatorTracker(1000);
        tracker.addIndicator(IndicatorType.FOOTSTEP, 10);
        tracker.addIndicator(IndicatorType.FOOTSTEP, 20);
        tracker.clear();

        assertTrue(tracker.getActiveEntries().isEmpty());
    }

    @Test
    void multipleIndicators_canSurroundThePlayer() {
        IndicatorTracker tracker = new IndicatorTracker(1000);
        for (int angle = 0; angle < 360; angle += 30) {
            tracker.addIndicator(angle % 60 == 0 ? IndicatorType.FOOTSTEP : IndicatorType.PROJECTILE, angle);
        }
        List<IndicatorEntry> active = tracker.getActiveEntries();
        assertEquals(12, active.size());
    }
}
