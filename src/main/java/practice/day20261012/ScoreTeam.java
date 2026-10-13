package practice.day20261012;
import java.util.Objects;

public final class ScoreTeam {
    private final Player[] members;
    public ScoreTeam(Player[] members) {
        Objects.requireNonNull(members, "members");
        // TODO: reject null elements; defensive copy.
        this.members = members;
    }
    public Player[] members() {
        // TODO: defensive copy.
        return members;
    }
    public Player best() {
        // TODO: delegate to Rankings.best.
        return null;
    }
    public long totalScore() {
        // TODO: use long accumulation.
        return 0L;
    }
}
