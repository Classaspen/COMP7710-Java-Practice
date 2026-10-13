package practice.day20261012;

public final class Player implements Rated, Comparable<Player> {
    private final String id;
    private final int score;

    public Player(String id, int score) {
        // TODO: reject null/blank id and negative score.
        this.id = id;
        this.score = score;
    }
    public String id() { return id; }
    @Override public int score() { return score; }
    @Override public int compareTo(Player other) {
        // TODO: descending score; ascending id for ties; avoid subtraction.
        return 0;
    }
}
