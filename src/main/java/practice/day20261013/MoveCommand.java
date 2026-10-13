package practice.day20261013;

public final class MoveCommand implements Command {
    private final String label; private final int steps;
    public MoveCommand(String label,int steps){this.label=label;this.steps=steps;}
    @Override public int steps(){return steps;}
    @Override public String label(){return label;}
}
