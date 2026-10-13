package practice.day20261014;

public final class MotionEvent extends SensorEvent {
    private final int movement;

    public MotionEvent(String source, int priority, int movement) {
        super(source, priority);
        if (movement < 0 || movement > 100) throw new IllegalArgumentException("movement");
        this.movement = movement;
    }

    public int movement() { return movement; }
    @Override public String kind() { return "MOTION"; }
    @Override public int severity() {
        // TODO: priority + movement.
        return 0;
    }
}
