package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Objects;

/**
 * Represents a Person's busy period in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateFormat(String)}
 */
public class BusyPeriod {

    public static final String MESSAGE_CONSTRAINTS = "Dates must follow the DD/MM/YYYY format (e.g., 25/03/2026).";
    public static final String MESSAGE_DATE_LOGIC = "The start date cannot be later than the end date.";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public final LocalDate startDate;
    public final LocalDate endDate;

    /**
     * Constructs a {@code BusyPeriod}.
     *
     * @param startDate A valid start date.
     * @param endDate A valid end date.
     */
    public BusyPeriod(String startDate, String endDate) {
        requireNonNull(startDate);
        requireNonNull(endDate);
        checkArgument(isValidDateFormat(startDate), MESSAGE_CONSTRAINTS);
        checkArgument(isValidDateFormat(endDate), MESSAGE_CONSTRAINTS);

        LocalDate start = LocalDate.parse(startDate, DATE_FORMATTER);
        LocalDate end = LocalDate.parse(endDate, DATE_FORMATTER);

        checkArgument(isValidBusyPeriod(start, end), MESSAGE_DATE_LOGIC);

        this.startDate = start;
        this.endDate = end;
    }

    /**
     * Returns true if a given string is a valid date format.
     */
    public static boolean isValidDateFormat(String test) {
        try {
            LocalDate.parse(test, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns true if the start date is before or equal to the end date.
     */
    public static boolean isValidBusyPeriod(LocalDate startDate, LocalDate endDate) {
        return !startDate.isAfter(endDate);
    }

    /**
     * Returns true if the given date is within the busy period.
     */
    public boolean isWithinPeriod(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public String getStartDateString() {
        return startDate.format(DATE_FORMATTER);
    }

    public String getEndDateString() {
        return endDate.format(DATE_FORMATTER);
    }

    @Override
    public String toString() {
        return startDate.format(DATE_FORMATTER) + " to " + endDate.format(DATE_FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BusyPeriod)) {
            return false;
        }

        BusyPeriod otherBusyPeriod = (BusyPeriod) other;
        return startDate.equals(otherBusyPeriod.startDate)
                && endDate.equals(otherBusyPeriod.endDate);
    }

    /**
     * Returns true if this busy period overlaps with another busy period.
     * @param other
     * @return true if the two busy periods overlap, false otherwise
     */
    public boolean overlapsWith(BusyPeriod other) {
        return !this.endDate.isBefore(other.startDate) && !this.startDate.isAfter(other.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

}
