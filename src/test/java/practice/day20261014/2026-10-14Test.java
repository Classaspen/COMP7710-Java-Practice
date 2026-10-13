package practice.day20261014;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class Practice20261014Test {
    @Test void parityEmptyAndSingle() {
        assertArrayEquals(new int[]{-1,0},ParityRuns.longestAlternatingParityRun(new int[]{}));
        assertArrayEquals(new int[]{0,1},ParityRuns.longestAlternatingParityRun(new int[]{7}));
    }
    @Test void parityFullAlternation() {
        assertArrayEquals(new int[]{0,5},ParityRuns.longestAlternatingParityRun(new int[]{2,5,8,11,14}));
    }
    @Test void parityResetAndTail() {
        assertArrayEquals(new int[]{1,4},ParityRuns.longestAlternatingParityRun(new int[]{2,4,7,10,13}));
        assertArrayEquals(new int[]{1,4},ParityRuns.longestAlternatingParityRun(new int[]{3,3,4,5,6}));
    }
    @Test void parityNegativeOddAndExtreme() {
        assertArrayEquals(new int[]{0,4},ParityRuns.longestAlternatingParityRun(new int[]{0,-1,-2,-3}));
        assertArrayEquals(new int[]{0,3},ParityRuns.longestAlternatingParityRun(new int[]{Integer.MIN_VALUE,Integer.MAX_VALUE,0}));
    }
    @Test void parityTieEarliestAndNoMutation() {
        int[] v={2,3,8,8,9,4},copy=v.clone();
        assertArrayEquals(new int[]{0,3},ParityRuns.longestAlternatingParityRun(v));
        assertArrayEquals(copy,v);
    }
    @Test void parityAllSameAndNull() {
        assertArrayEquals(new int[]{0,1},ParityRuns.longestAlternatingParityRun(new int[]{2,4,6}));
        assertThrows(NullPointerException.class,()->ParityRuns.longestAlternatingParityRun(null));
    }
    @Test void polymorphismAndSummary() {
        SensorEvent m=new MotionEvent("camera",3,12),b=new BatteryEvent("power",2,40);
        assertEquals("MOTION:camera:15",m.summary());
        assertEquals("BATTERY:power:62",b.summary());
        Alertable a=m;assertEquals(15,a.severity());
    }
    @Test void validation() {
        assertThrows(IllegalArgumentException.class,()->new MotionEvent(null,2,1));
        assertThrows(IllegalArgumentException.class,()->new BatteryEvent(" ",2,10));
        assertThrows(IllegalArgumentException.class,()->new MotionEvent("m",-1,0));
        assertThrows(IllegalArgumentException.class,()->new BatteryEvent("b",11,0));
        assertThrows(IllegalArgumentException.class,()->new MotionEvent("m",2,101));
        assertThrows(IllegalArgumentException.class,()->new BatteryEvent("b",2,-1));
    }
    @Test void naturalOrder() {
        SensorEvent high=new MotionEvent("z",9,1),low=new BatteryEvent("a",2,90);
        assertTrue(high.compareTo(low)<0);
        assertTrue(new MotionEvent("a",5,1).compareTo(new BatteryEvent("z",5,50))<0);
        assertTrue(new BatteryEvent("same",4,30).compareTo(new MotionEvent("same",4,1))<0);
        assertThrows(NullPointerException.class,()->high.compareTo(null));
    }
    @Test void boardSnapshotDefensive() {
        MotionEvent m=new MotionEvent("first",1,10);
        List<MotionEvent> source=new ArrayList<>(List.of(m));
        EventBoard board=new EventBoard(source);
        source.clear();List<SensorEvent> snap=board.snapshot();
        assertEquals(List.of(m),snap);
        board.add(new BatteryEvent("second",2,20));
        assertEquals(List.of(m),snap);
        assertEquals(2,board.snapshot().size());
        assertThrows(UnsupportedOperationException.class,()->snap.add(m));
    }
    @Test void boardMaxTieAndEmpty() {
        SensorEvent a=new MotionEvent("a",2,50),b=new BatteryEvent("b",3,51);
        assertSame(a,new EventBoard(List.of(a,b)).highestSeverity());
        assertNull(new EventBoard(List.of()).highestSeverity());
    }
    @Test void boardSafeCast() {
        MotionEvent m=new MotionEvent("first",1,1);
        EventBoard board=new EventBoard(List.of(new BatteryEvent("bat",2,90),m));
        assertSame(m,board.firstOfType(MotionEvent.class));
        assertNull(board.firstOfType(UnknownEvent.class));
    }
    @Test void boardNullValidation() {
        assertThrows(NullPointerException.class,()->new EventBoard(null));
        assertThrows(NullPointerException.class,()->new EventBoard(Arrays.asList(new MotionEvent("m",1,1),null)));
        EventBoard board=new EventBoard(List.of());
        assertThrows(NullPointerException.class,()->board.add(null));
        assertThrows(NullPointerException.class,()->board.firstOfType(null));
    }
    @Test void sortedCopyGenericAndNulls() {
        MotionEvent a=new MotionEvent("a",1,1),b=new MotionEvent("b",9,1);
        List<MotionEvent> input=new ArrayList<>(List.of(a,b));
        List<MotionEvent> sorted=EventBoard.sortedCopy(input);
        assertEquals(List.of(b,a),sorted);assertEquals(List.of(a,b),input);
        assertNotSame(input,sorted);
        sorted.add(new MotionEvent("c",0,1));assertEquals(3,sorted.size());
        assertThrows(NullPointerException.class,()->EventBoard.sortedCopy(null));
        assertThrows(NullPointerException.class,()->EventBoard.sortedCopy(Arrays.asList(a,null)));
    }
    @Test void comparatorSeveritySourceKind() {
        Comparator<SensorEvent> c=EventBoard.bySeverityThenSource();
        SensorEvent low=new MotionEvent("z",9,1),high=new BatteryEvent("a",1,10);
        assertTrue(c.compare(low,high)<0);
        SensorEvent x=new MotionEvent("a",1,10),y=new BatteryEvent("z",1,90);
        assertTrue(c.compare(x,y)<0);
        assertTrue(c.compare(new BatteryEvent("same",0,90),new MotionEvent("same",0,10))<0);
    }
    private static final class UnknownEvent extends SensorEvent {
        UnknownEvent(){super("unknown",0);}
        @Override public String kind(){return "OTHER";}
        @Override public int severity(){return 0;}
    }
}
