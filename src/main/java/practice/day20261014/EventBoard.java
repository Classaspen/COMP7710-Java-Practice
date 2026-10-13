package practice.day20261014;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class EventBoard {
    private final List<SensorEvent> events;

    public EventBoard(List<? extends SensorEvent> initial) {
        Objects.requireNonNull(initial, "initial");
        // TODO: reject null elements and copy list structure.
        this.events = new ArrayList<>();
    }

    public void add(SensorEvent event) {
        Objects.requireNonNull(event, "event");
        // TODO
    }

    public List<SensorEvent> snapshot() {
        // TODO: unmodifiable independent snapshot.
        return List.of();
    }

    public SensorEvent highestSeverity() {
        // TODO: maximum severity, earliest insertion on ties, empty -> null.
        return null;
    }

    public <T extends SensorEvent> T firstOfType(Class<T> type) {
        Objects.requireNonNull(type, "type");
        // TODO: safe runtime type check, then type.cast(...).
        return null;
    }

    public static <T extends SensorEvent> List<T> sortedCopy(List<T> input) {
        Objects.requireNonNull(input, "input");
        // TODO: reject null elements; sort new modifiable list by natural order.
        return new ArrayList<>(input);
    }

    public static Comparator<SensorEvent> bySeverityThenSource() {
        // TODO: severity ASC, then source ASC, then kind ASC.
        return (a, b) -> 0;
    }
}
