package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class BusyPeriodTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BusyPeriod(null, "25/03/2026"));
        assertThrows(NullPointerException.class, () -> new BusyPeriod("25/03/2026", null));
    }

    @Test
    public void constructor_invalidDateFormat_throwsIllegalArgumentException() {
        String invalidDate = "2026-03-25";
        assertThrows(IllegalArgumentException.class, () -> new BusyPeriod(invalidDate, "28/03/2026"));
        assertThrows(IllegalArgumentException.class, () -> new BusyPeriod("25/03/2026", invalidDate));
    }

    @Test
    public void constructor_invalidDateLogic_throwsIllegalArgumentException() {
        String startDate = "28/03/2026";
        String endDate = "25/03/2026";
        assertThrows(IllegalArgumentException.class, () -> new BusyPeriod(startDate, endDate));
    }

    @Test
    public void isValidDateFormat() {
        // null date
        assertThrows(NullPointerException.class, () -> BusyPeriod.isValidDateFormat(null));

        // invalid formats
        assertFalse(BusyPeriod.isValidDateFormat("")); // empty string
        assertFalse(BusyPeriod.isValidDateFormat(" ")); // spaces only
        assertFalse(BusyPeriod.isValidDateFormat("25-03-2026")); // wrong separator
        assertFalse(BusyPeriod.isValidDateFormat("2026/03/25")); // wrong order
        assertFalse(BusyPeriod.isValidDateFormat("25/03/26")); // wrong year format
        assertFalse(BusyPeriod.isValidDateFormat("32/03/2026")); // invalid day (exceeds 31)
        assertFalse(BusyPeriod.isValidDateFormat("25/13/2026")); // invalid month

        // dates that are invalid for specific months
        assertFalse(BusyPeriod.isValidDateFormat("31/04/2026")); // April has 30 days
        assertFalse(BusyPeriod.isValidDateFormat("29/02/2026")); // 2026 is not a leap year
        assertTrue(BusyPeriod.isValidDateFormat("29/02/2024")); // 2024 is a leap year

        // valid formats
        assertTrue(BusyPeriod.isValidDateFormat("25/03/2026"));
        assertTrue(BusyPeriod.isValidDateFormat("01/01/2000"));
    }

    @Test
    public void constructor_invalidDateForMonth_throwsIllegalArgumentException() {
        // This test is expected to FAIL if the current implementation rounds the date instead of throwing error.
        assertThrows(IllegalArgumentException.class, () -> new BusyPeriod("31/04/2026", "01/05/2026"));
    }

    @Test
    public void isValidBusyPeriod() {
        LocalDate start = LocalDate.of(2026, 3, 25);
        LocalDate end = LocalDate.of(2026, 3, 28);

        // start before end -> true
        assertTrue(BusyPeriod.isValidBusyPeriod(start, end));

        // start equals end -> true
        assertTrue(BusyPeriod.isValidBusyPeriod(start, start));

        // start after end -> false
        assertFalse(BusyPeriod.isValidBusyPeriod(end, start));
    }

    @Test
    public void isWithinPeriod() {
        BusyPeriod busyPeriod = new BusyPeriod("25/03/2026", "28/03/2026");

        // date within period -> true
        assertTrue(busyPeriod.isWithinPeriod(LocalDate.of(2026, 3, 25)));
        assertTrue(busyPeriod.isWithinPeriod(LocalDate.of(2026, 3, 26)));
        assertTrue(busyPeriod.isWithinPeriod(LocalDate.of(2026, 3, 28)));

        // date before period -> false
        assertFalse(busyPeriod.isWithinPeriod(LocalDate.of(2026, 3, 24)));

        // date after period -> false
        assertFalse(busyPeriod.isWithinPeriod(LocalDate.of(2026, 3, 29)));
    }

    @Test
    public void equals() {
        BusyPeriod busyPeriod = new BusyPeriod("25/03/2026", "28/03/2026");

        // same values -> returns true
        assertTrue(busyPeriod.equals(new BusyPeriod("25/03/2026", "28/03/2026")));

        // same object -> returns true
        assertTrue(busyPeriod.equals(busyPeriod));

        // null -> returns false
        assertFalse(busyPeriod.equals(null));

        // different types -> returns false
        assertFalse(busyPeriod.equals(5.0f));

        // different values (different start) -> returns false
        assertFalse(busyPeriod.equals(new BusyPeriod("26/03/2026", "28/03/2026")));

        // different values (different end) -> returns false
        assertFalse(busyPeriod.equals(new BusyPeriod("25/03/2026", "29/03/2026")));
    }

    /**
     * Yi Heng: I used AI to help me ideate what are the possible test cases for overlapsWith() method,
     * and I have implemented the ones that I think are the most important, as written below.
     * If you got other suggestions, please let me know! Tks!
     */

    @Test
    public void overlapsWith_overlappingPeriods_returnsTrue() {
        BusyPeriod bp1 = new BusyPeriod("01/03/2026", "30/06/2026");
        BusyPeriod bp2 = new BusyPeriod("15/06/2026", "31/12/2026");
        assertTrue(bp1.overlapsWith(bp2));
        assertTrue(bp2.overlapsWith(bp1)); // symmetry
    }

    @Test
    public void overlapsWith_oneInsideOther_returnsTrue() {
        BusyPeriod outer = new BusyPeriod("01/01/2026", "31/12/2026");
        BusyPeriod inner = new BusyPeriod("01/06/2026", "30/06/2026");
        assertTrue(outer.overlapsWith(inner));
        assertTrue(inner.overlapsWith(outer));
    }

    @Test
    public void overlapsWith_exactMatch_returnsTrue() {
        BusyPeriod bp1 = new BusyPeriod("01/01/2026", "31/12/2026");
        BusyPeriod bp2 = new BusyPeriod("01/01/2026", "31/12/2026");
        assertTrue(bp1.overlapsWith(bp2));
    }

    @Test
    public void overlapsWith_touchingOnBoundary_returnsTrue() {
        // End of bbusyperiod1 == Start of busyperiod2
        // they share exactly one day in common, so they DO overlap
        BusyPeriod bp1 = new BusyPeriod("01/01/2026", "15/06/2026");
        BusyPeriod bp2 = new BusyPeriod("15/06/2026", "31/12/2026");
        assertTrue(bp1.overlapsWith(bp2));
        assertTrue(bp2.overlapsWith(bp1));
    }

    @Test
    public void overlapsWith_nonOverlappingPeriods_returnsFalse() {
        BusyPeriod bp1 = new BusyPeriod("01/01/2026", "31/05/2026");
        BusyPeriod bp2 = new BusyPeriod("01/06/2026", "31/12/2026");
        assertFalse(bp1.overlapsWith(bp2));
        assertFalse(bp2.overlapsWith(bp1));
    }

    @Test
    public void getStartDateString_returnsCorrectFormat() {
        BusyPeriod bp = new BusyPeriod("25/03/2026", "28/03/2026");
        assertEquals("25/03/2026", bp.getStartDateString());
    }

    @Test
    public void getEndDateString_returnsCorrectFormat() {
        BusyPeriod bp = new BusyPeriod("25/03/2026", "28/03/2026");
        assertEquals("28/03/2026", bp.getEndDateString());
    }

    @Test
    public void toStringMethod() {
        BusyPeriod bp = new BusyPeriod("25/03/2026", "28/03/2026");
        assertEquals("25/03/2026 to 28/03/2026", bp.toString());
    }

    @Test
    public void hashCode_equalObjects_sameHashCode() {
        BusyPeriod bp1 = new BusyPeriod("25/03/2026", "28/03/2026");
        BusyPeriod bp2 = new BusyPeriod("25/03/2026", "28/03/2026");
        assertEquals(bp1.hashCode(), bp2.hashCode());
    }

    @Test
    public void merge_emptySet_returnsEmptySet() {
        assertTrue(BusyPeriod.merge(new java.util.HashSet<>()).isEmpty());
    }

    @Test
    public void merge_singlePeriod_returnsSamePeriod() {
        java.util.Set<BusyPeriod> periods = new java.util.HashSet<>();
        periods.add(new BusyPeriod("01/01/2026", "05/01/2026"));
        java.util.Set<BusyPeriod> merged = BusyPeriod.merge(periods);
        assertEquals(1, merged.size());
        assertTrue(merged.contains(new BusyPeriod("01/01/2026", "05/01/2026")));
    }

    @Test
    public void merge_overlappingPeriods_merged() {
        java.util.Set<BusyPeriod> periods = new java.util.HashSet<>();
        periods.add(new BusyPeriod("01/01/2026", "05/01/2026"));
        periods.add(new BusyPeriod("04/01/2026", "10/01/2026"));
        java.util.Set<BusyPeriod> merged = BusyPeriod.merge(periods);
        assertEquals(1, merged.size());
        assertTrue(merged.contains(new BusyPeriod("01/01/2026", "10/01/2026")));
    }

    @Test
    public void merge_adjacentPeriods_merged() {
        java.util.Set<BusyPeriod> periods = new java.util.HashSet<>();
        periods.add(new BusyPeriod("01/01/2026", "05/01/2026"));
        periods.add(new BusyPeriod("06/01/2026", "10/01/2026"));
        java.util.Set<BusyPeriod> merged = BusyPeriod.merge(periods);
        assertEquals(1, merged.size());
        assertTrue(merged.contains(new BusyPeriod("01/01/2026", "10/01/2026")));
    }

    @Test
    public void merge_nonOverlappingPeriods_notMerged() {
        java.util.Set<BusyPeriod> periods = new java.util.HashSet<>();
        periods.add(new BusyPeriod("01/01/2026", "05/01/2026"));
        periods.add(new BusyPeriod("07/01/2026", "10/01/2026"));
        java.util.Set<BusyPeriod> merged = BusyPeriod.merge(periods);
        assertEquals(2, merged.size());
        assertTrue(merged.contains(new BusyPeriod("01/01/2026", "05/01/2026")));
        assertTrue(merged.contains(new BusyPeriod("07/01/2026", "10/01/2026")));
    }
}
