package practice.day20261014;

import java.util.Objects;

public final class ParityRuns {
    private ParityRuns() { }

    /** Returns {earliestStart, longestLength}; empty -> {-1, 0}. */
    public static int[] longestAlternatingParityRun(int[] values) {
        Objects.requireNonNull(values, "values");
        // TODO: one for loop, O(n) time, O(1) extra space; do not mutate values.
        return new int[]{-1, 0};
    }
}
