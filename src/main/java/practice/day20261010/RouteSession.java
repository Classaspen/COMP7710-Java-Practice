package practice.day20261010;

public interface RouteSession {
    boolean move(int steps);
    int position();
    boolean stopped();
    int[] visitCounts();
}
