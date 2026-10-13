package practice.day20261010;

import java.util.Objects;

public final class RouteSnapshot {
    private final int position;
    private final boolean stopped;
    private final int[] counts;

    public RouteSnapshot(int position, boolean stopped, int[] counts) {
        this.position = position;
        this.stopped = stopped;
        // TODO: defensive copy on construction.
        this.counts = Objects.requireNonNull(counts, "counts");
    }

    public int position() {
        return position;
    }

    public boolean stopped() {
        return stopped;
    }

    public int[] visitCounts() {
        // TODO: defensive copy on return.
        return counts;
    }
}
