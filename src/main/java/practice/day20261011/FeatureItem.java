package practice.day20261011;

public final class FeatureItem extends WorkItem {
    private final int complexity;

    public FeatureItem(String title, int severity, int complexity) {
        super(title, severity);
        if (complexity < 1 || complexity > 5) throw new IllegalArgumentException("complexity must be 1..5");
        this.complexity = complexity;
    }
    public int complexity() { return complexity; }
    @Override public String category() { return "FEATURE"; }
    @Override public int effortPoints() { return 0; }
}
