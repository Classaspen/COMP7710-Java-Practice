package practice.day20261011;

import java.util.Objects;

public abstract class WorkItem implements Comparable<WorkItem> {
    private final String title;
    private final int severity;
    protected WorkItem(String title, int severity) {
        this.title = Objects.requireNonNull(title, "title");
        if (title.isBlank()) throw new IllegalArgumentException("blank title");
        if (severity < 1 || severity > 10) throw new IllegalArgumentException("severity must be 1..10");
        this.severity = severity;
    }
    public final String title() { return title; }
    public final int severity() { return severity; }
    public abstract String category();
    public abstract int effortPoints();
    public final String summary() { return category() + ":" + title + ":" + effortPoints(); }
    @Override public int compareTo(WorkItem other) {
        Objects.requireNonNull(other, "other");
        return 0;
    }
}
