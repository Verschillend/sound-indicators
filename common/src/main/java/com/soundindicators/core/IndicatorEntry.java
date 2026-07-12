package com.soundindicators.core;

public final class IndicatorEntry {

    public final IndicatorType type;
    public final double angleDegrees;
    public final long spawnTimeMillis;

    public IndicatorEntry(IndicatorType type, double angleDegrees, long spawnTimeMillis) {
        this.type = type;
        this.angleDegrees = angleDegrees;
        this.spawnTimeMillis = spawnTimeMillis;
    }

    public long ageMillis(long now) {
        return now - spawnTimeMillis;
    }

    public float fadeAlpha(long now, long displayDurationMillis) {
        if (displayDurationMillis <= 0) {
            return 1.0f;
        }
        double age = ageMillis(now);
        float alpha = 1.0f - (float) (age / (double) displayDurationMillis);
        if (alpha < 0f) return 0f;
        if (alpha > 1f) return 1f;
        return alpha;
    }
}
