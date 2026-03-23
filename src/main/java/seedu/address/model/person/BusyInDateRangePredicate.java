package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person} is busy in the given date range.
 */
public class BusyInDateRangePredicate implements Predicate<Person> {
    private final String startDate;
    private final String endDate;
    private final BusyPeriod range;

    /**
     * Creates a predicate that checks if a person's busy periods overlap with the given date range.
     *
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     */
    public BusyInDateRangePredicate(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.range = new BusyPeriod(startDate, endDate);
    }

    /**
     * Returns true if the person has a busy period that overlaps with the specified date range.
     * If the person does not have a busy period, returns false.
     * @param person The person to test.
     * @return true if the person is busy in the date range, false otherwise.
     */
    @Override
    public boolean test(Person person) {
        return person.getBusyPeriod()
                .map(bp -> bp.overlapsWith(this.range))
                .orElse(false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BusyInDateRangePredicate otherPredicate)) {
            return false;
        }
        return startDate.equals(otherPredicate.startDate) && endDate.equals(otherPredicate.endDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("startDate", startDate).add("endDate", endDate).toString();
    }
}
