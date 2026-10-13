package practice.day20261012;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Practice20261012Test {
    @Test void runEmptyAndSingle(){assertArrayEquals(new int[]{-1,0},RunScanner.longestBoundedGapRun(new int[]{},1));assertArrayEquals(new int[]{0,1},RunScanner.longestBoundedGapRun(new int[]{9},0));}
    @Test void runTiePrefersEarliest(){assertArrayEquals(new int[]{0,3},RunScanner.longestBoundedGapRun(new int[]{1,3,5,20,21,22},2));}
    @Test void runLongestAtEnd(){assertArrayEquals(new int[]{2,3},RunScanner.longestBoundedGapRun(new int[]{1,2,100,101,102},1));}
    @Test void runStrictGapBoundary(){assertArrayEquals(new int[]{0,3},RunScanner.longestBoundedGapRun(new int[]{1,2,3,7},1));assertArrayEquals(new int[]{0,1},RunScanner.longestBoundedGapRun(new int[]{1,2,3},0));}
    @Test void runOverflowSafety(){assertArrayEquals(new int[]{0,1},RunScanner.longestBoundedGapRun(new int[]{Integer.MIN_VALUE,Integer.MAX_VALUE},1));assertArrayEquals(new int[]{1,2},RunScanner.longestBoundedGapRun(new int[]{Integer.MIN_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE},0));}
    @Test void runDoesNotMutateInput(){int[] values={9,3,5,6};int[] original=values.clone();RunScanner.longestBoundedGapRun(values,3);assertArrayEquals(original,values);}
    @Test void runRejectsInvalidInput(){assertThrows(IllegalArgumentException.class,()->RunScanner.longestBoundedGapRun(new int[]{1},-1));assertThrows(NullPointerException.class,()->RunScanner.longestBoundedGapRun(null,1));}
    @Test void playerImplementsInterfaces(){Player p=new Player("id",7);Rated r=p;assertEquals(7,r.score());assertEquals("id",p.id());}
    @Test void playerOrdersByDescendingScore(){assertTrue(new Player("low",3).compareTo(new Player("high",7))>0);assertTrue(new Player("high",7).compareTo(new Player("low",3))<0);}
    @Test void playerTieBreaksById(){assertTrue(new Player("a",5).compareTo(new Player("b",5))<0);assertEquals(0,new Player("same",5).compareTo(new Player("same",5)));}
    @Test void playerComparisonDoesNotOverflow(){assertTrue(new Player("max",Integer.MAX_VALUE).compareTo(new Player("zero",0))<0);assertTrue(new Player("zero",0).compareTo(new Player("max",Integer.MAX_VALUE))>0);}
    @Test void playerRejectsInvalidFields(){assertThrows(IllegalArgumentException.class,()->new Player(null,2));assertThrows(IllegalArgumentException.class,()->new Player("   ",2));assertThrows(IllegalArgumentException.class,()->new Player("ok",-1));}
    @Test void rankingSortsCopyWithoutMutatingSource(){Player a=new Player("a",3),b=new Player("b",9),c=new Player("c",9);Player[] input={a,c,b};Player[] result=Rankings.sortedCopy(input);assertArrayEquals(new Player[]{b,c,a},result);assertArrayEquals(new Player[]{a,c,b},input);assertNotSame(input,result);}
    @Test void rankingPreservesRuntimeArrayType(){Player[] result=Rankings.sortedCopy(new Player[]{new Player("x",1)});assertEquals(Player[].class,result.getClass());}
    @Test void rankingBestAndEmpty(){Player a=new Player("a",1),b=new Player("b",3);assertSame(b,Rankings.best(new Player[]{a,b}));assertNull(Rankings.best(new Player[]{}));assertEquals(0,Rankings.sortedCopy(new Player[]{}).length);}
    @Test void rankingWorksWithOtherComparableRatedType(){class Badge implements Rated,Comparable<Badge>{final int n;Badge(int n){this.n=n;}public int score(){return n;}public int compareTo(Badge o){return Integer.compare(o.n,n);}}Badge low=new Badge(1),high=new Badge(10);Badge[] ordered=Rankings.sortedCopy(new Badge[]{low,high});assertSame(high,ordered[0]);assertSame(high,Rankings.best(new Badge[]{low,high}));}
    @Test void rankingRejectsNullsEvenInSingleton(){assertThrows(NullPointerException.class,()->Rankings.sortedCopy((Player[])null));assertThrows(NullPointerException.class,()->Rankings.best((Player[])null));assertThrows(NullPointerException.class,()->Rankings.sortedCopy(new Player[]{null}));assertThrows(NullPointerException.class,()->Rankings.best(new Player[]{null}));}
    @Test void teamConstructorMustCopy(){Player a=new Player("a",4),b=new Player("b",6);Player[] input={a};ScoreTeam team=new ScoreTeam(input);input[0]=b;assertSame(a,team.members()[0]);}
    @Test void teamGetterMustCopy(){Player a=new Player("a",4);ScoreTeam team=new ScoreTeam(new Player[]{a});Player[] leaked=team.members();leaked[0]=new Player("changed",99);assertSame(a,team.members()[0]);assertNotSame(leaked,team.members());}
    @Test void teamBestAndEmpty(){Player a=new Player("a",2),b=new Player("b",8);ScoreTeam team=new ScoreTeam(new Player[]{a,b});assertSame(b,team.best());assertNull(new ScoreTeam(new Player[]{}).best());assertEquals(0L,new ScoreTeam(new Player[]{}).totalScore());}
    @Test void teamTotalUsesLong(){ScoreTeam team=new ScoreTeam(new Player[]{new Player("a",Integer.MAX_VALUE),new Player("b",Integer.MAX_VALUE)});assertEquals(2L*Integer.MAX_VALUE,team.totalScore());}
    @Test void teamRejectsNullInputAndMembers(){assertThrows(NullPointerException.class,()->new ScoreTeam(null));assertThrows(NullPointerException.class,()->new ScoreTeam(new Player[]{null}));}
}
