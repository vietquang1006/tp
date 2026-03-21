package seedu.address.model.person;

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
        assertFalse(BusyPeriod.isValidDateFormat("32/03/2026")); // invalid day
        assertFalse(BusyPeriod.isValidDateFormat("25/13/2026")); // invalid month

        // valid formats
        assertTrue(BusyPeriod.isValidDateFormat("25/03/2026"));
        assertTrue(BusyPeriod.isValidDateFormat("01/01/2000"));
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
}
