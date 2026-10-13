package practice.day20261013;

import java.util.List;
import java.util.Objects;

public final class RunReport {
    private final int position; private final boolean stopped; private final int[] counts; private final List<String> executedLabels;
    public RunReport(int position,boolean stopped,int[] counts,List<String> executedLabels){this.position=position;this.stopped=stopped;this.counts=Objects.requireNonNull(counts,"counts");this.executedLabels=Objects.requireNonNull(executedLabels,"executedLabels");}
    public int position(){return position;} public boolean stopped(){return stopped;} public int[] visitCounts(){return counts;} public List<String> executedLabels(){return executedLabels;}
}
