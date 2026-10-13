package practice.day20261014;

public final class BatteryEvent extends SensorEvent {
    private final int percentage;

    public BatteryEvent(String source, int priority, int percentage) {
        super(source, priority);
        if (percentage < 0 || percentage > 100) throw new IllegalArgumentException("percentage");
        this.percentage = percentage;
    }

    public int percentage() { return percentage; }
    @Override public String kind() { return "BATTERY"; }
    @Override public int severity() {
        // TODO: priority + (100 - percentage).
        return 0;
    }
}
