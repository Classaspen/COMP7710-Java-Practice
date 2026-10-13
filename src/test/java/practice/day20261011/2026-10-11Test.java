package practice.day20261011;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class Practice20261011Test {
    @Test void zigzagEmptyAndSingle() {
        assertEquals(0, ArrayRuns.longestZigzagRun(new int[]{}));
        assertEquals(-1, ArrayRuns.earliestLongestZigzagStart(new int[]{}));
        assertEquals(1, ArrayRuns.longestZigzagRun(new int[]{8}));
        assertEquals(0, ArrayRuns.earliestLongestZigzagStart(new int[]{8}));
    }
    @Test void zigzagFullRun() {
        int[] a = {1,3,2,4,3};
        assertEquals(5, ArrayRuns.longestZigzagRun(a));
        assertEquals(0, ArrayRuns.earliestLongestZigzagStart(a));
    }
    @Test void zigzagEqualBreaks() {
        int[] a = {1,1,2};
        assertEquals(2, ArrayRuns.longestZigzagRun(a));
        assertEquals(1, ArrayRuns.earliestLongestZigzagStart(a));
        assertEquals(1, ArrayRuns.longestZigzagRun(new int[]{5,5,5}));
        assertEquals(0, ArrayRuns.earliestLongestZigzagStart(new int[]{5,5,5}));
    }
    @Test void zigzagSameDirectionRestartsAtPreviousElement() {
        int[] a = {1,2,3,1,4,2};
        assertEquals(5, ArrayRuns.longestZigzagRun(a));
        assertEquals(1, ArrayRuns.earliestLongestZigzagStart(a));
    }
    @Test void zigzagEarliestOnTie() {
        int[] a = {1,3,2,4,4,8,6,9};
        assertEquals(4, ArrayRuns.longestZigzagRun(a));
        assertEquals(0, ArrayRuns.earliestLongestZigzagStart(a));
    }
    @Test void zigzagExtremeValuesAvoidIntOverflow() {
        int[] a = {Integer.MIN_VALUE,Integer.MAX_VALUE,Integer.MIN_VALUE};
        assertEquals(3, ArrayRuns.longestZigzagRun(a));
        assertEquals(0, ArrayRuns.earliestLongestZigzagStart(a));
    }
    @Test void zigzagDoesNotMutateInput() {
        int[] a = {1,3,2,4,4,8,6,9}; int[] original = a.clone();
        ArrayRuns.longestZigzagRun(a); ArrayRuns.earliestLongestZigzagStart(a);
        assertArrayEquals(original,a);
    }
    @Test void workItemPolymorphicSummary() {
        WorkItem a = new BugItem("Crash",5,2);
        WorkItem b = new FeatureItem("Export",5,2);
        assertEquals("BUG:Crash:12",a.summary());
        assertEquals("FEATURE:Export:11",b.summary());
        assertEquals(12,a.effortPoints()); assertEquals(11,b.effortPoints());
    }
    @Test void workItemConstructorValidation() {
        assertThrows(NullPointerException.class,()->new BugItem(null,5,0));
        assertThrows(IllegalArgumentException.class,()->new BugItem("  ",5,0));
        assertThrows(IllegalArgumentException.class,()->new BugItem("B",0,0));
        assertThrows(IllegalArgumentException.class,()->new FeatureItem("F",11,1));
        assertThrows(IllegalArgumentException.class,()->new BugItem("B",3,-1));
        assertThrows(IllegalArgumentException.class,()->new BugItem("B",3,101));
        assertThrows(IllegalArgumentException.class,()->new FeatureItem("F",3,0));
        assertThrows(IllegalArgumentException.class,()->new FeatureItem("F",3,6));
    }
    @Test void workItemNaturalOrderSeverityDescending() {
        WorkItem high = new BugItem("Z",9,0), low = new FeatureItem("A",3,1);
        assertTrue(high.compareTo(low)<0); assertTrue(low.compareTo(high)>0);
    }
    @Test void workItemNaturalOrderTitleTieBreak() {
        WorkItem a = new BugItem("Alpha",5,1), z = new FeatureItem("Zulu",5,2);
        assertTrue(a.compareTo(z)<0); assertTrue(z.compareTo(a)>0);
        assertEquals(0,a.compareTo(new BugItem("Alpha",5,99)));
    }
    @Test void workItemCompareToNull() {
        WorkItem item = new BugItem("B",1,0);
        assertThrows(NullPointerException.class,()->item.compareTo(null));
    }
    @Test void sortedCopyNaturalOrderAndIndependentArray() {
        WorkItem a = new FeatureItem("A",2,1), b = new BugItem("B",9,2), c = new BugItem("C",9,0);
        WorkItem[] input={a,c,b}; WorkItem[] sorted=WorkItemTools.sortedCopy(input);
        assertArrayEquals(new WorkItem[]{b,c,a},sorted);
        assertArrayEquals(new WorkItem[]{a,c,b},input);
        assertNotSame(input,sorted); assertSame(b,sorted[0]);
    }
    @Test void sortedCopyEmptyAndNull() {
        WorkItem[] empty=new WorkItem[0]; WorkItem[] out=WorkItemTools.sortedCopy(empty);
        assertEquals(0,out.length); assertNotSame(empty,out);
        assertThrows(NullPointerException.class,()->WorkItemTools.sortedCopy(null));
    }
    @Test void highestEffortReturnsOriginalTypedObject() {
        BugItem low=new BugItem("low",2,1), high=new BugItem("high",8,9);
        List<BugItem> bugs=Arrays.asList(low,high);
        BugItem result=WorkItemTools.highestEffort(bugs); assertSame(high,result);
    }
    @Test void highestEffortPolymorphismAndTieEarliest() {
        WorkItem a=new BugItem("A",5,0), b=new FeatureItem("B",7,1), c=new BugItem("C",3,0);
        assertSame(a,WorkItemTools.highestEffort(Arrays.asList(a,b,c)));
    }
    @Test void highestEffortEmptyNullAndNullElement() {
        assertNull(WorkItemTools.highestEffort(new ArrayList<BugItem>()));
        assertThrows(NullPointerException.class,()->WorkItemTools.highestEffort(null));
        assertThrows(IllegalArgumentException.class,()->WorkItemTools.highestEffort(Arrays.asList(new BugItem("B",1,0),null)));
    }
    @Test void comparatorByEffortAscending() {
        WorkItem a=new BugItem("A",9,0), b=new FeatureItem("B",1,1);
        Comparator<WorkItem> c=WorkItemTools.byEffortThenTitle();
        assertTrue(c.compare(b,a)<0); assertTrue(c.compare(a,b)>0);
    }
    @Test void comparatorTitleBreaksEqualEffort() {
        WorkItem a=new BugItem("Alpha",3,0), z=new FeatureItem("Zulu",3,1);
        Comparator<WorkItem> c=WorkItemTools.byEffortThenTitle();
        assertTrue(c.compare(a,z)<0); assertTrue(c.compare(z,a)>0);
    }
    @Test void comparatorDiffersFromNaturalOrder() {
        WorkItem a=new BugItem("High",8,0), b=new FeatureItem("Low",1,1);
        assertTrue(a.compareTo(b)<0);
        assertTrue(WorkItemTools.byEffortThenTitle().compare(a,b)>0);
    }
}
