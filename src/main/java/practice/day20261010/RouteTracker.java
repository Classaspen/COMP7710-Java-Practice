package practice.day20261010;

import java.util.Objects;

public final class RouteTracker implements RouteSession {
    private final int size;
    private final int[] counts;
    private int position;
    private boolean stopped;

    public RouteTracker(int size, int start) {
        if (size <= 0 || start < 0 || start >= size) {
            throw new IllegalArgumentException("invalid size or start");
        }
        this.size = size;
        this.counts = new int[size];
        this.position = start;
        this.stopped = false;
        this.counts[start] = 1;
    }

    @Override
    public boolean move(int steps) {
        // TODO: check the entire move first; support positive/negative/zero.
        return false;
    }

    @Override
    public int position() {
        return position;
    }

    @Override
    public boolean stopped() {
        return stopped;
    }

    @Override
    public int[] visitCounts() {
        // TODO: defensive copy.
        return counts;
    }

    public RouteSnapshot snapshot() {
        // TODO: ensure this snapshot is independent of future changes.
        return new RouteSnapshot(position, stopped, counts);
    }

    public static RouteTracker fromMoves(int size, int start, int[] moves) {
        Objects.requireNonNull(moves, "moves");
        RouteTracker tracker = new RouteTracker(size, start);
        // TODO: process moves in order; stop after the first invalid move.
        return tracker;
    }
}
