package practice.day20261011;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class Practice20261011Test {
    @Test void zigzagEmptyAndSingle(){assertEquals(0,ArrayRuns.longestZigzagRun(new int[]{}));assertEquals(-1,ArrayRuns.earliestLongestZigzagStart(new int[]{}));assertEquals(1,ArrayRuns.longestZigzagRun(new int[]{8}));assertEquals(0,ArrayRuns.earliestLongestZigzagStart(new int[]{8}));}
    @Test void zigzagFullRun(){int[] a={1,3,2,4,3};assertEquals(5,ArrayRuns.longestZigzagRun(a));assertEquals(0,ArrayRuns.earliestLongestZigzagStart(a));}
    @Test void zigzagEqualBreaks(){int[] a={1,1,2};assertEquals(2,ArrayRuns.longestZigzagRun(a));assertEquals(1,ArrayRuns.earliestLongestZigzagStart(a));assertEquals(1,ArrayRuns.longestZigzagRun(new int[]{5,5,5}));}
    @Test void zigzagExtremeValuesAvoidIntOverflow(){int[] a={Integer.MIN_VALUE,Integer.MAX_VALUE,Integer.MIN_VALUE};assertEquals(3,ArrayRuns.longestZigzagRun(a));}
    @Test void workItemPolymorphicSummary(){WorkItem a=new BugItem("Crash",5,2);WorkItem b=new FeatureItem("Export",5,2);assertEquals("BUG:Crash:12",a.summary());assertEquals("FEATURE:Export:11",b.summary());}
    @Test void workItemNaturalOrderSeverityDescending(){WorkItem high=new BugItem("Z",9,0),low=new FeatureItem("A",3,1);assertTrue(high.compareTo(low)<0);}
    @Test void workItemNaturalOrderTitleTieBreak(){WorkItem a=new BugItem("Alpha",5,1),z=new FeatureItem("Zulu",5,2);assertTrue(a.compareTo(z)<0);}
    @Test void sortedCopyNaturalOrderAndIndependentArray(){WorkItem a=new FeatureItem("A",2,1),b=new BugItem("B",9,2),c=new BugItem("C",9,0);WorkItem[] input={a,c,b};WorkItem[] sorted=WorkItemTools.sortedCopy(input);assertArrayEquals(new WorkItem[]{b,c,a},sorted);assertArrayEquals(new WorkItem[]{a,c,b},input);assertNotSame(input,sorted);}
    @Test void highestEffortReturnsOriginalTypedObject(){BugItem low=new BugItem("low",2,1),high=new BugItem("high",8,9);BugItem result=WorkItemTools.highestEffort(Arrays.asList(low,high));assertSame(high,result);}
    @Test void comparatorByEffortAscending(){WorkItem a=new BugItem("A",9,0),b=new FeatureItem("B",1,1);Comparator<WorkItem> c=WorkItemTools.byEffortThenTitle();assertTrue(c.compare(b,a)<0);}
}
