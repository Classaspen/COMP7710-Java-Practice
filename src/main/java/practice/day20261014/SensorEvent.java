package practice.day20261014;

import java.util.Objects;

public abstract class SensorEvent implements Alertable, Comparable<SensorEvent> {
    private final String source;
    private final int priority;

    protected SensorEvent(String source, int priority) {
        if (source == null || source.isBlank()) throw new IllegalArgumentException("source");
        if (priority < 0 || priority > 10) throw new IllegalArgumentException("priority");
        this.source = source;
        this.priority = priority;
    }

    public final String source() { return source; }
    public final int priority() { return priority; }
    public abstract String kind();

    public final String summary() {
        // TODO: KIND:source:severity, using dynamic dispatch.
        return "";
    }

    @Override public int compareTo(SensorEvent other) {
        Objects.requireNonNull(other, "other");
        // TODO: priority DESC; source ASC; kind ASC.
        return 0;
    }
}
