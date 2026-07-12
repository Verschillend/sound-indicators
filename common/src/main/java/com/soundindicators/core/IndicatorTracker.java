package com.soundindicators.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class IndicatorTracker {

    private final CopyOnWriteArrayList<IndicatorEntry> entries = new CopyOnWriteArrayList<>();

    private volatile long displayDurationMillis;

    private static final int MAX_ENTRIES = 128;

    public IndicatorTracker(long displayDurationMillis) {
        this.displayDurationMillis = displayDurationMillis;
    }

    public void setDisplayDurationMillis(long displayDurationMillis) {
        this.displayDurationMillis = displayDurationMillis;
    }

    public void addIndicator(IndicatorType type, double angleDegrees) {
        if (entries.size() >= MAX_ENTRIES) {
            entries.remove(0);
        }
        entries.add(new IndicatorEntry(type, angleDegrees, System.currentTimeMillis()));
    }

    public List<IndicatorEntry> getActiveEntries() {
        long now = System.currentTimeMillis();
        long duration = displayDurationMillis;
        List<IndicatorEntry> active = new ArrayList<>(entries.size());
        for (IndicatorEntry entry : entries) {
            if (entry.ageMillis(now) <= duration) {
                active.add(entry);
            }
        }
        if (active.size() != entries.size()) {
            entries.removeIf(e -> e.ageMillis(now) > duration);
        }
        return Collections.unmodifiableList(active);
    }

    public void clear() {
        entries.clear();
    }
}
