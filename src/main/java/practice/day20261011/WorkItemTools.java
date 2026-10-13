package practice.day20261011;

import java.util.Comparator;
import java.util.List;

public final class WorkItemTools {
    private WorkItemTools() {}
    public static <T extends WorkItem> T highestEffort(List<T> items) { return null; }
    public static WorkItem[] sortedCopy(WorkItem[] input) { return new WorkItem[0]; }
    public static Comparator<WorkItem> byEffortThenTitle() { return (a,b) -> 0; }
}
