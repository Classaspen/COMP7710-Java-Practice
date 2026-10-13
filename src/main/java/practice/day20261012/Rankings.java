package practice.day20261012;
import java.util.Objects;

public final class Rankings {
    private Rankings() {}

    public static <T extends Rated & Comparable<? super T>> T[] sortedCopy(T[] input) {
        Objects.requireNonNull(input, "input");
        // TODO: reject null elements; sort a new array. No new T[n].
        return input;
    }
    public static <T extends Rated & Comparable<? super T>> T best(T[] input) {
        Objects.requireNonNull(input, "input");
        // TODO: reject null elements; return first in natural order or null.
        return null;
    }
}
