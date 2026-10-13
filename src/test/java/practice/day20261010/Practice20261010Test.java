package practice.day20261010;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Practice20261010Test {

    @Test void alternatingEmptyAndZeros() {
        assertEquals(0, ArrayPatterns.longestAlternatingSignRun(new int[]{}));
        assertEquals(0, ArrayPatterns.longestAlternatingSignRun(new int[]{0, 0, 0}));
    }

    @Test void alternatingSingleAndZeroBreak() {
        assertEquals(1, ArrayPatterns.longestAlternatingSignRun(new int[]{-7}));
        assertEquals(3, ArrayPatterns.longestAlternatingSignRun(new int[]{-1, 2, -3, 0, 4, -5}));
    }

    @Test void alternatingStrictSignAndFinalRun() {
        assertEquals(2, ArrayPatterns.longestAlternatingSignRun(new int[]{2, 2, -1}));
        assertEquals(4, ArrayPatterns.longestAlternatingSignRun(new int[]{1, 1, -1, 2, -3}));
    }

    @Test void alternatingMinValueOverflowTrap() {
        assertEquals(2, ArrayPatterns.longestAlternatingSignRun(new int[]{Integer.MIN_VALUE, 1}));
        assertEquals(3, ArrayPatterns.longestAlternatingSignRun(new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, 1}));
    }

    @Test void increasingStartEmptyAndSingleton() {
        assertEquals(-1, ArrayPatterns.firstLongestIncreasingRunStart(new int[]{}));
        assertEquals(0, ArrayPatterns.firstLongestIncreasingRunStart(new int[]{5}));
    }

    @Test void increasingStartEarliestTie() {
        assertEquals(0, ArrayPatterns.firstLongestIncreasingRunStart(new int[]{1, 2, 0, 1}));
        assertEquals(2, ArrayPatterns.firstLongestIncreasingRunStart(new int[]{1, 2, 0, 1, 2}));
    }

    @Test void increasingStartStrictAndLastRun() {
        assertEquals(0, ArrayPatterns.firstLongestIncreasingRunStart(new int[]{5, 5, 5}));
        assertEquals(2, ArrayPatterns.firstLongestIncreasingRunStart(new int[]{9, 7, 1, 2, 3, 4}));
    }

    @Test void arrayPatternsDoNotMutateInputs() {
        int[] values = {Integer.MIN_VALUE, 1, 0, 3, 4, 5};
        int[] before = values.clone();
        ArrayPatterns.longestAlternatingSignRun(values);
        ArrayPatterns.firstLongestIncreasingRunStart(values);
        assertArrayEquals(before, values);
    }

    @Test void trackerInitialStateNonZeroStart() {
        RouteSession s = new RouteTracker(5, 3);
        assertEquals(3, s.position());
        assertFalse(s.stopped());
        assertArrayEquals(new int[]{0, 0, 0, 1, 0}, s.visitCounts());
    }

    @Test void trackerPositiveAndNegativeVisits() {
        RouteSession s = new RouteTracker(6, 3);
        assertTrue(s.move(-2));
        assertEquals(1, s.position());
        assertTrue(s.move(1));
        assertEquals(2, s.position());
        assertArrayEquals(new int[]{0, 1, 2, 1, 0, 0}, s.visitCounts());
    }

    @Test void trackerRevisitsMustAccumulate() {
        RouteTracker t = RouteTracker.fromMoves(5, 0, new int[]{2, -1, 1, -1});
        assertArrayEquals(new int[]{1, 3, 2, 0, 0}, t.visitCounts());
        assertEquals(1, t.position());
        assertFalse(t.stopped());
    }

    @Test void trackerZeroMoveIsValidButDoesNotCount() {
        RouteSession s = new RouteTracker(3, 1);
        assertTrue(s.move(0));
        assertTrue(s.move(0));
        assertEquals(1, s.position());
        assertArrayEquals(new int[]{0, 1, 0}, s.visitCounts());
        assertFalse(s.stopped());
    }

    @Test void trackerInvalidHighMoveStopsBeforeAnySteps() {
        RouteSession s = new RouteTracker(4, 0);
        assertTrue(s.move(1));
        assertFalse(s.move(5));
        assertEquals(1, s.position());
        assertTrue(s.stopped());
        assertArrayEquals(new int[]{1, 1, 0, 0}, s.visitCounts());
        assertFalse(s.move(-1));
        assertArrayEquals(new int[]{1, 1, 0, 0}, s.visitCounts());
    }

    @Test void trackerInvalidNegativeMoveStops() {
        RouteSession s = new RouteTracker(4, 2);
        assertFalse(s.move(-3));
        assertTrue(s.stopped());
        assertEquals(2, s.position());
        assertArrayEquals(new int[]{0, 0, 1, 0}, s.visitCounts());
    }

    @Test void trackerOverflowMustNotWrap() {
        RouteSession s = new RouteTracker(4, 2);
        assertFalse(s.move(Integer.MAX_VALUE));
        assertEquals(2, s.position());
        assertArrayEquals(new int[]{0, 0, 1, 0}, s.visitCounts());
        RouteSession t = new RouteTracker(4, 2);
        assertFalse(t.move(Integer.MIN_VALUE));
        assertEquals(2, t.position());
    }

    @Test void factoryAppliesMovesAndIgnoresRemainder() {
        int[] moves = {1, 5, -1};
        int[] before = moves.clone();
        RouteTracker t = RouteTracker.fromMoves(4, 0, moves);
        assertArrayEquals(new int[]{1, 1, 0, 0}, t.visitCounts());
        assertEquals(1, t.position());
        assertTrue(t.stopped());
        assertArrayEquals(before, moves);
    }

    @Test void factoryNegativeZeroAndNonZeroStart() {
        RouteTracker t = RouteTracker.fromMoves(6, 3, new int[]{-2, 1, 0, 2});
        assertArrayEquals(new int[]{0, 1, 2, 2, 1, 0}, t.visitCounts());
        assertEquals(4, t.position());
    }

    @Test void trackerCountsGetterMustBeDefensive() {
        RouteTracker t = new RouteTracker(3, 1);
        int[] first = t.visitCounts();
        first[1] = 999;
        assertArrayEquals(new int[]{0, 1, 0}, t.visitCounts());
        assertNotSame(first, t.visitCounts());
    }

    @Test void snapshotMustBeIndependentFromTracker() {
        RouteTracker t = new RouteTracker(4, 1);
        assertTrue(t.move(1));
        RouteSnapshot snapshot = t.snapshot();
        assertEquals(2, snapshot.position());
        assertFalse(snapshot.stopped());
        assertArrayEquals(new int[]{0, 1, 1, 0}, snapshot.visitCounts());
        assertTrue(t.move(1));
        assertEquals(2, snapshot.position());
        assertArrayEquals(new int[]{0, 1, 1, 0}, snapshot.visitCounts());
    }

    @Test void snapshotGetterAndConstructorMustBeDefensive() {
        int[] source = {1, 2, 3};
        RouteSnapshot s = new RouteSnapshot(1, false, source);
        source[1] = 99;
        int[] leaked = s.visitCounts();
        leaked[2] = 88;
        assertArrayEquals(new int[]{1, 2, 3}, s.visitCounts());
    }

    @Test void factoryAndConstructorValidateInputs() {
        assertThrows(IllegalArgumentException.class, () -> new RouteTracker(0, 0));
        assertThrows(IllegalArgumentException.class, () -> new RouteTracker(3, -1));
        assertThrows(IllegalArgumentException.class, () -> new RouteTracker(3, 3));
        assertThrows(NullPointerException.class, () -> RouteTracker.fromMoves(3, 0, null));
    }

    @Test void snapshotPreservesStoppedFlag() {
        RouteTracker t = new RouteTracker(3, 1);
        assertFalse(t.move(9));
        RouteSnapshot s = t.snapshot();
        assertTrue(s.stopped());
        assertEquals(1, s.position());
        assertArrayEquals(new int[]{0, 1, 0}, s.visitCounts());
    }
}
