package practice.day20261013;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class Practice20261013Test {
 @Test void arithmeticEmptyAndSingle(){assertArrayEquals(new int[]{-1,0},ArithmeticRuns.longestArithmeticRun(new int[]{}));assertArrayEquals(new int[]{0,1},ArithmeticRuns.longestArithmeticRun(new int[]{4}));}
 @Test void arithmeticTwoElementsAlwaysLengthTwo(){assertArrayEquals(new int[]{0,2},ArithmeticRuns.longestArithmeticRun(new int[]{9,-9}));}
 @Test void arithmeticLongFirstRun(){assertArrayEquals(new int[]{0,4},ArithmeticRuns.longestArithmeticRun(new int[]{1,3,5,7,8,9}));}
 @Test void arithmeticIntOverflowBoundary(){assertArrayEquals(new int[]{0,2},ArithmeticRuns.longestArithmeticRun(new int[]{Integer.MIN_VALUE,Integer.MAX_VALUE,Integer.MIN_VALUE}));}
 @Test void moveCommandValidAndInterfaceDispatch(){Command c=new MoveCommand("RIGHT",-2);assertEquals("RIGHT",c.label());assertEquals(-2,c.steps());}
 @Test void runnerRevisitsAccumulateWithInterfaceAndWait(){List<Command> cmds=List.of(new MoveCommand("R",2),new MoveCommand("L",-1),new WaitCommand(),new MoveCommand("R2",1),new MoveCommand("L2",-1));RunReport r=CommandRunner.execute(5,0,cmds);assertArrayEquals(new int[]{1,3,2,0,0},r.visitCounts());assertEquals(1,r.position());}
 @Test void runnerInvalidRightStopsAtomically(){RunReport r=CommandRunner.execute(4,0,List.of(new MoveCommand("A",1),new MoveCommand("BAD",5),new MoveCommand("L",-1)));assertTrue(r.stopped());assertArrayEquals(new int[]{1,1,0,0},r.visitCounts());}
 @Test void sortingMinValueMustUseLongAbs(){MoveCommand min=new MoveCommand("MIN",Integer.MIN_VALUE),max=new MoveCommand("MAX",Integer.MAX_VALUE),zero=new MoveCommand("ZERO",0);assertEquals(List.of(zero,max,min),CommandRunner.sortedCopyByMagnitude(List.of(min,zero,max)));}
 @Test void reportDefensiveCopyAtConstruction(){int[] counts={1,2};List<String> labels=new ArrayList<>(List.of("X"));RunReport report=new RunReport(1,false,counts,labels);counts[1]=99;labels.add("Y");assertArrayEquals(new int[]{1,2},report.visitCounts());assertEquals(List.of("X"),report.executedLabels());}
}
