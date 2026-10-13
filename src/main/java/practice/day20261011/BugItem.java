package practice.day20261011;

public final class BugItem extends WorkItem {
    private final int reproductions;

    public BugItem(String title, int severity, int reproductions) {
        super(title, severity);
        if (reproductions < 0 || reproductions > 100) {
            throw new IllegalArgumentException("reproductions must be 0..100");
        }
        this.reproductions = reproductions;
    }

    public int reproductions() { return reproductions; }

    @Override public String category() { return "BUG"; }
    @Override public int effortPoints() { return 0; }
}
