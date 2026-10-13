package practice.day20261012;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Practice20261012Test {
    @Test void runEmptyAndSingle() { assertArrayEquals(new int[]{-1,0}, RunScanner.longestBoundedGapRun(new int[]{},1)); assertArrayEquals(new int[]{0,1},RunScanner.longestBoundedGapRun(new int[]{9},0)); }
    @Test void runTiePrefersEarliest() { assertArrayEquals(new int[]{0,3},RunScanner.longestBoundedGapRun(new int[]{1,3,5,20,21,22},2)); }
    @Test void runLongestAtEnd() { assertArrayEquals(new int[]{2,3},RunScanner.longestBoundedGapRun(new int[]{1,2,100,101,102},1)); }
    @Test void runOverflowSafety() { assertArrayEquals(new int[]{0,1},RunScanner.longestBoundedGapRun(new int[]{Integer.MIN_VALUE,Integer.MAX_VALUE},1)); }
    @Test void playerOrdersByDescendingScore(){ assertTrue(new Player("low",3).compareTo(new Player("high",7))>0); }
    @Test void playerTieBreaksById(){ assertTrue(new Player("a",5).compareTo(new Player("b",5))<0); }
    @Test void rankingSortsCopyWithoutMutatingSource(){ Player a=new Player("a",3),b=new Player("b",9),c=new Player("c",9);Player[] input={a,c,b};Player[] result=Rankings.sortedCopy(input);assertArrayEquals(new Player[]{b,c,a},result);assertArrayEquals(new Player[]{a,c,b},input);assertNotSame(input,result); }
    @Test void rankingBestAndEmpty(){ Player a=new Player("a",1),b=new Player("b",3);assertSame(b,Rankings.best(new Player[]{a,b}));assertNull(Rankings.best(new Player[]{})); }
    @Test void teamConstructorMustCopy(){ Player a=new Player("a",4),b=new Player("b",6);Player[] input={a};ScoreTeam team=new ScoreTeam(input);input[0]=b;assertSame(a,team.members()[0]); }
    @Test void teamTotalUsesLong(){ ScoreTeam team=new ScoreTeam(new Player[]{new Player("a",Integer.MAX_VALUE),new Player("b",Integer.MAX_VALUE)});assertEquals(2L*Integer.MAX_VALUE,team.totalScore()); }
}
