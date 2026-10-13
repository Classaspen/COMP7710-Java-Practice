package practice.day20261012;
import java.util.Objects;

public final class RunScanner {
    private RunScanner() {}
    /** Returns {earliestStart,longestLength}; empty -> {-1,0}. */
    public static int[] longestBoundedGapRun(int[] values, int maxGap) {
        Objects.requireNonNull(values, "values");
        if (maxGap < 0) throw new IllegalArgumentException("maxGap");
        // TODO: O(n), O(1) extra space; use long for adjacent differences.
        return new int[]{-1, 0};
    }
}
