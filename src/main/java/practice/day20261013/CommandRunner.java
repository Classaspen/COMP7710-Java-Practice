package practice.day20261013;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CommandRunner {
    private CommandRunner() {}
    public static <T extends Command> RunReport execute(int size,int start,List<T> commands){
        if(size<=0||start<0||start>=size) throw new IllegalArgumentException("size or start");
        Objects.requireNonNull(commands,"commands");
        int[] counts=new int[size]; counts[start]=1;
        return new RunReport(start,false,counts,new ArrayList<>());
    }
    public static <T extends Command> List<T> sortedCopyByMagnitude(List<T> commands){
        Objects.requireNonNull(commands,"commands");
        return new ArrayList<>(commands);
    }
}
