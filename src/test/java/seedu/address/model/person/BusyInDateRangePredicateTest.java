package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class BusyInDateRangePredicateTest {

    @Test
    public void equals_sameObject_returnsTrue() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        assertTrue(predicate.equals(predicate));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        BusyInDateRangePredicate predicate1 = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        BusyInDateRangePredicate predicate2 = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        assertTrue(predicate1.equals(predicate2));
    }

    @Test
    public void equals_differentStartDate_returnsFalse() {
        BusyInDateRangePredicate predicate1 = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        BusyInDateRangePredicate predicate2 = new BusyInDateRangePredicate("02/01/2026", "31/12/2026");
        assertFalse(predicate1.equals(predicate2));
    }

    @Test
    public void equals_differentEndDate_returnsFalse() {
        BusyInDateRangePredicate predicate1 = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        BusyInDateRangePredicate predicate2 = new BusyInDateRangePredicate("01/01/2026", "30/12/2026");
        assertFalse(predicate1.equals(predicate2));
    }

    @Test
    public void equals_null_returnsFalse() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        assertFalse(predicate.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        assertFalse(predicate.equals("not a predicate"));
    }

    // ----------------------------- test() -----------------------------

    @Test
    public void test_personBusyPeriodFullyInsideRange_returnsTrue() {
        // Person's busy period 01/06/2026–30/06/2026 is entirely within query 01/01/2026–31/12/2026
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        Person person = new PersonBuilder().withBusyPeriod("01/06/2026", "30/06/2026").build();
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_personBusyPeriodFullyCoversRange_returnsTrue() {
        // Person's busy period spans the entire query range
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/06/2026", "30/06/2026");
        Person person = new PersonBuilder().withBusyPeriod("01/01/2026", "31/12/2026").build();
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_personBusyPeriodOverlapsAtStart_returnsTrue() {
        // Person's busy period ends inside the query range
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("15/06/2026", "31/12/2026");
        Person person = new PersonBuilder().withBusyPeriod("01/01/2026", "16/06/2026").build();
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_personBusyPeriodOverlapsAtEnd_returnsTrue() {
        // Person's busy period starts inside the query range
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "15/06/2026");
        Person person = new PersonBuilder().withBusyPeriod("14/06/2026", "31/12/2026").build();
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_personBusyPeriodExactlyMatchesRange_returnsTrue() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        Person person = new PersonBuilder().withBusyPeriod("01/01/2026", "31/12/2026").build();
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_personBusyPeriodAdjacentBeforeRange_returnsFalse() {
        // Person's busy period ends exactly one day before the query starts — no overlap
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/06/2026", "31/12/2026");
        Person person = new PersonBuilder().withBusyPeriod("01/01/2026", "31/05/2026").build();
        assertFalse(predicate.test(person));
    }

    @Test
    public void test_personBusyPeriodAdjacentAfterRange_returnsFalse() {
        // Person's busy period starts exactly one day after the query ends — no overlap
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/05/2026");
        Person person = new PersonBuilder().withBusyPeriod("01/06/2026", "31/12/2026").build();
        assertFalse(predicate.test(person));
    }

    @Test
    public void test_personBusyPeriodEntirelyBeforeRange_returnsFalse() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/07/2026", "31/12/2026");
        Person person = new PersonBuilder().withBusyPeriod("01/01/2026", "30/06/2026").build();
        assertFalse(predicate.test(person));
    }

    @Test
    public void test_personBusyPeriodEntirelyAfterRange_returnsFalse() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "30/06/2026");
        Person person = new PersonBuilder().withBusyPeriod("01/07/2026", "31/12/2026").build();
        assertFalse(predicate.test(person));
    }

    @Test
    public void test_personHasNoBusyPeriod_returnsFalse() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        // PersonBuilder with no withBusyPeriod() call leaves busyPeriod as Optional.empty()
        Person person = new PersonBuilder().build();
        assertFalse(predicate.test(person));
    }

    @Test
    public void toStringMethod() {
        BusyInDateRangePredicate predicate = new BusyInDateRangePredicate("01/01/2026", "31/12/2026");
        String expected = BusyInDateRangePredicate.class.getCanonicalName()
                + "{startDate=01/01/2026, endDate=31/12/2026}";
        assertEquals(expected, predicate.toString());
    }
}
